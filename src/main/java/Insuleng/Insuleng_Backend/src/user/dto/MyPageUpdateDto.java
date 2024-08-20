package Insuleng.Insuleng_Backend.src.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Schema(description = "내 정보 수정 Dto")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyPageUpdateDto {

    @Schema(description = "닉네임", nullable = false, example = "눈사람")
    @NotBlank
    @Size(min = 2, max = 6, message = "닉네임은 2자에서 6자 사이로 지어주세요")
    private String nickname;

    @Schema(description = "전화번호", nullable = false, example = "01011112222")
    //NotBlank는 String 타입에만 사용 가능
    @NotNull
    private String phoneNumber;

    @Schema(description = "성별", nullable = false, example = "M", allowableValues = {"M", "W"})
    @NotNull
    private Character gender;

    //NotBlank는 String 타입에만 사용 가능
    @Schema(description = "나이", nullable = false, example = "24")
    @NotNull
    private Integer age;

    @Schema(description = "프로필 이미지, 프로필을 안 넣고 싶다면 null로 처리하기", nullable = true)
    private MultipartFile profileImg;

}
