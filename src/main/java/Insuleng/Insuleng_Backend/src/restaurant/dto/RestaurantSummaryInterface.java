package Insuleng.Insuleng_Backend.src.restaurant.dto;

public interface RestaurantSummaryInterface {


    Long getRestaurantId();
    String getRestaurantName();
    String getMainImg();

    Integer getCountHeart();
    Integer getCountBookmark();
    Integer getCountReview();
    Double getAverageStar();
    String getMenuNames();
}
