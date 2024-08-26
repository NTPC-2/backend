package Insuleng.Insuleng_Backend.src.community.service;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.community.dto.*;
import Insuleng.Insuleng_Backend.src.community.entity.PostEntity;
import Insuleng.Insuleng_Backend.src.community.repository.CommunityRepository;
import Insuleng.Insuleng_Backend.src.community.repository.PostRepository;
import Insuleng.Insuleng_Backend.src.storage.S3Uploader;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import Insuleng.Insuleng_Backend.src.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;

    public CommunityService(CommunityRepository communityRepository, UserRepository userRepository, PostRepository postRepository, S3Uploader s3Uploader) {

        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.s3Uploader = s3Uploader;
    }

    public void createPost(Long userId, PostDto postDto) {
        //실제 존재하는 유저인지 검사
        boolean userExist = communityRepository.testUserId(userId);
        if(userExist == false){
            throw new BaseException(BaseResponseStatus.USER_NO_EXIST);
        }
        else {
            //게시글 작성
            String imgUrl;

            if(postDto.getImgUrl().isEmpty() || Objects.isNull(postDto.getImgUrl().getOriginalFilename()) || postDto.getImgUrl() == null){
                imgUrl = null;
            }
            else{
                imgUrl = s3Uploader.upload(postDto.getImgUrl());
            }

            communityRepository.createPost(userId, postDto, imgUrl);
        }
    }
    @Transactional
    public void deletePost(Long userId, Long postId){
        //실제 존재하는 유저인지 검사
        boolean userExist = communityRepository.testUserId(userId);
        if(!userExist){
            throw new BaseException(BaseResponseStatus.USER_NO_EXIST);
        }

        //작성한 유저id랑 삭제하려는 유저id 같은지 검사
        boolean isUserOwner = communityRepository.isUserOwnerOfPost(userId, postId);

        if (!isUserOwner) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }

        //postid존재 + status ACTIVE 검사
        boolean postExist = communityRepository.isPostByIdAndStatusActive(postId);
        if (!postExist) {
            throw new BaseException(BaseResponseStatus.POST_EMPTY);
        }

        communityRepository.deletePost(userId, postId);
    }

    @Transactional
    public void updatePost(Long userId, UpdatePostDto updatePostDto){
        //실제 존재하는 유저인지 검사
        boolean userExist = communityRepository.testUserId(userId);
        if(userExist == false){
            throw new BaseException(BaseResponseStatus.USER_NO_EXIST);
        }
        //작성한 유저id랑 수정하려는 유저id 같은지 검사
        boolean isUserOwner = communityRepository.isUserOwnerOfPost(userId, updatePostDto.getPostId());

        if (!isUserOwner) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }
        //postid존재 + status ACTIVE 검사
        boolean postExist = communityRepository.isPostByIdAndStatusActive( updatePostDto.getPostId());
        if (!postExist) {
            throw new BaseException(BaseResponseStatus.POST_EMPTY);
        }

        String imgUrl;

        if(updatePostDto.getImgUrl().isEmpty() || Objects.isNull(updatePostDto.getImgUrl().getOriginalFilename()) || updatePostDto.getImgUrl() == null){
            imgUrl = null;
        }
        else{
            imgUrl = s3Uploader.upload(updatePostDto.getImgUrl());
        }

        communityRepository.updatePost(userId,  updatePostDto, imgUrl);
    }

    public PostListDto searchPosts(Long userId, String keyword) {
        //검색어가 없는경우
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.KEYWORD_EMPTY);
        }
        // 키워드 길이 제한
        if (keyword.length() > 100) {
            keyword = keyword.substring(0, 100);
        }
        List<PostSummaryDto> postSummaryList = communityRepository.searchPosts(userId, keyword);

        //결과가 없는경우
        if (postSummaryList.isEmpty()) {
            throw new BaseException(BaseResponseStatus.POST_EMPTY);
        }
        return new PostListDto(postSummaryList.size(), postSummaryList);
    }

    public void addPostLike(Long userId, Long postId) {
        // 존재하는 유저인지
        boolean userExist = communityRepository.testUserId(userId);
        if (!userExist) {
            throw new BaseException(BaseResponseStatus.USER_NO_EXIST);
        }

        //postid존재 + status ACTIVE 검사
        boolean postExist = communityRepository.isPostByIdAndStatusActive(postId);
        if (!postExist) {
            throw new BaseException(BaseResponseStatus.POST_EMPTY);
        }

        //like 이미 되어있는지
        boolean alreadyLiked = communityRepository.checkPostLike(userId, postId);
        if (alreadyLiked) {
            throw new BaseException(BaseResponseStatus.ALREADY_POST_LIKE);
        }

        // 이전에 like가 해제된 기록 확인
        boolean previouslyLiked = communityRepository.checkPostLikeInactive(userId, postId);
        if (previouslyLiked) {
            // 이전 기록이 있으면 다시 활성화 (새 레코드 추가하지 않음)
            communityRepository.updatePostLikeStatus(userId, postId, Status.ACTIVE);
        } else {
            // 이전 기록이 없으면 새로운 like
            communityRepository.addPostLike(userId, postId);
        }

        // 좋아요 수 증가
        communityRepository.increasePostLikeCount(postId);
    }

    public void removePostLike(Long userId, Long postId) {
        //존재하는 유저인지
        boolean userExist = communityRepository.testUserId(userId);
        if(userExist == false){
            throw new BaseException(BaseResponseStatus.USER_NO_EXIST);
        }
        //postid존재 + status ACTIVE 검사
        boolean postExist = communityRepository.isPostByIdAndStatusActive(postId);
        if (!postExist) {
            throw new BaseException(BaseResponseStatus.POST_EMPTY);
        }
        //like 이미 해제되어있는지
        boolean alreadyLiked = communityRepository.checkPostLike(userId, postId);
        if (!alreadyLiked) {
            throw new BaseException(BaseResponseStatus.ALREADY_POST_NO_LIKE);
        }

        communityRepository.updatePostLikeStatus(userId, postId, Status.INACTIVE);
        communityRepository.decreasePostLikeCount(postId);
    }

    public void addCommentLike(Long userId, Long commentId) {
        // 존재하는 유저인지
        boolean userExist = communityRepository.testUserId(userId);
        if (!userExist) {
            throw new BaseException(BaseResponseStatus.USER_NO_EXIST);
        }

        //commentid존재 + status ACTIVE 검사
        boolean commentExist = communityRepository.isCommentByIdAndStatusActive(commentId);
        if (!commentExist) {
            throw new BaseException(BaseResponseStatus.COMMENT_EMPTY);
        }

        //like 이미 되어있는지
        boolean alreadyLiked = communityRepository.checkCommentLike(userId, commentId);
        if (alreadyLiked) {
            throw new BaseException(BaseResponseStatus.ALREADY_COMMENT_LIKE);
        }

        // 이전에 like가 해제된 기록 확인
        boolean previouslyLiked = communityRepository.checkCommentLikeInactive(userId, commentId);
        if (previouslyLiked) {
            // 이전 기록이 있으면 다시 활성화 (새 레코드 추가하지 않음)
            communityRepository.updateCommentLikeStatus(userId, commentId, Status.ACTIVE);
        } else {
            // 이전 기록이 없으면 새로운 like
            communityRepository.addCommentLike(userId, commentId);
        }

        // 좋아요 수 증가
        communityRepository.increaseCommentLikeCount(commentId);
    }

    public void removeCommentLike(Long userId, Long commentId) {
        //존재하는 유저인지
        boolean userExist = communityRepository.testUserId(userId);
        if(userExist == false){
            throw new BaseException(BaseResponseStatus.USER_NO_EXIST);
        }
        //commentid존재 + status ACTIVE 검사
        boolean commentExist = communityRepository.isCommentByIdAndStatusActive(commentId);
        if (!commentExist) {
            throw new BaseException(BaseResponseStatus.COMMENT_EMPTY);
        }
        //like 이미 해제되어있는지
        boolean alreadyLiked = communityRepository.checkCommentLike(userId, commentId);
        if (!alreadyLiked) {
            throw new BaseException(BaseResponseStatus.ALREADY_COMMENT_NO_LIKE);
        }

        communityRepository.updateCommentLikeStatus(userId, commentId, Status.INACTIVE);
        communityRepository.decreaseCommentLikeCount(commentId);
    }

    public void addPostScrap(Long userId, Long postId) {
        // 존재하는 유저인지
        boolean userExist = communityRepository.testUserId(userId);
        if (!userExist) {
            throw new BaseException(BaseResponseStatus.USER_NO_EXIST);
        }

        //postid존재 + status ACTIVE 검사
        boolean postExist = communityRepository.isPostByIdAndStatusActive(postId);
        if (!postExist) {
            throw new BaseException(BaseResponseStatus.POST_EMPTY);
        }

        //like 이미 되어있는지
        boolean alreadyScrap = communityRepository.checkPostScrap(userId, postId);
        if (alreadyScrap) {
            throw new BaseException(BaseResponseStatus.ALREADY_POST_SCRAP);
        }

        // 이전에 like가 해제된 기록 확인
        boolean previouslyScrap = communityRepository.checkPostScrapInactive(userId, postId);
        if (previouslyScrap) {
            // 이전 기록이 있으면 다시 활성화 (새 레코드 추가하지 않음)
            communityRepository.updatePostScrapStatus(userId, postId, Status.ACTIVE);
        } else {
            // 이전 기록이 없으면 새로운 like
            communityRepository.addPostScrap(userId, postId);
        }

        // 좋아요 수 증가
        communityRepository.increasePostScrapCount(postId);
    }
    public void removePostScrap(Long userId, Long postId) {
        //존재하는 유저인지
        boolean userExist = communityRepository.testUserId(userId);
        if(userExist == false){
            throw new BaseException(BaseResponseStatus.USER_NO_EXIST);
        }
        //postid존재 + status ACTIVE 검사
        boolean postExist = communityRepository.isPostByIdAndStatusActive(postId);
        if (!postExist) {
            throw new BaseException(BaseResponseStatus.POST_EMPTY);
        }
        //scrap 이미 해제되어있는지
        boolean alreadyScrap = communityRepository.checkPostScrap(userId, postId);
        if (!alreadyScrap) {
            throw new BaseException(BaseResponseStatus.ALREADY_POST_NO_SCRAP);
        }

        communityRepository.updatePostScrapStatus(userId, postId, Status.INACTIVE);
        communityRepository.decreasePostScrapCount(postId);
    }

    public PostListDto getPostslist(){
        List<PostSummaryDto> postSummaryList = communityRepository.findAllPosts();
        //결과가 없는경우
        if (postSummaryList.isEmpty()) {
            throw new BaseException(BaseResponseStatus.POST_EMPTY);
        }
        return new PostListDto(postSummaryList.size(), postSummaryList);
    }
    public PostDetailsDto getPostDetails(Long userId, Long postId) {
        //존재하는 유저인지
        boolean userExist = communityRepository.testUserId(userId);
        if (userExist == false) {
            throw new BaseException(BaseResponseStatus.USER_NO_EXIST);
        }
        // 게시글 세부정보를 가져오기
        PostDetailsDto postDetailsDto = communityRepository.getPostDetails(postId);
        if (postDetailsDto == null) {
            throw new BaseException(BaseResponseStatus.POST_EMPTY);
        }
        return postDetailsDto;
    }

    @Transactional
    public void createComment(Long userId, Long postId, CommentRequestDto commentRequestDto){

       String postStatus = communityRepository.checkPostStatus(postId);

       if(postStatus.equals("ACTIVE")) {
           communityRepository.createComment(userId, postId, commentRequestDto);
           communityRepository.increaseCountComment(postId);
       }
       else{
           throw new BaseException(BaseResponseStatus.POST_EMPTY);
       }

    }

    @Transactional
    public void createReplyComment(Long userId, Long postId, Long parentCommentId, CommentRequestDto commentRequestDto) {

        String postStatus = communityRepository.checkPostStatus(postId);

        if(postStatus.equals("ACTIVE")){
            communityRepository.createReplyComment(userId, postId, parentCommentId, commentRequestDto);
            communityRepository.increaseCountComment(postId);
        }else{
            throw new BaseException(BaseResponseStatus.POST_EMPTY);
        }


    }

    public void updateComment(Long userId, Long commentId, UpdateCommentDto updateCommentDto) {

        String commentStatus = communityRepository.checkCommentStatus(commentId);

        if(commentStatus.equals("ACTIVE")){
            Long findUserIdByCommentId = communityRepository.findUserIdByCommentId(commentId);

            if(findUserIdByCommentId.longValue() != userId.longValue()){
                throw new BaseException(BaseResponseStatus.NO_PRIVILEGE_COMMENT);
            }

            communityRepository.updateComment(userId, commentId, updateCommentDto);
        }else{
            throw new BaseException(BaseResponseStatus.COMMENT_EMPTY);
        }

    }

