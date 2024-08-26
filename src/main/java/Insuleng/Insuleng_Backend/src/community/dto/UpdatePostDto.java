package Insuleng.Insuleng_Backend.src.community.dto;

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

    @NotNull
    private Long postId;

    @NotBlank(message = "제목을 입력하세요")
    @Size(min = 1, max = 30)
    private String topic;

    @NotBlank(message = "내용을 입력하세요")
    @Size
    private String contents;

    private MultipartFile imgUrl;

}
