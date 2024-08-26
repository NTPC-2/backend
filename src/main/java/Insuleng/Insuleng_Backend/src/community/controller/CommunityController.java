package Insuleng.Insuleng_Backend.src.community.controller;

import Insuleng.Insuleng_Backend.auth.CustomUserDetails;
import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.src.community.dto.*;
import Insuleng.Insuleng_Backend.src.community.entity.PostEntity;
import Insuleng.Insuleng_Backend.src.community.repository.CommunityRepository;
import Insuleng.Insuleng_Backend.src.community.service.CommunityService;
import Insuleng.Insuleng_Backend.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CommunityController {

    private final CommunityService communityService;
    private final Logger logger = LoggerFactory.getLogger(CommunityService.class);

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @PostMapping (value = "/post" , consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<String> createPost (@Valid PostDto postDto){
        try {
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

            System.out.println("error 건너뜀");
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
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

            communityService.deletePost(userId, deletePostDto.getPostId());

            return new BaseResponse<>("게시글 삭제 성공");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PutMapping("/post/update")
    public BaseResponse<String> updatePost (@Valid UpdatePostDto updatePostDto){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

            communityService.updatePost(userId, updatePostDto);
            return new BaseResponse<>("게시글 수정 성공");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/post/search")
    public BaseResponse<PostListDto> searchPosts(@RequestBody(required = false) SearchRequestDto searchRequestDto) {
        Long userId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

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
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            communityService.addPostLike(userId, postId);
            return new BaseResponse<>("좋아요를 눌렀습니다");
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("post/removepostlike/{post_id}")
    public BaseResponse<String> removePostLike(@PathVariable("post_id") Long postId){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            communityService.removePostLike(userId, postId);
            return new BaseResponse<>("좋아요를 해제했습니다");
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    @PostMapping("comment/addcommentlike/{comment_id}")
    public BaseResponse<String> addCommentLike(@PathVariable("comment_id") Long commentId){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            communityService.addCommentLike(userId, commentId);
            return new BaseResponse<>("좋아요를 눌렀습니다");
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("comment/removecommentlike/{comment_id}")
    public BaseResponse<String> removeCommentLike(@PathVariable("comment_id") Long commentId){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

            communityService.removeCommentLike(userId, commentId);
            return new BaseResponse<>("좋아요를 해제했습니다");
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("post/addpostscrap/{post_id}")
    public BaseResponse<String> addPostScrap(@PathVariable("post_id") Long postId){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            communityService.addPostScrap(userId, postId);
            return new BaseResponse<>("게시글을 스크랩 했습니다");
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    @PatchMapping("post/removepostscrap/{post_id}")
    public BaseResponse<String> removePostScrap(@PathVariable("post_id") Long postId){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

            communityService.removePostScrap(userId, postId);
            return new BaseResponse<>("스크랩을 해제했습니다");
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    @GetMapping("post/list")
    public BaseResponse<PostListDto> getPostsList(){
        try {
            PostListDto postsList = communityService.getPostslist();
            return new BaseResponse<>(postsList);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    @GetMapping("post/details/{postId}")
    public BaseResponse<PostDetailsDto> getPostDetails(@PathVariable("postId") Long postId){
        try {
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

            PostDetailsDto postDetails = communityService.getPostDetails(userId, postId);
            return new BaseResponse<>(postDetails);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("comment/create/{postId}")
    @Operation(summary = "댓글 작성 api", description = "CommentRequestDto 의 정보로 값을 받는다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다"),
            @ApiResponse(responseCode = "4001", description = "게시글이 존재하지 않습니다")
    })
    public BaseResponse<String> createComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto) {
        try {
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            communityService.createComment(userId, postId, commentRequestDto);
            return new BaseResponse<>("댓글을 작성했습니다");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("comment/reply/{postId}/{parentCommentId}")
    @Operation(summary = "대댓글 작성 api", description = "CommentRequestDto 의 정보로 값을 받는다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다"),
            @ApiResponse(responseCode = "4001", description = "게시글이 존재하지 않습니다")
    })
    public BaseResponse<String> createComment(@PathVariable Long postId, @PathVariable Long parentCommentId, @RequestBody CommentRequestDto commentRequestDto) {
        try {
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            communityService.createReplyComment(userId, postId, parentCommentId, commentRequestDto);
            return new BaseResponse<>("대댓글을 작성했습니다");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PutMapping("comment/update/{commentId}")
    @Operation(summary = "댓글 수정 api", description = "UpdateCommentDto 의 정보로 값을 받는다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다"),
            @ApiResponse(responseCode = "4001", description = "게시글이 존재하지 않습니다"),
            @ApiResponse(responseCode = "4101", description = "댓글 작성자가 아니여서 글을 수정할 수 없습니다")
    })
    public BaseResponse<String> updateComment(@PathVariable Long commentId, @RequestBody UpdateCommentDto updateCommentDto){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

            communityService.updateComment(userId, commentId, updateCommentDto);
            return new BaseResponse<>("댓글을 수정했습니다");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }


    }


//
//    @ApiOperation(value = "댓글,대댓글 작성 API")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
//            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),
//            @ApiResponse(code = 400, message = "Bad Request"),
//            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
//            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
//    })
//    @PostMapping(value = {"/{post_id}/details/comment" , "/{post_id}/details/comment/{parent_id}"})
//    @PreAuthorize("hasAnyRole('USER')")
//    public BaseResponse<GetCommentIdDto> createComment(@PathVariable("post_id")Long post_id, @PathVariable(value = "parent_id", required = false) Long parent_id, @RequestBody @Valid CommentDto commentDto){
//
//        if(parent_id == null){
//            try{
//                String currentUserId = SecurityUtil.getCurrentUserId()
//                        .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
//                Long userId = Long.parseLong(currentUserId);
//
//                return new BaseResponse<>(new GetCommentIdDto(communityService.createComment(userId, post_id, commentDto)));
//            } catch(BaseException e){
//                return new BaseResponse<>(e.getStatus());
//            }
//
//        }
//        else{
//            try{
//                String currentUserId = SecurityUtil.getCurrentUserId()
//                        .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
//                Long userId = Long.parseLong(currentUserId);
//
//                return new BaseResponse<>(new GetCommentIdDto(communityService.createReplyComment(userId, post_id, parent_id, commentDto)));
//            }
//            catch (BaseException e){
//                return new BaseResponse<>(e.getStatus());
//            }
//        }
//    }

//    @PostMapping(value = {"/{post_id}/comment", "/{post_id}/comment/{parent_id}"})
//    public BaseResponse<GetCommentIdDto> createComment(
//            @PathVariable("post_id") Long postId,
//            @PathVariable(value = "parent_id", required = false) Long parentId,
//            @RequestBody @Valid CommentRequestDto commentRequestDto) {
//
//        try {
//            Long userId = SecurityUtil.getCurrentUserId()
//                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
//
//            Long commentId;
//            if (parentId == null) {
//                // 새 댓글 작성
//                commentId = communityService.createComment(userId, postId, commentRequestDto);
//            } else {
//                // 대댓글 작성
//                commentId = communityService.createReplyComment(userId, postId, parentId, commentRequestDto);
//            }
//
//            return new BaseResponse<>(new GetCommentIdDto(commentId));
//        } catch (BaseException e) {
//            return new BaseResponse<>(e.getStatus());
//        }
//    }
//
}
