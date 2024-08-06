package Insuleng.Insuleng_Backend.src.restaurant.service;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.restaurant.dto.RestaurantListDto;
import Insuleng.Insuleng_Backend.src.restaurant.dto.RestaurantSummaryDto;
import Insuleng.Insuleng_Backend.src.restaurant.dto.UpdateReviewDto;
import Insuleng.Insuleng_Backend.src.restaurant.dto.WriteReviewDto;
import Insuleng.Insuleng_Backend.src.restaurant.entity.HeartEntity;
import Insuleng.Insuleng_Backend.src.restaurant.entity.RestaurantEntity;
import Insuleng.Insuleng_Backend.src.restaurant.entity.ReviewEntity;
import Insuleng.Insuleng_Backend.src.restaurant.entity.ReviewImgEntity;
import Insuleng.Insuleng_Backend.src.restaurant.repository.HeartRepository;
import Insuleng.Insuleng_Backend.src.restaurant.repository.RestaurantRepository;
import Insuleng.Insuleng_Backend.src.restaurant.repository.ReviewImgRepository;
import Insuleng.Insuleng_Backend.src.restaurant.repository.ReviewRepository;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import Insuleng.Insuleng_Backend.src.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final HeartRepository heartRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;

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

    public void addRestaurantHeart(Long userId, Long restaurantId) {

        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        RestaurantEntity restaurant = restaurantRepository.findRestaurantEntityByRestaurantIdAndStatus(restaurantId, Status.ACTIVE)
                .orElseThrow(()->new BaseException(BaseResponseStatus.RESTAURANT_NO_EXIST));

        HeartEntity heart = heartRepository.findHeartEntityByUserEntityAndAndRestaurantEntity(user,restaurant)
                .orElse(null);

        if(heart == null){
            //heart 테이블에 새로 하트 정보 추가
            HeartEntity newHeart = new HeartEntity(user, restaurant);
            heartRepository.save(newHeart);
            //레스토랑 전체 하트 수 증가
            restaurant.increaseCountHeart();
            restaurantRepository.save(restaurant); //값을 업데이트 할 때도 save 사용
        }else{
            if(heart.getStatus() == Status.ACTIVE){
                throw new BaseException(BaseResponseStatus.ALREADY_RESTAURANT_HEART);
            }else if(heart.getStatus() == Status.INACTIVE){
                //heart 테이블에 status를 active로 변경
                heart.changeToActive();
                heartRepository.save(heart);
                //레스토랑 전체 하트 수 증가
                restaurant.increaseCountHeart();
                restaurantRepository.save(restaurant);
            }
        }

    }

    public void removeRestaurantHeart(Long userId, Long restaurantId) {
        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        RestaurantEntity restaurant = restaurantRepository.findRestaurantEntityByRestaurantIdAndStatus(restaurantId, Status.ACTIVE)
                .orElseThrow(()->new BaseException(BaseResponseStatus.RESTAURANT_NO_EXIST));

        HeartEntity heart = heartRepository.findHeartEntityByUserEntityAndAndRestaurantEntity(user,restaurant)
                .orElse(null);

        if(heart == null){
            throw new BaseException(BaseResponseStatus.HEART_NO_EXIST);
        }else{
            if(heart.getStatus() == Status.ACTIVE){
                //heart 테이블에 status를 inactive로 변경
                heart.changeToInActive();
                heartRepository.save(heart);

                //레스토랑 전체 하트 수 감소
                restaurant.decreaseCountHeart();
                restaurantRepository.save(restaurant);

            }else if(heart.getStatus() == Status.INACTIVE){
                throw new BaseException(BaseResponseStatus.ALREADY_RESTAURANT_NO_HEART);
            }
        }

    }

    public void writeReview(Long userId, Long restaurantId ,WriteReviewDto writeReviewDto) {
        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        RestaurantEntity restaurant = restaurantRepository.findRestaurantEntityByRestaurantIdAndStatus(restaurantId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.RESTAURANT_NO_EXIST));

        ReviewEntity reviewEntity = new ReviewEntity(writeReviewDto.getContents(), writeReviewDto.getStar(), user, restaurant);
        reviewRepository.save(reviewEntity);

        if(writeReviewDto.getReviewImg() != null){
            for(int i =0; i<writeReviewDto.getReviewImg().size(); i++){
                String imgUrl = writeReviewDto.getReviewImg().get(i);

                ReviewImgEntity reviewImgEntity = new ReviewImgEntity(imgUrl, reviewEntity);
                reviewImgRepository.save(reviewImgEntity);
            }
        }

    }

    public void updateReview(Long userId, Long reviewId, UpdateReviewDto updateReviewDto) {
        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        ReviewEntity review = reviewRepository.findReviewEntityByReviewIdAndStatus(reviewId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.REVIEW_NO_EXIST));

        if(review.getUserEntity() != user){
            throw new BaseException(BaseResponseStatus.NO_PRIVILEGE);
        }

        reviewImgRepository.updateStatusOfRestaurantImgEntities(Status.INACTIVE, review);

        if(updateReviewDto.getReviewImg() != null){
            for(int i =0; i<updateReviewDto.getReviewImg().size(); i++){
                String imgUrl = updateReviewDto.getReviewImg().get(i);

                ReviewImgEntity reviewImgEntity = new ReviewImgEntity(imgUrl, review);
                reviewImgRepository.save(reviewImgEntity);
            }
        }

        review.updateReview(updateReviewDto.getContents(), updateReviewDto.getStar());
        reviewRepository.save(review);
    }
}
