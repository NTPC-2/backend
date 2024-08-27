package Insuleng.Insuleng_Backend.src.community.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class DeletePostDto {

    @Schema(description = "게시글 아이디")
    @NotNull
    private Long postId;

}
