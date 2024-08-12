package Insuleng.Insuleng_Backend.src.user.controller;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.jwt.JWTUtil;
import Insuleng.Insuleng_Backend.src.user.service.ReissueService;
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
