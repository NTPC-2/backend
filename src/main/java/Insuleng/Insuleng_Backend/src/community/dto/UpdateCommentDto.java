package Insuleng.Insuleng_Backend.src.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentDto {

    @NotBlank(message = "내용을 입력하세요")
    @Size
    private String contents;
}
