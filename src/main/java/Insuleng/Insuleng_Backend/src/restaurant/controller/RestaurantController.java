package Insuleng.Insuleng_Backend.src.restaurant.controller;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.src.restaurant.dto.*;
import Insuleng.Insuleng_Backend.src.restaurant.service.RestaurantService;
import Insuleng.Insuleng_Backend.src.storage.S3Uploader;
import Insuleng.Insuleng_Backend.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Autowired
    private S3Uploader s3Uploader;

    @GetMapping("restaurant/list")
    @Operation(summary = "음식점 리스트 보기 api", description = "카테고리에 맞는 음식접 리스트를 보여줍니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2010", description = "URl의 parameter 값이 잘못되었습니다")
    })
    public BaseResponse<RestaurantListDto> getRestaurantList(@RequestParam(value = "category") Long categoryId ){
        try{
            //categoryId 값에 1~7 이외의 정수가 들어오면 예외처리
            //s3Uploader.deleteImageFromS3("ddd");
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
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다"),
            @ApiResponse(responseCode = "3600", description = "이미 음식점 좋아요가 되어있습니다")
    })
    public BaseResponse<String> addRestaurantHeart(@PathVariable("restaurant_id") Long restaurantId){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            restaurantService.addRestaurantHeart(userId, restaurantId);

            return new BaseResponse<>("좋아요를 눌렀습니다");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("restaurant/removeheart/{restaurant_id}")
    @Operation(summary = "음식점 좋아요 해제 api", description = "해당 유저로 음식점 좋아요 해제합니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않는 유저입니다"),
            @ApiResponse(responseCode = "2006", description = "존재하지 않는 음식점입니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다"),
            @ApiResponse(responseCode = "3601", description = "이미 음식점 좋아요가 해제되어있습니다"),
            @ApiResponse(responseCode = "3605",description = "음식점 좋아요 정보가 없습니다")
    })
    public BaseResponse<String> removeRestaurantHeart(@PathVariable("restaurant_id") Long restaurantId){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            restaurantService.removeRestaurantHeart(userId, restaurantId);

            return new BaseResponse<>("좋아요를 해제했습니다");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("restaurant/addbookmark/{restaurant_id}")
    @Operation(summary = "음식점 즐겨찾기 api", description = "해당 유저로 음식점 즐겨찾기 설정합니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않는 유저입니다"),
            @ApiResponse(responseCode = "2006", description = "존재하지 않는 음식점입니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다"),
            @ApiResponse(responseCode = "3610", description = "이미 음식점 즐겨찾기가 되어있습니다"),
    })
    public BaseResponse<String> addRestaurantBookmark(@PathVariable("restaurant_id") Long restaurantId){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            restaurantService.addRestaurantBookmark(userId, restaurantId);

            return new BaseResponse<>("즐겨찾기를 눌렀습니다");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("restaurant/removebookmark/{restaurant_id}")
    @Operation(summary = "음식점 즐겨찾기 해제 api", description = "해당 유저로 음식점 즐겨찾기 해제합니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않는 유저입니다"),
            @ApiResponse(responseCode = "2006", description = "존재하지 않는 음식점입니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다"),
            @ApiResponse(responseCode = "3611", description = "이미 음식점 즐겨찾기가 해제되어있습니다"),
            @ApiResponse(responseCode = "3615",description = "음식점 즐겨찾기 정보가 없습니다")
    })
    public BaseResponse<String> removeRestaurantBookmark(@PathVariable("restaurant_id") Long restaurantId){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            restaurantService.removeRestaurantBookmark(userId, restaurantId);

            return new BaseResponse<>("즐겨찾기를 해제했습니다");
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
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다")
    })
    public BaseResponse<String> writeReview(@PathVariable("restaurant_id") Long restaurantId, @RequestBody @Valid WriteReviewDto writeReviewDto){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            restaurantService.writeReview(userId, restaurantId, writeReviewDto);

            return new BaseResponse<>("리뷰를 작성했습니다");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //리뷰 수정 하는 form을 제공하는 api
    @GetMapping("restaurant/review/{review_id}")
    @Operation(summary = "리뷰를 수정할 수 있는 form을 제공하는 api", description = "기존 리뷰 정보를 제공합니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않는 유저입니다"),
            @ApiResponse(responseCode = "2006", description = "존재하지 않는 음식점입니다"),
            @ApiResponse(responseCode = "2100", description = "해당 글에 대한 권한이 없습니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다")
    })
    public BaseResponse<ReviewFormDto> getReviewInfo(@PathVariable("review_id") Long reviewId){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            ReviewFormDto reviewFormDto = restaurantService.getReviewInfo(userId, reviewId);

            return new BaseResponse<>(reviewFormDto);
        }
        catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    //JPA의 영속성 컨텍스트, 엔티티 매니저, 1차 캐시 시점, 트랜잭션 커밋 시점 좀 더 공부하기 + 더티 체킹, 벌크연산
    //실제로 review가 수정되는 api
    @Transactional
    @PutMapping("restaurant/review/{review_id}")
    @Operation(summary = "리뷰를 실제로 수정하는 api", description = "updateReviewDto 내용으로 리뷰를  수정합니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않는 유저입니다"),
            @ApiResponse(responseCode = "2006", description = "존재하지 않는 음식점입니다"),
            @ApiResponse(responseCode = "2100", description = "해당 글에 대한 권한이 없습니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다")
    })
    public BaseResponse<String> updateReview(@PathVariable("review_id") Long reviewId, @RequestBody @Valid UpdateReviewDto updateReviewDto){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            restaurantService.updateReview(userId, reviewId, updateReviewDto);

            return new BaseResponse<>("리뷰를 수정했습니다");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    @PatchMapping("restaurant/delete/review/{review_id}")
    @Operation(summary = "리뷰 삭제 api", description = "해당 음식점의 리뷰를 삭제합니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않는 유저입니다"),
            @ApiResponse(responseCode = "2006", description = "존재하지 않는 음식점입니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다")
    })
    public BaseResponse<String> deleteReview(@PathVariable("review_id") Long reviewId){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            restaurantService.deleteReview(userId, reviewId);

            return new BaseResponse<>("리뷰를 삭제했습니다");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    @GetMapping("restaurant/search/list")
    @Operation(summary = "음식점 검색 결과 api", description = "키워드에 해당하는 음식점 리스트를 보여줍니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2010", description = "URl의 parameter 값이 잘못되었습니다")
    })
    public BaseResponse<RestaurantSearchListDto> getRestaurantSearchList(@RequestParam(value = "search") String keyword ){
        try{
            return new BaseResponse<>(restaurantService.getRestaurantSearchList2(keyword));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

   /* @GetMapping("restaurant/{restaurant_id}")
    public BaseResponse<RestaurantDetailsDto> getRestaurantDetails(@PathVariable("restaurant_id")Long restaurantId){
        try {
            Long userId = SecurityUtil.getCurrentUserId().orElse(null);

            //로그인이 안 된 사용자 -> 즐겨찾기/좋아요 불가, 리뷰 보기 불가
            if(userId == null){
                return new BaseResponse<>(restaurantService.getRestaurantDetails(restaurantId));
            }

            //로그인된 사용자 -> 즐겨찾기/좋아요 가능, 리뷰 시청 가능
            return new BaseResponse<>(restaurantService.getRestaurantDetails(userId, restaurantId));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }*/


}
