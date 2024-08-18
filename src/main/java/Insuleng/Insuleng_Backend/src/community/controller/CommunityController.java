package Insuleng.Insuleng_Backend.src.community.controller;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.src.community.dto.*;
import Insuleng.Insuleng_Backend.src.community.service.CommunityService;
import Insuleng.Insuleng_Backend.utils.SecurityUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CommunityController {
    private final CommunityService communityService;
    private final Logger logger = LoggerFactory.getLogger(CommunityService.class);


    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @PostMapping ("/post")
    public BaseResponse<String> createPost (@RequestBody @Valid PostDto postDto){
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            communityService.createPost(userId,postDto);
            return new BaseResponse<>("게시글 작성 성공");
        }
        catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
    @DeleteMapping("/post/delete")
    public BaseResponse<String> deletePost (@RequestBody @Valid DeletePostDto deletePostDto){
        try {
            Long userId = SecurityUtil.getCurrentUserId();

            communityService.deletePost(userId, deletePostDto.getPostId());

            return new BaseResponse<>("게시글 삭제 성공");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


    @PutMapping("/post/update")
    public BaseResponse<String> updatePost (@RequestBody @Valid UpdatePostDto updatePostDto){
        try{
            Long userId = SecurityUtil.getCurrentUserId();
            communityService.updatePost(userId, updatePostDto);
            return new BaseResponse<>("게시글 수정 성공");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/post/search")
    public BaseResponse<PostListDto> searchPosts(@RequestBody(required = false) SearchRequestDto searchRequestDto) {
        Long userId = SecurityUtil.getCurrentUserId();

        String keyword = (searchRequestDto != null) ? searchRequestDto.getKeyword() : null;

        try {
            PostListDto result = communityService.searchPosts(userId, keyword);
            return new BaseResponse<>(result);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("post/addpostlike/{post_id}")
    public BaseResponse<String> addPostLike(@PathVariable("post_id") Long postId){
        try{
            Long userId = SecurityUtil.getCurrentUserId();
            communityService.addPostLike(userId, postId);
            return new BaseResponse<>("좋아요를 눌렀습니다");
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("post/removepostlike/{post_id}")
    public BaseResponse<String> removePostLike(@PathVariable("post_id") Long postId){
        try{
            Long userId = SecurityUtil.getCurrentUserId();
            communityService.removePostLike(userId, postId);
            return new BaseResponse<>("좋아요를 해제했습니다");
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    @PostMapping("comment/addcommentlike/{comment_id}")
    public BaseResponse<String> addCommentLike(@PathVariable("comment_id") Long commentId){
        try{
            Long userId = SecurityUtil.getCurrentUserId();
            communityService.addCommentLike(userId, commentId);
            return new BaseResponse<>("좋아요를 눌렀습니다");
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("comment/removecommentlike/{comment_id}")
    public BaseResponse<String> removeCommentLike(@PathVariable("comment_id") Long commentId){
        try{
            Long userId = SecurityUtil.getCurrentUserId();
            communityService.removeCommentLike(userId, commentId);
            return new BaseResponse<>("좋아요를 해제했습니다");
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("post/addpostscrap/{post_id}")
    public BaseResponse<String> addPostScrap(@PathVariable("post_id") Long postId){
        try{
            Long userId = SecurityUtil.getCurrentUserId();
            communityService.addPostScrap(userId, postId);
            return new BaseResponse<>("게시글을 스크랩 했습니다");
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    @PatchMapping("post/removepostscrap/{post_id}")
    public BaseResponse<String> removePostScrap(@PathVariable("post_id") Long postId){
        try{
            Long userId = SecurityUtil.getCurrentUserId();
            communityService.removePostScrap(userId, postId);
            return new BaseResponse<>("스크랩을 해제했습니다");
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

}
