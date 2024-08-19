package Insuleng.Insuleng_Backend.src.restaurant.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDetailsDto {

    private String userNickname;
    private String restaurantName;
    private String contents;
    private String timeLine;
    private double star;
    private List<String> reviewImgList;

}
