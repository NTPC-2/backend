package Insuleng.Insuleng_Backend.src.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "내 정보 Dto")
@Builder
public class MyPageInfoDto {

    @Schema(description = "이메일", nullable = false, example = "cos123@naver.com")
    @NotBlank
    @Size
    private String email;

    @Schema(description = "닉네임", nullable = false, example = "눈사람")
    @NotBlank
    @Size(min = 2, max = 6)
    private String nickname;

    @Schema(description = "전화번호", nullable = false, example = "01011112222")
    //NotBlank는 String 타입에만 사용 가능
    @NotNull
    private Integer phoneNumber;

    @Schema(description = "성별", nullable = false, example = "M", allowableValues = {"M", "W"})
    @NotNull
    private Character gender;

    //NotBlank는 String 타입에만 사용 가능
    @Schema(description = "나이", nullable = false, example = "24")
    @NotNull
    private Integer age;

    @Schema(description = "프로필 이미지", nullable = true)
    private String profileImg;





}
