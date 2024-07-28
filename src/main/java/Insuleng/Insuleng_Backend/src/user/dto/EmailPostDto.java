package Insuleng.Insuleng_Backend.src.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailPostDto {

    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

}
