package Insuleng.Insuleng_Backend.src.restaurant.service;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.restaurant.dto.*;
import Insuleng.Insuleng_Backend.src.restaurant.entity.*;
import Insuleng.Insuleng_Backend.src.restaurant.repository.*;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import Insuleng.Insuleng_Backend.src.user.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    private final BookmarkRepository bookmarkRepository;
    private final MenuRepository menuRepository;

    /*public RestaurantListDto getRestaurantList(Long categoryId) {
        if(categoryId >7 || categoryId <0){
            throw new BaseException(BaseResponseStatus.INVALID_PARAMETER);
        }

        RestaurantListDto restaurantListDto = new RestaurantListDto();
        int countRestaurant; //restaurantListDto에 들어갈 총 음식점 숫자
        List<RestaurantSummaryDto> restaurantSummaryDtoList = new ArrayList<>(); //restaurantListDto에 들어갈 음식점 목록
        List<RestaurantEntity> restaurantEntityList; // repository에서 가져올 entity 목록

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
                            restaurantEntityList.get(i).getRestaurantId(),
                            restaurantEntityList.get(i).getName(),
                            restaurantEntityList.get(i).getMainImg(),
                            restaurantEntityList.get(i).getCountHeart(),
                            restaurantEntityList.get(i).getCountBookmark(),
                            restaurantEntityList.get(i).getCountReview(),
                            restaurantEntityList.get(i).getAverageStar(),
                            null // 임시 메인메뉴 리스트
                    );
            restaurantSummaryDtoList.add(restaurantSummaryDto);
        }

        restaurantListDto.setCountRestaurant(countRestaurant); // 음식점 숫자 set
        restaurantListDto.setRestaurantSummaryDtoList(restaurantSummaryDtoList);

        return restaurantListDto;
    }*/

    public RestaurantListDto getRestaurantList2(Long categoryId) {
        if(categoryId >7 || categoryId <0){
            throw new BaseException(BaseResponseStatus.INVALID_PARAMETER);
        }

        RestaurantListDto restaurantListDto = new RestaurantListDto();
        List<RestaurantSummaryInterface> restaurantSummaryDtoList = new ArrayList<>(); //restaurantListDto에 들어갈 음식점 목록

        if(categoryId == 0){
            restaurantSummaryDtoList = restaurantRepository.findRestaurantListOrderByAverageStar();

            restaurantListDto.setCountRestaurant(restaurantSummaryDtoList.size()); // 목록에 뜨는 총 음식점 숫자
            restaurantListDto.setRestaurantSummaryDtoList(restaurantSummaryDtoList);
        }
        else{
            restaurantSummaryDtoList = restaurantRepository.findRestaurantListOrderByAverageStar(categoryId);

            restaurantListDto.setCountRestaurant(restaurantSummaryDtoList.size()); // 목록에 뜨는 총 음식점 숫자
            restaurantListDto.setRestaurantSummaryDtoList(restaurantSummaryDtoList);
        }

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

    public void addRestaurantBookmark(Long userId, Long restaurantId) {
        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        RestaurantEntity restaurant = restaurantRepository.findRestaurantEntityByRestaurantIdAndStatus(restaurantId, Status.ACTIVE)
                .orElseThrow(()->new BaseException(BaseResponseStatus.RESTAURANT_NO_EXIST));

        BookmarkEntity bookmark = bookmarkRepository.findBookmarkEntityByUserEntityAndRestaurantEntity(user, restaurant)
                .orElse(null);

        if(bookmark == null){
            //bookmark 테이블에 새로 즐겨찾기 정보 추가
            BookmarkEntity newBookmark = new BookmarkEntity(user, restaurant);
            bookmarkRepository.save(newBookmark);
            //레스토랑 전체 즐겨찾기 수 증가
            restaurant.increaseCountBookmark();
            restaurantRepository.save(restaurant); //값을 업데이트 할 때도 save 사용
        }else{
            if(bookmark.getStatus() == Status.ACTIVE){
                throw new BaseException(BaseResponseStatus.ALREADY_RESTAURANT_BOOKMARK);
            }else if(bookmark.getStatus() == Status.INACTIVE){
                //bookmark 테이블에 status를 active로 변경
                bookmark.changeToActive();
                bookmarkRepository.save(bookmark);
                //레스토랑 전체 즐겨찾기 수 증가
                restaurant.increaseCountBookmark();
                restaurantRepository.save(restaurant);
            }
        }


    }

    public void removeRestaurantBookmark(Long userId, Long restaurantId) {
        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        RestaurantEntity restaurant = restaurantRepository.findRestaurantEntityByRestaurantIdAndStatus(restaurantId, Status.ACTIVE)
                .orElseThrow(()->new BaseException(BaseResponseStatus.RESTAURANT_NO_EXIST));

        BookmarkEntity bookmark = bookmarkRepository.findBookmarkEntityByUserEntityAndRestaurantEntity(user, restaurant)
                .orElse(null);

        if(bookmark == null){
            throw new BaseException(BaseResponseStatus.BOOKMARK_NO_EXIST);
        }else{
            if(bookmark.getStatus() == Status.ACTIVE){
                //bookmark 테이블에 status를 inactive로 변경
                bookmark.changeToInActive();
                bookmarkRepository.save(bookmark);

                //레스토랑 전체 즐겨찾기 수 감소
                restaurant.decreaseCountBookmark();
                restaurantRepository.save(restaurant);

            }else if(bookmark.getStatus() == Status.INACTIVE){
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
        //review table에 작성한 리뷰 저장
        reviewRepository.save(reviewEntity);

        //리뷰 작성시 이미지를 첨부했으면 각 이미지가 review_img table에 저장
        if(writeReviewDto.getReviewImg() != null){
            for(int i =0; i<writeReviewDto.getReviewImg().size(); i++){
                String imgUrl = writeReviewDto.getReviewImg().get(i);

                ReviewImgEntity reviewImgEntity = new ReviewImgEntity(imgUrl, reviewEntity);
                reviewImgRepository.save(reviewImgEntity);
            }
        }

        //'전체 평점/평점 개수'와 리뷰 개수를 restaurant table에 업데이트
        restaurant.writeReview(writeReviewDto.getStar());

        restaurantRepository.save(restaurant);

    }

    public void updateReview(Long userId, Long reviewId, UpdateReviewDto updateReviewDto) {
        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        ReviewEntity review = reviewRepository.findReviewEntityByReviewIdAndStatus(reviewId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.REVIEW_NO_EXIST));

        RestaurantEntity restaurant = restaurantRepository.findRestaurantEntityByRestaurantIdAndStatus(review.getRestaurantEntity().getRestaurantId(), Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.RESTAURANT_NO_EXIST));

        //마지막에 restaurant 테이블의 전체 평점, 평균 평점을 바꾸기 위해서 사용
        double oldStar = review.getStar();

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

        //'전체 평점/ 평균 평점' 업데이트
        restaurant.updateReview(oldStar, updateReviewDto.getStar());
        restaurantRepository.save(restaurant);

    }

    public ReviewFormDto getReviewInfo(Long userId, Long reviewId) {

        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        ReviewEntity review = reviewRepository.findReviewEntityByReviewIdAndStatus(reviewId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.REVIEW_NO_EXIST));

        if(review.getUserEntity() != user){
            throw new BaseException(BaseResponseStatus.NO_PRIVILEGE);
        }

        ReviewFormDto reviewFormDto = new ReviewFormDto(review.getContents(), review.getStar());
        List<ReviewImgEntity> reviewImgEntityList = reviewImgRepository.findReviewImgEntitiesByReviewEntityAndStatus(review, Status.ACTIVE);
        List<String> imgUrlList = null;
        if(reviewImgEntityList != null){
            imgUrlList = new ArrayList<>();
            for(int i =0; i<reviewImgEntityList.size() ; i++){
                imgUrlList.add(reviewImgEntityList.get(i).getReviewImgUrl());
            }
        }
        reviewFormDto.setReviewImg(imgUrlList);

        return reviewFormDto;
    }

    public RestaurantSearchListDto getRestaurantSearchList2(String keyword){

        RestaurantSearchListDto restaurantSearchListDto = new RestaurantSearchListDto(); //return할 Dto

        //native 쿼리에서는 일반 dto로 값을 받으면 No converter found capable of converting from type 오류가 뜨므로
        //Interface 생성 후 이름을 맞추면 자동으로 alias에서 변수와 연결시켜준다.
        List<RestaurantSummaryInterface> restaurantSummaryDtoList = restaurantRepository.findSearchList(keyword);

        restaurantSearchListDto.setCountRestaurant(restaurantSummaryDtoList.size());
        restaurantSearchListDto.setRestaurantSummaryDtoList(restaurantSummaryDtoList);

        return restaurantSearchListDto;
    }


    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        ReviewEntity review = reviewRepository.findReviewEntityByReviewIdAndStatus(reviewId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.REVIEW_NO_EXIST));

        if(review.getUserEntity() != user){
            throw new BaseException(BaseResponseStatus.NO_PRIVILEGE);
        }

        review.deleteReview();
        reviewRepository.save(review);
    }

    /*public RestaurantDetailsDto getRestaurantDetails(Long restaurantId) {

        RestaurantDetailsDto restaurantDetailsDto = new RestaurantDetailsDto();
        restaurantDetailsDto.setIsLogin(false);

        RestaurantEntity restaurant = restaurantRepository.findRestaurantEntityByRestaurantIdAndStatus(restaurantId, Status.ACTIVE)
                .orElseThrow(()->new BaseException(BaseResponseStatus.RESTAURANT_NO_EXIST));

        restaurantDetailsDto.updateInfo(
                restaurant.getName(),
                restaurant.getDetails(),
                restaurant.getCountBookmark(),
                restaurant.getCountHeart(),
                restaurant.getCountReview(),
                restaurant.getAverageStar(),
                restaurant.getAddress(),
                restaurant.getPhoneNumber()
                );

        List<MenuEntity> menuEntityList = menuRepository.findMenuEntitiesByRestaurantEntityAndStatus(restaurant, Status.ACTIVE);

        if(menuEntityList.size() == 0){
            restaurantDetailsDto.setMenuMap(null);
        }
        else{
            Map<>
            for(int i = 0; i<menuEntityList.size(); i++){



            }


        }


    }*/

    /*public RestaurantDetailsDto getRestaurantDetails(Long userId, Long restaurantId) {

        RestaurantDetailsDto restaurantDetailsDto = new RestaurantDetailsDto();
        restaurantDetailsDto.setIsLogin(true);






    }*/
}
