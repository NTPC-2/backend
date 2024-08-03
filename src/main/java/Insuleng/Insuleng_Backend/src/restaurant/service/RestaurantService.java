package Insuleng.Insuleng_Backend.src.restaurant.service;

import Insuleng.Insuleng_Backend.src.restaurant.dto.RestaurantSummaryDto;
import Insuleng.Insuleng_Backend.src.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    /*public List<RestaurantSummaryDto> getRestaurantList(int categoryId) {



    }*/
}
