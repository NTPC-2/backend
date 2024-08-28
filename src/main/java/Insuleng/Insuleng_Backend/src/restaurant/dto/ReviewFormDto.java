package Insuleng.Insuleng_Backend.src.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "리뷰 form Dto(만약 기본 리뷰에 사진이 없으면 reviewImg에 null이 출력된다)")
public class ReviewFormDto {

    private Long reviewId;
    private Double star;
    private String contents;
    List<String> reviewImg;

    public ReviewFormDto(Long reviewId, String contents, Double star){
        this.reviewId = reviewId;
        this.contents = contents;
        this.star = star;
    }
}
