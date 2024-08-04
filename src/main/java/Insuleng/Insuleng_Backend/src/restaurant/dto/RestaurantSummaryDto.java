package Insuleng.Insuleng_Backend.src.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantSummaryDto {

    private String restaurantName;
    private String mainImg;
    private int countHeart;
    private int countBookmark;
    private int countReview;
    private List<String> mainMenuList;


}
