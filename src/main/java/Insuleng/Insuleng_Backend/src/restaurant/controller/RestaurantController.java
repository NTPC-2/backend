package Insuleng.Insuleng_Backend.src.restaurant.controller;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.src.restaurant.dto.*;
import Insuleng.Insuleng_Backend.src.restaurant.service.RestaurantService;
import Insuleng.Insuleng_Backend.utils.SecurityUtil;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
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
    @Operation(summary = "음식점 좋아요 api", description = "해당 유저로 음식점 좋아요 설정합니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않는 유저입니다"),
            @ApiResponse(responseCode = "2006", description = "존재하지 않는 음식점입니다"),
            @ApiResponse(responseCode = "3600", description = "이미 음식점 좋아요가 되어있습니다"),
    })
    public BaseResponse<String> addRestaurantHeart(@PathVariable("restaurant_id") Long restaurantId){
        try{
            Long userId = SecurityUtil.getCurrentUserId();
            restaurantService.addRestaurantHeart(userId, restaurantId);

            return new BaseResponse<>("좋아요를 눌렀습니다");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("restaurant/removeheart/{restaurant_id}")
    @Operation(summary = "음식점 좋아요 api", description = "해당 유저로 음식점 좋아요 설정합니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않는 유저입니다"),
            @ApiResponse(responseCode = "2006", description = "존재하지 않는 음식점입니다"),
            @ApiResponse(responseCode = "3601", description = "이미 음식점 좋아요가 해제되어있습니다"),
            @ApiResponse(responseCode = "3610",description = "음식점 좋아요 정보가 없습니다")
    })
    public BaseResponse<String> removeRestaurantHeart(@PathVariable("restaurant_id") Long restaurantId){
        try{
            Long userId = SecurityUtil.getCurrentUserId();
            restaurantService.removeRestaurantHeart(userId, restaurantId);

            return new BaseResponse<>("좋아요를 해제했습니다");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("restaurant/{restaurant_id}/review")
    @Operation(summary = "리뷰 작성 api", description = "해당 음식점의 리뷰를 작성합니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않는 유저입니다"),
            @ApiResponse(responseCode = "2006", description = "존재하지 않는 음식점입니다"),
    })
    public BaseResponse<String> writeReview(@PathVariable("restaurant_id") Long restaurantId, @RequestBody @Valid WriteReviewDto writeReviewDto){
        try{
            Long userId = SecurityUtil.getCurrentUserId();
            restaurantService.writeReview(userId, restaurantId, writeReviewDto);

            return new BaseResponse<>("리뷰를 작성했습니다");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("restaurant/review/{review_id}")
    public BaseResponse<ReviewFormDto> getReviewInfo(@PathVariable("review_id") Long reviewId){
        try{
            Long userId = SecurityUtil.getCurrentUserId();
            ReviewFormDto reviewFormDto = restaurantService.getReviewInfo(userId, reviewId);

            return new BaseResponse<>(reviewFormDto);
        }
        catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    //JPA의 영속성 컨텍스트, 엔티티 매니저, 1차 캐시 시점, 트랜잭션 커밋 시점 좀 더 공부하기 + 더티 체킹, 벌크연산
    @Transactional
    @PutMapping("restaurant/review/{review_id}")
    public BaseResponse<String> updateReview(@PathVariable("review_id") Long reviewId, @RequestBody @Valid UpdateReviewDto updateReviewDto){
        try{
            Long userId = SecurityUtil.getCurrentUserId();
            restaurantService.updateReview(userId, reviewId, updateReviewDto);

            return new BaseResponse<>("리뷰를 수정했습니다");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }
}
