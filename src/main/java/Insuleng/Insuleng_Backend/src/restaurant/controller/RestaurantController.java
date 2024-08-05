package Insuleng.Insuleng_Backend.src.restaurant.controller;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.src.restaurant.dto.RestaurantListDto;
import Insuleng.Insuleng_Backend.src.restaurant.dto.RestaurantSummaryDto;
import Insuleng.Insuleng_Backend.src.restaurant.service.RestaurantService;
import Insuleng.Insuleng_Backend.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("restaurant/list")
    @Operation(summary = "음식점 리스트 보기 api", description = "카테고리에 맞는 음식접 리스트를 보여줍니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2010", description = "URl의 parameter 값이 잘못되었습니다")
    })
    public BaseResponse<RestaurantListDto> getRestaurantList(@RequestParam(value = "category") Long categoryId ){
        try{
            //categoryId 값에 1~7 이외의 정수가 들어오면 예외처리
            return new BaseResponse<>(restaurantService.getRestaurantList(categoryId));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    @PostMapping("restaurant/addheart/{restaurant_id}")
    public BaseResponse<String> addRestaurantHeart(@PathVariable("restaurant_id") Long restaurantId){
        try{
            Long userId = SecurityUtil.getCurrentUserId();
            restaurantService.addRestaurantHeart(userId, restaurantId);

            return new BaseResponse<>("좋아요를 눌렀습니다");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }



}
