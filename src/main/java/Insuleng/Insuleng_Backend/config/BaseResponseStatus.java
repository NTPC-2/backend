package Insuleng.Insuleng_Backend.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseResponseStatus {

    //요청 성공
    Success(true, 200, "ok"),


    //common
    INVALID_INPUT(false, 2000, "입력한 값이 잘못됐습니다"),
    USER_NO_EXIST(false, 2005, "존재하지 않은 유저입니다"),


    //회원가입
    USER_EMAIL_EMPTY(false, 3000, "이메일을 입력해주세요"),
    USER_PASSWORD_EMPTY(false, 3001, "비밀번호를 입력해주세요"),
    USER_NICKNAME_EMPTY(false, 3002, "닉네임을 입력해주세요"),
    USER_PHONE_NUMBER_EMPTY(false, 3003, "핸드폰번호를 입력해주세요"),
    USER_GENDER_EMPTY(false, 3004, "성별을 선택해주세요"),
    USER_AGE_EMPTY(false, 3005, "나이를 입력해주세요"),


    DUPLICATED_EMAIL(false, 3100, "이미 존재하는 이메일입니다"),
    DUPLICATED_NICKNAME(false, 3105, "이미 존재하는 닉네임입니다");


    private final boolean isSuccess;
    private final int code;
    private final String message;

}
