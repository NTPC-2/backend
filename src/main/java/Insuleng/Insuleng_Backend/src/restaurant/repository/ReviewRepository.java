package Insuleng.Insuleng_Backend.src.restaurant.repository;

import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.restaurant.dto.ReviewDetailsDto;
import Insuleng.Insuleng_Backend.src.restaurant.entity.RestaurantEntity;
import Insuleng.Insuleng_Backend.src.restaurant.entity.ReviewEntity;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Optional<ReviewEntity> findReviewEntityByReviewIdAndStatus(Long reviewId, Status status);

    @Query("select r from ReviewEntity r left join fetch r.reviewImgEntityList as img where r.status = :status and img.status = :status")
    List<ReviewEntity> findAllWithImages(@Param("status")Status status);

    List<ReviewEntity> findReviewEntitiesByUserEntityAndStatus(UserEntity user, Status status);

    @Query("select r from ReviewEntity as r inner join fetch r.userEntity as u where r.status =:status and r.restaurantEntity =:restaurant")
    List<ReviewEntity> findReviewEntitiesByStatusJPQL(@Param("status")Status status, @Param("restaurant")RestaurantEntity restaurant);
}
