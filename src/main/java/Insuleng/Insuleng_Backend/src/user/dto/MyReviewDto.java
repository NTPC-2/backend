package Insuleng.Insuleng_Backend.src.user.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyReviewDto {

    private String restaurantName;
    private Double star;
    private String contents;
    private String firstImg;
    private String timeLine;
    private String userNickname;

}
