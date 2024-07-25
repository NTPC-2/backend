package Insuleng.Insuleng_Backend.src.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindEmailDto {

    @NotBlank(message = "닉네임을 입력해주세요")
    @Size(min = 2, max = 6)
    private String nickname;

    @NotNull(message = "전화번호를 입력해주세요")
    private Integer phoneNumber;
}
