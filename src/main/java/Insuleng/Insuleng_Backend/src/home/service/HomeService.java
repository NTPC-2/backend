package Insuleng.Insuleng_Backend.src.home.service;

import Insuleng.Insuleng_Backend.src.community.repository.PostRepository;
import Insuleng.Insuleng_Backend.src.home.dto.HomepageSummaryDto;
import Insuleng.Insuleng_Backend.src.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final RestaurantRepository restaurantRepository;
    private final PostRepository postRepository;

    public HomepageSummaryDto getHomePage() {

        HomepageSummaryDto homepageSummaryDto = new HomepageSummaryDto();

        homepageSummaryDto.setPopularRestaurantInterfaceList(restaurantRepository.findPopularRestaurants());
        homepageSummaryDto.setPopularPostDtoList(postRepository.findPopularPosts());

        return homepageSummaryDto;
    }
}
