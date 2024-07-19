package Insuleng.Insuleng_Backend.src.user.service;

import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.user.dto.SignUpDto;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import Insuleng.Insuleng_Backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signUp(SignUpDto signUpDto) {
        //이메일 중복검사
        if(userRepository.existsUserEntitiesByEmailAndStatus(signUpDto.getEmail(), Status.ACTIVE) == true){
            System.out.println("중복된 이메일 입니다");
            return;
        }
        //닉네임 중복검사
        if(userRepository.existsUserEntitiesByNicknameAndStatus(signUpDto.getNickname(), Status.ACTIVE) == true){
            System.out.println("중복된 닉네입입니다");
            return;
        }

        String encodePwd = bCryptPasswordEncoder.encode(signUpDto.getPassword());
        UserEntity newUser = new UserEntity(signUpDto, encodePwd);

        userRepository.save(newUser);
    }
}
