package Insuleng.Insuleng_Backend.src.restaurant.repository;

import Insuleng.Insuleng_Backend.src.restaurant.entity.BookmarkEntity;
import Insuleng.Insuleng_Backend.src.restaurant.entity.RestaurantEntity;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {

    Optional<BookmarkEntity> findBookmarkEntityByUserEntityAndRestaurantEntity(UserEntity user, RestaurantEntity restaurant);
}
