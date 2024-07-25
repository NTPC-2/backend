package Insuleng.Insuleng_Backend.src.user.controller;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.src.user.dto.EmailDto;
import Insuleng.Insuleng_Backend.src.user.dto.FindEmailDto;
import Insuleng.Insuleng_Backend.src.user.dto.SignUpDto;
import Insuleng.Insuleng_Backend.src.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public BaseResponse<String> signUp(@RequestBody @Valid SignUpDto signUpDto){
        try{
            authService.signUp(signUpDto);

            return new BaseResponse<>("회원가입 성공!");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/test-email/{user_email}")
    public BaseResponse<String> checkEmailDuplicate(@PathVariable("user_email") String email){
        try{
            authService.checkEmailDuplicate(email);

            return new BaseResponse<>("이메일 사용 가능");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("test-nickname/{user_nickname}")
    public BaseResponse<String> checkNicknameDuplicate(@PathVariable("user_nickname") String nickname){
        try{
            authService.checkNicknameDuplicate(nickname);

            return new BaseResponse<>("닉네임 사용 가능");

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("recovery/email")
    public BaseResponse<EmailDto> findEmail(@RequestBody @Valid FindEmailDto findEmailDto){
        try{
            EmailDto emailDto = authService.findEmail(findEmailDto);

            return new BaseResponse<>(emailDto);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }


    }

}
