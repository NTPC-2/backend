package Insuleng.Insuleng_Backend.src.home.controller;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.src.home.dto.HomepageSummaryDto;
import Insuleng.Insuleng_Backend.src.home.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
