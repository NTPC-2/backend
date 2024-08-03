package Insuleng.Insuleng_Backend.src.restaurant.controller;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.src.restaurant.dto.RestaurantSummaryDto;
import Insuleng.Insuleng_Backend.src.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    /*@GetMapping("restaurant/list")
    public BaseResponse<List<RestaurantSummaryDto>> getRestaurantList(@RequestParam(value = "category") int categoryId ){
        try{
            //categoryId 값에 1~7 이외의 정수가 들어오면 예외처리
            //return new BaseResponse<>(restaurantService.getRestaurantList(categoryId));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }*/


}
