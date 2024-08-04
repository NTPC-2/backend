package Insuleng.Insuleng_Backend.src.restaurant.repository;

import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.restaurant.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    @Query("select count(r) from RestaurantEntity as r inner join r.categoryEntity c where c.categoryId = :categoryId")
    Integer countRestaurant(@Param("categoryId")Long categoryId);

    @Query("select count(r) from RestaurantEntity as r")
    Integer countTotal();

    List<RestaurantEntity> findByStatus(Status status);

    @Query("select distinct r from RestaurantEntity as r inner join r.categoryEntity c where c.categoryId = :categoryId")
    List<RestaurantEntity> findRestaurantByCategoryId(@Param("categoryId") Long categoryId);
}
