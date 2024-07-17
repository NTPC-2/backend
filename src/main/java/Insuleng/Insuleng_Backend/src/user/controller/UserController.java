package Insuleng.Insuleng_Backend.src.user.controller;

import Insuleng.Insuleng_Backend.src.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;




}
