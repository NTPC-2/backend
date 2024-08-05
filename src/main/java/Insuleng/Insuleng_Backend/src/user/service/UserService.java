package Insuleng.Insuleng_Backend.src.user.service;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.restaurant.entity.RestaurantEntity;
import Insuleng.Insuleng_Backend.src.restaurant.repository.RestaurantRepository;
import Insuleng.Insuleng_Backend.src.user.dto.*;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import Insuleng.Insuleng_Backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public MyPageDto getMyPage(Long userId) {

        UserEntity userEntity = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));


        UserStatics userStatics = userRepository.findUserStatics(userId);
        MyPageDto myPageDto = new MyPageDto(userStatics);

        return myPageDto;

    }

    public MyPageInfoDto getMyPageInfo(Long userId) {

        UserEntity userEntity = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        MyPageInfoDto myPageInfoDto = MyPageInfoDto.builder()
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .phoneNumber(userEntity.getPhoneNumber())
                .gender(userEntity.getGender())
                .age(userEntity.getAge())
                .profileImg(userEntity.getProfileImg())
                .build();

        return myPageInfoDto;

    }

    public void updateMyPageInfo(Long userId, MyPageUpdateDto myPageUpdateDto) {
        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        if(userRepository.existsUserEntitiesByNicknameAndStatus(myPageUpdateDto.getNickname(), Status.ACTIVE) == true){
            System.out.println("중복된 닉네임입니다");
            throw new BaseException(BaseResponseStatus.DUPLICATED_NICKNAME);
        }

        user.updateMyPage(myPageUpdateDto);
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
}
