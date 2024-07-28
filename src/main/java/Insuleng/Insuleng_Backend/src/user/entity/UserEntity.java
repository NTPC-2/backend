package Insuleng.Insuleng_Backend.src.user.entity;

import Insuleng.Insuleng_Backend.config.BaseEntity;
import Insuleng.Insuleng_Backend.src.user.dto.SignUpDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
@DynamicInsert
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    //이메일 조건 추가하기
    @Column(nullable = false, length = 50)
    private String email;

    //비밀번호 글자 수 제한
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false, length = 11)
    private Integer phoneNumber;

    //enum 처리
    @Column(nullable = false)
    private Character gender;

    @Column(nullable = false)
    private Integer age;

    //프로필 이미지
    @Column(length = 255)
    private String profileImg;

    //user 권한 ROLE_ADMIN, ROLE_USER로 구분
    @Column
    private String role;

    //Oauth2 구현 시 기능 넣기
//    @Column
//    private Integer loginType;

    //일반 user 회원가입
    public UserEntity(SignUpDto signUpDto, String encodePwd){
        this.email = signUpDto.getEmail();
        this.nickname = signUpDto.getNickname();
        this.phoneNumber = signUpDto.getPhoneNumber();
        this.gender = signUpDto.getGender();
        this.age = signUpDto.getAge();
        this.role = "ROLE_USER";
        this.password = encodePwd;
    }

    //토큰 생성 시 임의의 usetEntity를 만들기 위한 메서드
    public void setEmailAndPwdAndRole(String email, String randomPwd ,String role){
        this.email = email;
        this.password = randomPwd;
        this.role = role;
    }

    //비밀번호 변경을 위한 메서드
    public void setPassword(String password){
        this.password = password;
    }

}
