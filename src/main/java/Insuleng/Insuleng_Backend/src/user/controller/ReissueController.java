package Insuleng.Insuleng_Backend.src.user.controller;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.jwt.JWTUtil;
import Insuleng.Insuleng_Backend.src.user.service.ReissueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueService reissueService;

    @Operation(summary = "토큰 재발급 api", description = "access, refresh 토큰을 재발급합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2300", description = "refresh 토큰이 존재하지 않습니다"),
            @ApiResponse(responseCode = "2305", description = "refresh 토큰이 만료되었습니다"),
            @ApiResponse(responseCode = "2310", description = "refresh 토큰이 올바르지 않습니다")
    })
    @PostMapping("reissue")
    public BaseResponse<String> reissue(HttpServletRequest request, HttpServletResponse response){

        try{
            reissueService.reissue(request, response);
            return new BaseResponse<>("access 토큰이 정상 발급되었습니다");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }



}
