package Insuleng.Insuleng_Backend.src.community.service;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.src.community.dto.PostDto;
import Insuleng.Insuleng_Backend.src.community.dto.PostListDto;
import Insuleng.Insuleng_Backend.src.community.dto.PostSummaryDto;
import Insuleng.Insuleng_Backend.src.community.dto.UpdatePostDto;
import Insuleng.Insuleng_Backend.src.community.repository.CommunityRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Service
public class CommunityService {

    private final CommunityRepository communityRepository;

    public CommunityService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    public void createPost(Long userId, PostDto postDto) {
        //실제 존재하는 유저인지 검사
        boolean userExist = communityRepository.testUserId(userId);
        if(userExist == false){
            throw new BaseException(BaseResponseStatus.USER_NO_EXIST);
        }
        else {
            //게시글 작성
            communityRepository.createPost(userId, postDto);
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
        System.out.println("isUserOwner: " + isUserOwner);  // 로그 추가

        if (!isUserOwner) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }
        //postid존재 + status ACTIVE 검사
        boolean postExist = communityRepository.isPostByIdAndStatusActive( updatePostDto.getPostId());
        if (!postExist) {
            throw new BaseException(BaseResponseStatus.POST_EMPTY);
        }

        communityRepository.deletePost(userId,  updatePostDto.getPostId());
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

}
