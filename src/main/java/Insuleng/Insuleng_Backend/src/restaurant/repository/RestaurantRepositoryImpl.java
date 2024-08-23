package Insuleng.Insuleng_Backend.src.restaurant.repository;

import Insuleng.Insuleng_Backend.src.restaurant.dto.RestaurantDetailsDto;
import Insuleng.Insuleng_Backend.src.restaurant.entity.RestaurantEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.spel.ast.Projection;


import java.util.List;


@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantQueryDslRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RestaurantEntity> getRestaurantDetails(Long restaurantId) {
        return /*queryFactory
                .select(Projections.constructor(RestaurantDetailsDto.class,
                 null,
                restaurantEntity.restaurantId,
                restaurantEntity.details,
                restaurantEntity.countBookmark,
                restaurantEntity.countHeart,
                restaurantEntity.countReview,
                restaurantEntity.averageStar,
                null,
                restaurantEntity.address,
                restaurantEntity.phoneNumber,
                null))
                .from(restaurantEntity)
                .fetchOne();*/
                null;


    }
}