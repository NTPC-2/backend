package Insuleng.Insuleng_Backend.src.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "회원가입 요청 DTO")
public class SignUpDto {

    @Schema(description = "이메일", nullable = false, example = "cos123@naver.com")
    @NotBlank(message = "이메일을 입력해주세요")
    @Size
    private String email;

    @Schema(description = "비밀번호", nullable = false, example = "sndlmdl!")
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 4, max = 12)
    private String password;

    @Schema(description = "닉네임", nullable = false, example = "눈사람")
    @NotBlank(message = "닉네임을 입력해주세요")
    @Size(min = 2, max = 6)
    private String nickname;

    @Schema(description = "전화번호", nullable = false, example = "01011112222")
    //NotBlank는 String 타입에만 사용 가능
    @NotNull(message = "전화번호를 입력해주세요")
    private Integer phoneNumber;

    @Schema(description = "성별", nullable = false, example = "M", allowableValues = {"M", "W"})
    @NotNull(message = "성별을 골라주세요")
    private Character gender;

    //NotBlank는 String 타입에만 사용 가능
    @Schema(description = "나이", nullable = false, example = "24")
    @NotNull(message = "나이를 입력해주세요")
    private Integer age;

    @Schema(description = "프로필 이미지", nullable = true)
    private String profileImg;

}
