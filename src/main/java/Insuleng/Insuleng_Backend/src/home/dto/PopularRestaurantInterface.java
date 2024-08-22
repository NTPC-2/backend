package Insuleng.Insuleng_Backend.src.home.dto;

public interface PopularRestaurantInterface {

    Long getRestaurantId();
    String getRestaurantName();
    String getMainImg();

    Integer getCountHeart();
    Integer getCountBookmark();
    Integer getCountReview();
    Double getAverageStar();
    String getMenuNames();

}
