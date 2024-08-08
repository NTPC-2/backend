package Insuleng.Insuleng_Backend.src.community.controller;

import Insuleng.Insuleng_Backend.auth.CustomUserDetails;
import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.src.community.dto.DeletePostDto;
import Insuleng.Insuleng_Backend.src.community.dto.PostDto;
import Insuleng.Insuleng_Backend.src.community.dto.SearchPostDto;
import Insuleng.Insuleng_Backend.src.community.dto.UpdatePostDto;
import Insuleng.Insuleng_Backend.src.community.entity.PostEntity;
import Insuleng.Insuleng_Backend.src.community.service.CommunityService;
import Insuleng.Insuleng_Backend.utils.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class CommunityController {
    private final CommunityService communityService;
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
            return new BaseResponse<>(e.getMessage());
        }
    }
    @DeleteMapping("/post/delete")
    public BaseResponse<String> deletePost (@RequestBody @Valid DeletePostDto deletePostDto){
        try {
            Long userId = SecurityUtil.getCurrentUserId();

            communityService.deletePost(userId, deletePostDto.getPostId());

            return new BaseResponse<>("게시글 삭제 성공");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getMessage());
        }
    }


    @PutMapping("/post/update")
    public BaseResponse<String> updatePost (@RequestBody @Valid UpdatePostDto updatePostDto){
        try{
            Long userId = SecurityUtil.getCurrentUserId();
            communityService.updatePost(userId, updatePostDto);
            return new BaseResponse<>("게시글 수정 성공");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getMessage());
        }
    }

    @GetMapping("/post/search")
    public BaseResponse<List<PostEntity>> searchPosts(@RequestBody @Valid SearchPostDto searchPostDto) {
        try {
            List<PostEntity> posts = communityService.searchPosts(searchPostDto);
            return new BaseResponse<>(posts,true, "게시글 검색 성공");
        } catch (BaseException e) {
            return new BaseResponse<>(null, false, e.getMessage());
        }
    }
}
