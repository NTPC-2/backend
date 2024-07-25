package Insuleng.Insuleng_Backend.src.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Builder
@AllArgsConstructor
public class EmailDto {

    @NotBlank(message = "이메일을 입력해주세요")
    @Size
    private String email;

}
