package Insuleng.Insuleng_Backend.src.user.controller;


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
    //Response 객체가 만들어지기 전까지 String을 사용해서 return
    @PostMapping("/signup")
    public String signUp(@RequestBody @Valid SignUpDto signUpDto){
        authService.signUp(signUpDto);

        return "회원가입";
    }


}
