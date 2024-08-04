package Insuleng.Insuleng_Backend.src.restaurant.service;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.restaurant.dto.RestaurantListDto;
import Insuleng.Insuleng_Backend.src.restaurant.dto.RestaurantSummaryDto;
import Insuleng.Insuleng_Backend.src.restaurant.entity.RestaurantEntity;
import Insuleng.Insuleng_Backend.src.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantListDto getRestaurantList(Long categoryId) {
        if(categoryId >7 || categoryId <0){
            throw new BaseException(BaseResponseStatus.INVALID_PARAMETER);
        }

        RestaurantListDto restaurantListDto = new RestaurantListDto();
        int countRestaurant; //restaurantListDto에 들어갈 총 음식점 숫자
        List<RestaurantSummaryDto> restaurantSummaryDtoList = new ArrayList<>(); //restaurantListDto에 들어갈 음식점 목록
        List<RestaurantEntity> restaurantEntityList = new ArrayList<>(); // repository에서 가져올 entity 목록

        if(categoryId == 0){
            countRestaurant = restaurantRepository.countTotal();
            restaurantEntityList = restaurantRepository.findByStatus(Status.ACTIVE);
        }
        else{
            countRestaurant = restaurantRepository.countRestaurant(categoryId);
            restaurantEntityList = restaurantRepository.findRestaurantByCategoryId(categoryId);
        }

        for(int i =0; i< restaurantEntityList.size(); i++){
            RestaurantSummaryDto restaurantSummaryDto = new RestaurantSummaryDto
                    (
                            restaurantEntityList.get(i).getName(),
                            restaurantEntityList.get(i).getMainImg(),
                            restaurantEntityList.get(i).getCountHeart(),
                            restaurantEntityList.get(i).getCountBookmark(),
                            restaurantEntityList.get(i).getCountReview(),
                            new ArrayList<>() // 임시 메인메뉴 리스트
                    );
            restaurantSummaryDtoList.add(restaurantSummaryDto);
        }

        restaurantListDto.setCountRestaurant(countRestaurant); // 음식점 숫자 set
        restaurantListDto.setRestaurantSummaryDtoList(restaurantSummaryDtoList);

        return restaurantListDto;
    }
}
