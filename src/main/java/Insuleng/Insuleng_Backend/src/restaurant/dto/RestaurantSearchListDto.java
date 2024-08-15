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
public class RestaurantSearchListDto {

    private int countRestaurant;
    private List<RestaurantSummaryInterface> restaurantSummaryDtoList;
}
