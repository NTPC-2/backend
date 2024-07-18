package Insuleng.Insuleng_Backend.src.user.entity;

import Insuleng.Insuleng_Backend.config.BaseEntity;
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
    @Column(nullable = false, length = 20)
    private String password;

    @Column(nullable = false)
    private String username;

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

}
