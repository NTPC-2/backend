package Insuleng.Insuleng_Backend.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseResponseStatus {

    //요청 성공
    Success(true, 200, "ok"),

    //exception Handler
    UNEXPECTED_ERROR(false, 1000, "예상치 못한 오류"),
    VALIDATION_ERROR(false, 1100, "validation 오류"),

    //common
    INVALID_INPUT(false, 2000, "입출력 오류입니다"),
    USER_NO_EXIST(false, 2005, "존재하지 않은 유저입니다"),
    RESTAURANT_NO_EXIST(false, 2006, "존재하지 않은 음식점입니다"),
    REVIEW_NO_EXIST(false, 2007, "존재하지 않은 리뷰입니다"),
    INVALID_PARAMETER(false, 2010, "URl의 parameter 값이 잘못되었습니다"),

    NO_PRIVILEGE(false, 2100, "해당 글에 대한 권한이 없습니다"),


    NO_IMAGE_EXIST(false, 2200, "이미지가 존재하지 않습니다"),
    NO_FILE_EXTENSION(false, 2210,"파일 확장자가 없습니다"),
    INVALID_FILE_EXTENSION(false, 2211, "이미지 파일이 아닙니다"),
    PUT_OBJECT_EXCEPTION(false, 2215, "PutObjectRequest 오류입니다"),

    //token관련 오류
    NO_ACCESS_TOEKN(false, 2300, "access token이 존재하지 않습니다"),
    NO_REFRESH_TOKEN(false, 2301, "refresh token이 존재하지 않습니다"),
    INVALID_ACCESS_TOKEN(false,2310, "access token 값이 올바르지 않습니다"),
    INVALID_REFRESH_TOKEN(false, 2311, "refresh token 값이 올바르지 않습니다"),
    EXPIRED_ACCESS_TOKEN(false, 2350, "access token이 만료되었습니다"),
    EXPIRED_REFRESH_TOKEN(false,2351, "refresh token이 만료되었습니다"),
    REQUIRED_LOGIN(false, 2360, "로그인이 필요한 서비스입니다"),

    ALREADY_LOGOUT(false, 2320, "이미 로그아웃 됐습니다"),

    //회원가입
    USER_EMAIL_EMPTY(false, 3000, "이메일을 입력해주세요"),
    USER_PASSWORD_EMPTY(false, 3001, "비밀번호를 입력해주세요"),
    USER_NICKNAME_EMPTY(false, 3002, "닉네임을 입력해주세요"),
    USER_PHONE_NUMBER_EMPTY(false, 3003, "핸드폰번호를 입력해주세요"),
    USER_GENDER_EMPTY(false, 3004, "성별을 선택해주세요"),
    USER_AGE_EMPTY(false, 3005, "나이를 입력해주세요"),
    DUPLICATED_EMAIL(false, 3010, "이미 존재하는 이메일입니다"),
    DUPLICATED_NICKNAME(false, 3011, "이미 존재하는 닉네임입니다"),

    //로그인
    FAILED_LOGIN(false, 3100, "아이디나 비밀번호가 올바르지 않습니다"),
    //아이디 찾기
    EMAIL_NO_EXIST(false, 3200, "존재하지 않은 이메일입니다."),
    //비밀번호 찾기(임시 비밀번호 발급)
    FAIL_EMAIL_SEND(false, 3205, "메일로 임시 비밀번호를 보내는데 실패했습니다"),


    //음식점 좋아요
    ALREADY_RESTAURANT_HEART(false, 3600, "이미 음식점 좋아요가 되어있습니다"),
    ALREADY_RESTAURANT_NO_HEART(false, 3601, "이미 음식점 좋아요가 해제되어 있습니다"),
    HEART_NO_EXIST(false, 3605, "음식점 좋아요 정보가 없습니다"),
    //음식점 즐겨찾기
    ALREADY_RESTAURANT_BOOKMARK(false, 3610, "이미 음식점 즐겨찾기가 되어있습니다"),
    ALREADY_RESTAURANT_NO_BOOKMARK(false, 3611, "이미 음식점 좋아요가 해제되어 있습니다"),
    BOOKMARK_NO_EXIST(false, 3615, "음식점 즐겨찾기 정보가 없습니다"),


    POST_EMPTY(false, 4001, "게시글이 존재하지 않습니다"),
    COMMENT_EMPTY(false, 4002, "댓글이 존재하지 않습니다"),
    INVALID_USER(false, 4003, "유효하지 않은 사용자입니다"),
    KEYWORD_EMPTY(false,4004,"검색어가 존재하지 않습니다"),

    ALREADY_POST_LIKE(false, 4005, "이미 게시글 좋아요가 되어있습니다"),
    ALREADY_POST_NO_LIKE(false, 4006, "이미 게시글 좋아요가 해제되어 있습니다"),
    ALREADY_COMMENT_LIKE(false, 4007, "이미 댓글 좋아요가 되어있습니다"),
    ALREADY_COMMENT_NO_LIKE(false, 4008, "이미 댓글 좋아요가 해제되어 있습니다"),
    LIKE_NO_EXIST(false, 4009, "좋아요 정보가 없습니다"),
    ALREADY_POST_SCRAP(false, 4010, "이미 게시글 스크랩이 되어있습니다"),
    ALREADY_POST_NO_SCRAP(false, 4011, "이미 게시글 스크랩이 해제되어 있습니다"),

    NO_PRIVILEGE_COMMENT(false, 4101, "댓글 작성자가 아니여서 글을 수정할 수 없습니다");

    private final boolean isSuccess;
    private final int code;
    private final String message;

}
