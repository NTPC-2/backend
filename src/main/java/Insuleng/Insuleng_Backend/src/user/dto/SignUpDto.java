package Insuleng.Insuleng_Backend.src.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDto {

    @NotBlank(message = "이메일을 입력해주세요")
    @Size
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 4, max = 12)
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요")
    @Size(min = 2, max = 6)
    private String nickname;

    //NotBlank는 String 타입에만 사용 가능
    @NotNull(message = "전화번호를 입력해주세요")
    private Integer phoneNumber;

    @NotNull(message = "성별을 골라주세요")
    private Character gender;

    //NotBlank는 String 타입에만 사용 가능
    @NotNull(message = "나이를 입력해주세요")
    private Integer age;

    private String profileImg;

}
