package Insuleng.Insuleng_Backend.src.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor

public class PostDto {

    @NotBlank (message = "제목을 입력하세요")
    @Size (min = 1, max = 30)
    private String topic;

    @NotBlank (message = "내용을 입력하세요")
    @Size
    private String contents;

    private String imgUrl;
}