//    @Transactional
//    public void createComment(Long userId, Long postId, CommentRequestDto commentRequestDto) {
//        // groupNumber 설정: 새로운 댓글인 경우
//        int groupNumber = communityRepository.getMaxGroupNumber(postId) + 1;
//
//        // 기본적으로 댓글은 commentLevel이 0 (루트 댓글)
//        int commentLevel = 0;
//        Long parentCommentId = null;
//
//        // 대댓글인 경우 처리
//        if (commentRequestDto.getParentCommentId() != null) {
//            parentCommentId = commentRequestDto.getParentCommentId();
//            commentLevel = communityRepository.getCommentLevel(parentCommentId) + 1;
//            groupNumber = communityRepository.getGroupNumber(parentCommentId); // 부모 댓글의 groupNumber 사용
//        }
//
//        communityRepository.insertComment(userId, postId, commentRequestDto, groupNumber, commentLevel, parentCommentId);
//    }

//    @Transactional
//    public Long createComment(Long userId, Long postId, CommentRequestDto commentRequestDto) {
//        boolean userExist = communityRepository.testUserId(userId);
//        if(userExist == false){
//            throw new BaseException(BaseResponseStatus.USER_NO_EXIST);
//        }
//        //postid존재 + status ACTIVE 검사
//        boolean postExist = communityRepository.isPostByIdAndStatusActive(postId);
//        if (!postExist) {
//            throw new BaseException(BaseResponseStatus.POST_EMPTY);
//        }
//        //게시글 작성
//        return communityRepository.createComment(userId, postId, commentRequestDto);
//    }
//
//
//    @Transactional
//    public Long createReplyComment(Long userId, Long postId, Long parentId, CommentRequestDto commentRequestDto) {
//        boolean userExist = communityRepository.testUserId(userId);
//        if (userExist == false) {
//            throw new BaseException(BaseResponseStatus.USER_NO_EXIST);
//        }
//        //postid존재 + status ACTIVE 검사
//        boolean postExist = communityRepository.isPostByIdAndStatusActive(postId);
//        if (!postExist) {
//            throw new BaseException(BaseResponseStatus.POST_EMPTY);
//        }
//        boolean parentCommentExists = communityRepository.isCommentById(parentId);
//        if (!parentCommentExists) {
//            throw new BaseException(BaseResponseStatus.COMMENT_EMPTY);
//        }
//        return communityRepository.createReplyComment(userId, postId, parentId, commentRequestDto);
//
//    }

}
