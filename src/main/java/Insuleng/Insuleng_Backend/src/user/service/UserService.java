package Insuleng.Insuleng_Backend.src.user.service;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.community.entity.PostEntity;
import Insuleng.Insuleng_Backend.src.restaurant.entity.RestaurantEntity;
import Insuleng.Insuleng_Backend.src.restaurant.entity.ReviewEntity;
import Insuleng.Insuleng_Backend.src.restaurant.repository.RestaurantRepository;
import Insuleng.Insuleng_Backend.src.storage.S3Uploader;
import Insuleng.Insuleng_Backend.src.user.dto.*;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import Insuleng.Insuleng_Backend.src.user.repository.UserRepository;
import Insuleng.Insuleng_Backend.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final S3Uploader s3Uploader;

    public MyPageDto getMyPage(Long userId) {

        UserEntity userEntity = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));


        UserStatics userStatics = userRepository.findUserStatics(userId, Status.ACTIVE);
        MyPageDto myPageDto = new MyPageDto(userStatics, userEntity.getNickname());

        return myPageDto;

    }

    public MyPageFormDto getMyPageInfo(Long userId) {

        UserEntity userEntity = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        MyPageFormDto myPageFormDto = MyPageFormDto.builder()
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .phoneNumber(userEntity.getPhoneNumber())
                .gender(userEntity.getGender())
                .age(userEntity.getAge())
                .profileImg(userEntity.getProfileImg())
                .build();

        return myPageFormDto;

    }

    public void updateMyPageInfo(Long userId, MyPageUpdateDto myPageUpdateDto) {
        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        if(userRepository.existsUserEntitiesByNicknameAndStatus(myPageUpdateDto.getNickname(), Status.ACTIVE) == true){
            System.out.println("중복된 닉네임입니다");
            throw new BaseException(BaseResponseStatus.DUPLICATED_NICKNAME);
        }

        String profileImg;

        if(myPageUpdateDto.getProfileImg().isEmpty() || Objects.isNull(myPageUpdateDto.getProfileImg().getOriginalFilename())){
            profileImg = null;
        }
        else{
            profileImg = s3Uploader.upload(myPageUpdateDto.getProfileImg());
        }

        user.updateMyPage(myPageUpdateDto, profileImg);
        userRepository.save(user);
    }

    public List<MyBookmarkDto> getMyBookmarks(Long userId) {

        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        List<RestaurantEntity> restaurantEntityList = userRepository.findMyBookmarks(userId, Status.ACTIVE);
        List<MyBookmarkDto> myBookmarkDtoList = new ArrayList<>();

        for(int i =0; i<restaurantEntityList.size(); i++){
            MyBookmarkDto myBookmarkDto = MyBookmarkDto.builder()
                    .restaurantName(restaurantEntityList.get(i).getName())
                    .mainImg(restaurantEntityList.get(i).getMainImg())
                    .countHeart(restaurantEntityList.get(i).getCountHeart())
                    .countBookmark(restaurantEntityList.get(i).getCountBookmark())
                    .countReview(restaurantEntityList.get(i).getCountReview())
                    .build();

            myBookmarkDtoList.add(myBookmarkDto);
        }
        return myBookmarkDtoList;

    }

    public List<MyPostDto> getMyPosts(Long userId) {
        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        List<PostEntity> postEntityList = userRepository.findMyPosts(user, Status.ACTIVE);
        List<MyPostDto> myPostDtoList = new ArrayList<>();

        for(int i =0; i<postEntityList.size(); i++){
            MyPostDto myPostDto = MyPostDto.builder()
                    .topic(postEntityList.get(i).getTopic())
                    .contents(postEntityList.get(i).getContents())
                    .countLike(postEntityList.get(i).getCountLike())
                    .countComment(postEntityList.get(i).getCountComment())
                    .timeLine(TimeUtil.getTimeLine(postEntityList.get(i).getCreateAt()))
                    .userNickname(postEntityList.get(i).getUserEntity().getNickname())
                    .build();

            myPostDtoList.add(myPostDto);
        }
        return myPostDtoList;
    }

    public List<MyHeartDto> getMyHearts(Long userId) {
        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        List<RestaurantEntity> restaurantEntityList = userRepository.findMyHearts(user, Status.ACTIVE);
        List<MyHeartDto> myHeartDtoList = new ArrayList<>();

        for(int i =0; i<restaurantEntityList.size(); i++){
            MyHeartDto myHeartDto = MyHeartDto.builder()
                    .restaurantName(restaurantEntityList.get(i).getName())
                    .mainImg(restaurantEntityList.get(i).getMainImg())
                    .countHeart(restaurantEntityList.get(i).getCountHeart())
                    .countBookmark(restaurantEntityList.get(i).getCountBookmark())
                    .countReview(restaurantEntityList.get(i).getCountReview())
                    .build();

            myHeartDtoList.add(myHeartDto);
        }
        return myHeartDtoList;
    }

    public List<MyReviewDto> getMyReviews(Long userId) {
        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        List<ReviewEntity> reviewEntityList = userRepository.findMyReviews(user, Status.ACTIVE);
        List<MyReviewDto> myReviewDtoList = new ArrayList<>();

        for(int i = 0; i<reviewEntityList.size() ; i++){
            MyReviewDto myReviewDto = MyReviewDto.builder()
                    .restaurantName(reviewEntityList.get(i).getRestaurantEntity().getName())
                    .star(reviewEntityList.get(i).getStar())
                    .contents(reviewEntityList.get(i).getContents())
                    .timeLine(TimeUtil.getTimeLine(reviewEntityList.get(i).getCreateAt()))
                    .userNickname(user.getNickname())
                    .build();

           myReviewDto.setImgList(userRepository.findMyReviewImg(Status.ACTIVE, reviewEntityList.get(i), user));
           myReviewDtoList.add(myReviewDto);
        }

        return myReviewDtoList;

    }

    public void imageUpload(Long userId, MultipartFile file) {

        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        //s3에 이미지 저장 후 url 주소를 return
        String profileImg = s3Uploader.upload(file);

        //user 테이블에 프로필 이미지의 url 저장
        user.updateProfileImg(profileImg);
        userRepository.save(user);

    }

    public void deleteUser(Long userId) {

        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        user.changeToInActive();
        userRepository.save(user);

    }
}
