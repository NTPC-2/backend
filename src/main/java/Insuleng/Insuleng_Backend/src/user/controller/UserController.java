package Insuleng.Insuleng_Backend.src.user.controller;

import Insuleng.Insuleng_Backend.src.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping("/hello")
    public String hello(){
        return "안녕하세요";
    }
    @GetMapping("/user")
    public String user(){
        return "user 페이지 입니다";
    }
    @GetMapping("/admin")
    public String admin(){
        return "admin 페이지 입니다.";
    }


}
