package Insuleng.Insuleng_Backend.src.restaurant.repository;

import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.restaurant.entity.MenuEntity;
import Insuleng.Insuleng_Backend.src.restaurant.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

    List<MenuEntity> findMenuEntitiesByRestaurantEntityAndStatus(RestaurantEntity restaurantEntity, Status status);

}
