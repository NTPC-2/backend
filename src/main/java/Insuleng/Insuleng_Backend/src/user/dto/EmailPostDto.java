package Insuleng.Insuleng_Backend.src.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "임시 비밀번호를 받을 수 있는 이메일 dto")
public class EmailPostDto {

    @Schema(description = "이메일", nullable = false, example = "cos123@naver.com")
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

}
