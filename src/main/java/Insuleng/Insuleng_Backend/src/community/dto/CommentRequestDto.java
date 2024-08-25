package Insuleng.Insuleng_Backend.src.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {

    @NotBlank (message = "내용을 입력하세요")
    @Size
    private String contents;

    private Long parentCommentId; // 대댓글인 경우 사용
}