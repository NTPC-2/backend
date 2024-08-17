package Insuleng.Insuleng_Backend.src.restaurant.repository;

import Insuleng.Insuleng_Backend.src.restaurant.entity.RestaurantEntity;

import java.util.List;

public interface RestaurantQueryDslRepositoryCustom {

    List<RestaurantEntity> getRestaurantDetails(Long restaurantId);

}
