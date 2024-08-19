package Insuleng.Insuleng_Backend.src.restaurant.repository;

import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.restaurant.entity.HeartEntity;
import Insuleng.Insuleng_Backend.src.restaurant.entity.RestaurantEntity;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<HeartEntity, Long> {

    Optional<HeartEntity> findHeartEntityByUserEntityAndAndRestaurantEntity(UserEntity user, RestaurantEntity restaurant);

    Boolean existsByUserEntityAndStatus(UserEntity user, Status status);
}
