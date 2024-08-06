package Insuleng.Insuleng_Backend.src.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "리뷰 작성 dto")
public class WriteReviewDto {

    @Schema(description = "평점", nullable = false, example = "4.5")
    @NotNull(message = "평점을 입력해주세요")
    private Double star;

    @Schema(description = "리뷰 내용", nullable = false, example = "숨은 맛집입니다!")
    @NotBlank(message = "내용을 적어주세요")
    private String contents;

    @Schema(description = "이미지", nullable = true, example = "Amigo.jpg")
    List<String> reviewImg;


}
