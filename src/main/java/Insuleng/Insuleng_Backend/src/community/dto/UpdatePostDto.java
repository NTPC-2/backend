package Insuleng.Insuleng_Backend.src.community.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class UpdatePostDto {

    @Schema(description = "게시글 아이디", nullable = false)
    @NotNull
    private Long postId;

    @Schema(description = "제목", nullable = false)
    @NotBlank(message = "제목을 입력하세요")
    @Size(min = 1, max = 30)
    private String topic;

    @Schema(description = "내용", nullable = false)
    @NotBlank(message = "내용을 입력하세요")
    @Size
    private String contents;

    @Schema(description = "사진 파일", nullable = true)
    private MultipartFile imgUrl = null;

}
