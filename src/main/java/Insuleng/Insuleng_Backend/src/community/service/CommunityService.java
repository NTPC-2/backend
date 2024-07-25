package Insuleng.Insuleng_Backend.src.community.service;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.src.community.dto.PostDto;
import Insuleng.Insuleng_Backend.src.community.repository.CommunityRepository;
import org.springframework.stereotype.Service;

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
            throw new BaseException(BaseResponseStatus.USER_EMPTY);
        }
        else{
            //게시글 작성
            communityRepository.createPost(userId, postDto);
        }



    }
}
