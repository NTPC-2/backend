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
public class SearchByMenuNameDto {

    private String restaurantName;
    private String mainImg;
    private int countHeart;
    private int countBookmark;
    private int countReview;
    private double averageStar;
    private List<String> mainMenuList;

    public SearchByMenuNameDto(String restaurantName, String mainImg, int countHeart,int countBookmark, int countReview, double averageStar){
        this.restaurantName = restaurantName;
        this.mainImg = mainImg;
        this.countHeart = countHeart;
        this.countBookmark = countBookmark;
        this.countReview = countReview;
        this.averageStar = averageStar;
    }

}
