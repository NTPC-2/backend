package Insuleng.Insuleng_Backend.src.community.controller;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.src.community.dto.PostDto;
import Insuleng.Insuleng_Backend.src.community.service.CommunityService;
import Insuleng.Insuleng_Backend.utils.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
            return new BaseResponse<>("게시글작성 성공");
        }
        catch (BaseException e){
            return new BaseResponse<>(e.getMessage());
        }
    }

}
