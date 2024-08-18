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

    private Long restaurantId;
    private String restaurantName;
    private String mainImg;
    private int countHeart;
    private int countBookmark;
    private int countReview;
    private double averageStar;
    private String menuNames;
    private List<String> mainMenuList;

    public RestaurantSummaryDto(String restaurantName, String mainImg, int countHeart,int countBookmark, int countReview, double averageStar){
        this.restaurantId = null;
        this.restaurantName = restaurantName;
        this.mainImg = mainImg;
        this.countHeart = countHeart;
        this.countBookmark = countBookmark;
        this.countReview = countReview;
        this.averageStar = averageStar;
        mainMenuList = null;
    }

    public RestaurantSummaryDto(Long restaurantId, String restaurantName, String mainImg, int countHeart,int countBookmark, int countReview, double averageStar, String menuNames){
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.mainImg = mainImg;
        this.countHeart = countHeart;
        this.countBookmark = countBookmark;
        this.countReview = countReview;
        this.averageStar = averageStar;
        this.mainMenuList = null;
        this.menuNames = menuNames;
    }

}
