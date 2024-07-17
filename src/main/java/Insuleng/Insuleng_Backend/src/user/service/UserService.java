package Insuleng.Insuleng_Backend.src.user.service;

import Insuleng.Insuleng_Backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;


}
