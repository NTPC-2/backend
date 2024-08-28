package Insuleng.Insuleng_Backend.src.community.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "게시글 작성 dto")
public class PostDto {

    @Schema(description = "제목", nullable = false, example = "첫 번째 게시글")
    @NotBlank (message = "제목을 입력하세요")
    @Size (min = 1, max = 30)
    private String topic;

    @Schema(description = "내용", nullable = false, example = "게시글 내용입니다")
    @NotBlank (message = "내용을 입력하세요")
    @Size
    private String contents;

    @Schema(description = "이미지 파일", nullable = true)
    private MultipartFile imgUrl = null;
}
