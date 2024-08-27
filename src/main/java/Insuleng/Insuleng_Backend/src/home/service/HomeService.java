package Insuleng.Insuleng_Backend.src.home.service;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.community.entity.PostEntity;
import Insuleng.Insuleng_Backend.src.community.repository.PostRepository;
import Insuleng.Insuleng_Backend.src.home.dto.BookmarkRestaurantDto;
import Insuleng.Insuleng_Backend.src.home.dto.HomepageSummaryDto;
import Insuleng.Insuleng_Backend.src.home.dto.PopularPostDto;
import Insuleng.Insuleng_Backend.src.home.dto.RouletteListDto;
import Insuleng.Insuleng_Backend.src.restaurant.entity.RestaurantEntity;
import Insuleng.Insuleng_Backend.src.restaurant.repository.RestaurantRepository;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import Insuleng.Insuleng_Backend.src.user.repository.UserRepository;
import Insuleng.Insuleng_Backend.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final RestaurantRepository restaurantRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public HomepageSummaryDto getHomePage() {

        HomepageSummaryDto homepageSummaryDto = new HomepageSummaryDto();

        homepageSummaryDto.setPopularRestaurantInterfaceList(restaurantRepository.findPopularRestaurants());

        List<PostEntity> postEntityList = postRepository.findPopularPosts();

        List<PopularPostDto> popularPostDtoList = new ArrayList<>();

        for(int i =0; i<postEntityList.size(); i++){
            PopularPostDto popularPostDto = new PopularPostDto(
                    postEntityList.get(i).getPostId(),
                    postEntityList.get(i).getTopic(),
                    postEntityList.get(i).getContents(),
                    postEntityList.get(i).getCountLike(),
                    postEntityList.get(i).getCountScrap(),
                    postEntityList.get(i).getCountComment(),
                    postEntityList.get(i).getImgUrl(),
                    postEntityList.get(i).getUserEntity().getNickname()
            );
            String timeLine = TimeUtil.getTimeLine(postEntityList.get(i).getCreateAt());
            popularPostDto.setTimeLine(timeLine);

            popularPostDtoList.add(popularPostDto);
        }

        homepageSummaryDto.setPopularPostDtoList(popularPostDtoList);

        return homepageSummaryDto;
    }

    public List<BookmarkRestaurantDto> getRouletteList(Long userId) {

        UserEntity user = userRepository.findUserEntityByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        List<RestaurantEntity> restaurantEntityList = restaurantRepository.findRouletteList(Status.ACTIVE, user);
        List<BookmarkRestaurantDto> bookmarkRestaurantDtoList = new ArrayList<>();

        for(int i =0; i< restaurantEntityList.size(); i++){
            BookmarkRestaurantDto bookmarkRestaurantDto = new BookmarkRestaurantDto(
                    restaurantEntityList.get(i).getRestaurantId(),
                    restaurantEntityList.get(i).getName()
            );
            bookmarkRestaurantDtoList.add(bookmarkRestaurantDto);
        }

        return bookmarkRestaurantDtoList;
    }
}
