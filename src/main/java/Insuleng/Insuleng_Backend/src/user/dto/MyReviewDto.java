package Insuleng.Insuleng_Backend.src.user.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyReviewDto {
    private Long reviewId;
    private Long restaurantId;
    private String restaurantName;
    private Double star;
    private String contents;
    private String timeLine;
    private String userNickname;

    private List<String> imgList;
}
