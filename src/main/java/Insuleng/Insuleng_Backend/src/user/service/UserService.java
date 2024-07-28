package Insuleng.Insuleng_Backend.src.user.service;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import Insuleng.Insuleng_Backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //임시 비밀번호 발급 후 일정시간이 지나면 삭제
    public void setTemporalPwd(String email, String tempPwd){

        //이메일 유효성 검사
        UserEntity userEntity = userRepository.findUserEntityByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));
        //tempPwd를 암호화 해서 DB에 저장
        String encodedPwd = bCryptPasswordEncoder.encode(tempPwd);
        userEntity.setPassword(encodedPwd);

        userRepository.save(userEntity);
        //추후 구현 스케줄러를 이용해 DB 데이터 변경


    }





}
