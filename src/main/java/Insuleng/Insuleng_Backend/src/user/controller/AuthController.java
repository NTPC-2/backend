package Insuleng.Insuleng_Backend.src.user.controller;


import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.src.user.dto.SignUpDto;
import Insuleng.Insuleng_Backend.src.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
