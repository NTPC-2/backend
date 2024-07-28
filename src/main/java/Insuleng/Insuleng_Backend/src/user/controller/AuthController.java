package Insuleng.Insuleng_Backend.src.user.controller;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.src.user.dto.*;
import Insuleng.Insuleng_Backend.src.user.service.AuthService;
import Insuleng.Insuleng_Backend.src.user.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    //회원가입
    @PostMapping("/signup")
    public BaseResponse<String> signUp(@RequestBody @Valid SignUpDto signUpDto){
        try{
            authService.signUp(signUpDto);

            return new BaseResponse<>("회원가입 성공!");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
    //이메일 중복검사
    @GetMapping("/test-email/{user_email}")
    public BaseResponse<String> checkEmailDuplicate(@PathVariable("user_email") String email){
        try{
            authService.checkEmailDuplicate(email);

            return new BaseResponse<>("이메일 사용 가능");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //닉네임 중복검사
    @GetMapping("test-nickname/{user_nickname}")
    public BaseResponse<String> checkNicknameDuplicate(@PathVariable("user_nickname") String nickname){
        try{
            authService.checkNicknameDuplicate(nickname);

            return new BaseResponse<>("닉네임 사용 가능");

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //이메일 찾기
    @PostMapping("recovery/email")
    public BaseResponse<EmailDto> findEmail(@RequestBody @Valid FindEmailDto findEmailDto){
        try{
            EmailDto emailDto = authService.findEmail(findEmailDto);

            return new BaseResponse<>(emailDto);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //임시 비밀번호 발급
    @PostMapping("recovery/password")
    public BaseResponse<String> setTemporalPwd(@RequestBody @Valid EmailPostDto emailPostDto){
        try{
            EmailMessage emailMessage = EmailMessage.builder()
                    .to(emailPostDto.getEmail())
                    .subject("Insuleng 임시 비밀번호 발급")
                    .build();
            System.out.println("sendEmail 전");
            emailService.sendMail2(emailMessage);

            return new BaseResponse<>("이메일로 임시 비밀번호가 발급되었습니다.");

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

}
