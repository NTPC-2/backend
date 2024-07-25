package Insuleng.Insuleng_Backend.src.user.service;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.user.dto.EmailDto;
import Insuleng.Insuleng_Backend.src.user.dto.FindEmailDto;
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

    public void signUp(SignUpDto signUpDto) throws BaseException {
        //이메일 중복검사
        if(userRepository.existsUserEntitiesByEmailAndStatus(signUpDto.getEmail(), Status.ACTIVE) == true){
            System.out.println("중복된 이메일 입니다");
            throw new BaseException(BaseResponseStatus.DUPLICATED_EMAIL);

        }
        //닉네임 중복검사
        if(userRepository.existsUserEntitiesByNicknameAndStatus(signUpDto.getNickname(), Status.ACTIVE) == true){
            System.out.println("중복된 닉네입입니다");
            throw new BaseException(BaseResponseStatus.DUPLICATED_NICKNAME);
        }

        //비밀번호 암호화
        String encodePwd = bCryptPasswordEncoder.encode(signUpDto.getPassword());
        UserEntity newUser = new UserEntity(signUpDto, encodePwd);

        userRepository.save(newUser);
    }

    public void checkEmailDuplicate(String email) {

        Boolean bool = userRepository.existsUserEntitiesByEmailAndStatus(email, Status.ACTIVE);

        if(bool == true){
            throw new BaseException(BaseResponseStatus.DUPLICATED_EMAIL);
        }

    }

    public void checkNicknameDuplicate(String nickname) {

        Boolean bool = userRepository.existsUserEntitiesByNicknameAndStatus(nickname, Status.ACTIVE);

        if(bool == true){
            throw new BaseException(BaseResponseStatus.DUPLICATED_NICKNAME);
        }

    }

    public EmailDto findEmail(FindEmailDto findEmailDto) {

        String nickname = findEmailDto.getNickname();
        int phoneNumber = findEmailDto.getPhoneNumber();

        UserEntity userEntity = userRepository.findUserEntityByNicknameAndPhoneNumberAndStatus(
                nickname, phoneNumber, Status.ACTIVE).
                orElseThrow(() -> new BaseException(BaseResponseStatus.EMAIL_NO_EXIST));

        return new EmailDto(userEntity.getEmail());

    }
}
