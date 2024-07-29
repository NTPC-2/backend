package Insuleng.Insuleng_Backend.src.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "이메일 찾기 Dto")
public class FindEmailDto {

    @Schema(description = "닉네임", nullable = false, example = "눈사람")
    @NotBlank(message = "닉네임을 입력해주세요")
    @Size(min = 2, max = 6)
    private String nickname;

    @Schema(description = "전화번호", nullable = false, example = "01011112222")
    @NotNull(message = "전화번호를 입력해주세요")
    private Integer phoneNumber;
}
