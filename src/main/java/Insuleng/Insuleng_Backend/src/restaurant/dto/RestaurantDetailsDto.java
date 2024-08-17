package Insuleng.Insuleng_Backend.src.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDetailsDto {

    private Boolean isLogin;
    private String restaurantName;
    private String details;
    private int count_bookmark;
    private int count_heart;
    private int count_review;
    private double averageStar;

    private Map<String, Integer> menuMap;
    private String address;
    private String phoneNumber;

    //limit으로 리뷰 개수 제한하기
    private List<ReviewDetailsDto> reviewDetailsDtoList;

    public void updateInfo(String restaurantName, String details, int count_bookmark, int count_heart, int count_review, double averageStar, String address, String phoneNumber){
        this.restaurantName = restaurantName;
        this.details = details;
        this.count_bookmark = count_bookmark;
        this.count_heart = count_heart;
        this.count_review = count_review;
        this.averageStar = averageStar;
        this.address = address;
        this.phoneNumber = phoneNumber;

    }



}
