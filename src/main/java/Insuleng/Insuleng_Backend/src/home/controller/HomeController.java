package Insuleng.Insuleng_Backend.src.home.controller;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.src.home.dto.BookmarkRestaurantDto;
import Insuleng.Insuleng_Backend.src.home.dto.HomepageSummaryDto;
import Insuleng.Insuleng_Backend.src.home.dto.RouletteListDto;
import Insuleng.Insuleng_Backend.src.home.service.HomeService;
import Insuleng.Insuleng_Backend.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("")
    @Operation(summary = "홈 화면 보기 api", description = "좋아요 수가 많은 레스토랑과 좋아요 수가 많은 게시글 목록 불러오기", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
    })
    public BaseResponse<HomepageSummaryDto> getHomePage(){
        try{
            return new BaseResponse<>(homeService.getHomePage());

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    @GetMapping("/roulette")
    @Operation(summary = "룰렛 화면 api", description = "내가 즐겨찾기한 음식점 목록 출력(음식점 아이디와, 음식점 이름 return", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다")
    })
    public BaseResponse<List<BookmarkRestaurantDto>> getRouletteList(){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

            return new BaseResponse<>(homeService.getRouletteList(userId));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }


    }

}
