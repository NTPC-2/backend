package Insuleng.Insuleng_Backend.src.user.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyBookmarkDto {
    private Long restaurantId;
    private String restaurantName;
    private String mainImg;
    private int countHeart;
    private int countBookmark;
    private int countReview;
    private List<String> mainMenuList;


}
