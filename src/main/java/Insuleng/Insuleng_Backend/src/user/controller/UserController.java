package Insuleng.Insuleng_Backend.src.user.controller;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.src.user.dto.MyPageDto;
import Insuleng.Insuleng_Backend.src.user.dto.MyPageInfoDto;
import Insuleng.Insuleng_Backend.src.user.service.AuthService;
import Insuleng.Insuleng_Backend.src.user.service.UserService;
import Insuleng.Insuleng_Backend.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    @GetMapping("profiles")
    @Operation(summary = "마이페이지 api", description = "즐겨찾기한 음식점, 내가 쓴 리뷰, 좋아요 누른 음식점, 내가 쓴 글, 스크랩한 글의 개수가 출력", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다")
    })
    //추후 bookmark, review, heart, scrap 개수가 출력되는지 테스트해보기
    public BaseResponse<MyPageDto> getMyPage(){
        try{
            Long userId = SecurityUtil.getCurrentUserId();
            MyPageDto myPageDto = userService.getMyPage(userId);

            return new BaseResponse<>(myPageDto);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }


    @GetMapping("profiles/update")
    @Operation(summary = "정보 수정 폼을 제공하는 api", description = "내 개인정보를 보여주며, 이곳에서 정보를 수정가능합니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다")
    })
    public BaseResponse<MyPageInfoDto> getMyPageInfo(){
        try{
            Long userId = SecurityUtil.getCurrentUserId();
            MyPageInfoDto myPageInfoDto = userService.getMyPageInfo(userId);

            return new BaseResponse<>(myPageInfoDto);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}
