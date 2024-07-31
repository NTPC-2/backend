package Insuleng.Insuleng_Backend.src.user.controller;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.src.user.dto.*;
import Insuleng.Insuleng_Backend.src.user.service.AuthService;
import Insuleng.Insuleng_Backend.src.user.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "회원가입 api", description = "회원가입을 진행합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "3010", description = "이미 존재하는 이메일 입니다"),
            @ApiResponse(responseCode = "3011", description = "이미 존재하는 닉네임 입니다")
    })
    public BaseResponse<String> signUp(@RequestBody @Valid SignUpDto signUpDto){
        try{
            authService.signUp(signUpDto);

            return new BaseResponse<>("회원가입 성공!");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //이메일 중복검사
    @GetMapping("test/email/{user_email}")
    @Operation(summary = "이메일 중복검사 api", description = "기존에 존재하는 이메일이 있는지 검사합니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "3010", description = "이미 존재하는 이메일입니다")
    })
    public BaseResponse<String> checkEmailDuplicate(@Parameter(name = "user_email", description = "유저의 email", in = ParameterIn.PATH)
            @PathVariable("user_email") String email){
        try{
            authService.checkEmailDuplicate(email);

            return new BaseResponse<>("이메일 사용 가능");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //닉네임 중복검사
    @GetMapping("test/nickname/{user_nickname}")
    @Operation(summary = "닉네임 중복검사 api", description = "기존에 존재하는 닉네임이 있는지 검사합니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "3011", description = "이미 존재하는 닉네임입니다")
    })
    public BaseResponse<String> checkNicknameDuplicate(@Parameter(name = "user_nickname", description = "user의 nickname", in = ParameterIn.PATH)
            @PathVariable("user_nickname") String nickname){
        try{
            authService.checkNicknameDuplicate(nickname);

            return new BaseResponse<>("닉네임 사용 가능");

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //이메일 찾기
    @PostMapping("recovery/email")
    @Operation(summary = "이메일 찾기 api", description = "닉네임과 전화번호로 이메일을 찾습니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "3200", description = "존재하지 않은 이메일입니다")
    })
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
    @Operation(summary = "임시 비밀번호 발급 api", description = "해당 이메일로 임시 비밀번호를 발급합니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다")
    })
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
