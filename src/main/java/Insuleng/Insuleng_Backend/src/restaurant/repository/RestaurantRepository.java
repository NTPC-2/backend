package Insuleng.Insuleng_Backend.src.restaurant.repository;

import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.home.dto.PopularRestaurantInterface;
import Insuleng.Insuleng_Backend.src.restaurant.dto.RestaurantSummaryDto;
import Insuleng.Insuleng_Backend.src.restaurant.dto.RestaurantSummaryInterface;
import Insuleng.Insuleng_Backend.src.restaurant.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long>, RestaurantQueryDslRepositoryCustom {

    Optional<RestaurantEntity> findRestaurantEntityByRestaurantIdAndStatus(Long restaurantId, Status status);

    @Query("select count(r) from RestaurantEntity as r inner join r.categoryEntity c where c.categoryId = :categoryId")
    Integer countRestaurant(@Param("categoryId")Long categoryId);

    @Query("select count(r) from RestaurantEntity as r")
    Integer countTotal();

    List<RestaurantEntity> findByStatus(Status status);

    //카테고리에 맞춰 해당 분류의 음식점 출력
    @Query("select distinct r from RestaurantEntity as r inner join r.categoryEntity c where c.categoryId = :categoryId")
    List<RestaurantEntity> findRestaurantByCategoryId(@Param("categoryId") Long categoryId);

    /*@Query(
            "SELECT distinct new Insuleng.Insuleng_Backend.src.restaurant.dto.RestaurantSummaryDto(" +
                    "   r.name, " +
                    "   r.mainImg, " +
                    "   r.countHeart, " +
                    "   r.countBookmark, " +
                    "   r.countReview, " +
                    "   r.averageStar" +
                    ") " +
                    "FROM RestaurantEntity r " +
                    "LEFT JOIN r.menuEntityList m "+
                    "WHERE m.menuName = :keyword "
                    //"OR r.name = :keyword"
    )
    public List<RestaurantSummaryDto> findSearchListByMenuNameOrRestaurantName(@Param("keyword")String keyword );*/

    /*@Query("select distinct r from RestaurantEntity r left join fetch r.menuEntityList m where m.menuName = :keyword or r.name =:keyword")
    List<RestaurantEntity> findSearchList2ByMenuNameOrRestaurantName(@Param("keyword")String keyword);
*/
    @Query(
            "SELECT distinct new Insuleng.Insuleng_Backend.src.restaurant.dto.RestaurantSummaryDto(r.name, r.mainImg, r.countHeart, r.countBookmark, r.countReview, r.averageStar) " +
                    "FROM RestaurantEntity r " +
                    "INNER JOIN r.restaurantTagMapEntityList mapping " +
                    "INNER JOIN mapping.restaurantTagEntity as tag " +
                    "where tag.tagName = :keyword or r.name = :keyword " +
                    "order by r.averageStar desc "
    )
    public List<RestaurantSummaryDto> findSearchListByTagNameOrRestaurantName(@Param("keyword")String keyword);

    @Query(
            value = "SELECT r.restaurant_id as restaurantId, " +
                    "       r.name as restaurantName," +
                    "       r.main_img as mainImg, " +
                    "       r.count_heart as countHeart, " +
                    "       r.count_bookmark as countBookmark, " +
                    "       r.count_review as countReview, " +
                    "       r.average_star as averageStar, " +
                    "       GROUP_CONCAT(DISTINCT m.menu_name SEPARATOR ', ') AS menuNames " +
                    "FROM restaurant as r " +
                    "LEFT JOIN menu as m ON r.restaurant_id = m.restaurant_id " +
                    "WHERE r.category_id = :categoryId " +
                    "GROUP BY r.restaurant_id, r.name, r.main_img, r.count_heart, r.count_bookmark, r.count_review, r.average_star " +
                    "ORDER BY r.average_star desc",
            nativeQuery = true
    )
    public List<RestaurantSummaryInterface> findRestaurantListOrderByAverageStar(@Param("categoryId")Long categoryId);

    @Query(
            value = "SELECT r.restaurant_id as restaurantId, " +
                    "       r.name as restaurantName," +
                    "       r.main_img as mainImg, " +
                    "       r.count_heart as countHeart, " +
                    "       r.count_bookmark as countBookmark, " +
                    "       r.count_review as countReview, " +
                    "       r.average_star as averageStar, " +
                    "       GROUP_CONCAT(DISTINCT m.menu_name SEPARATOR ', ') AS menuNames " +
                    "FROM restaurant as r " +
                    "LEFT JOIN menu as m ON r.restaurant_id = m.restaurant_id " +
                    "GROUP BY r.restaurant_id, r.name, r.main_img, r.count_heart, r.count_bookmark, r.count_review, r.average_star " +
                    "ORDER BY r.average_star desc",
            nativeQuery = true
    )
    public List<RestaurantSummaryInterface> findRestaurantListOrderByAverageStar();


    @Query(
            value = "SELECT r.restaurant_id as restaurantId, " +
                    "       r.name as restaurantName," +
                    "       r.main_img as mainImg, " +
                    "       r.count_heart as countHeart, " +
                    "       r.count_bookmark as countBookmark, " +
                    "       r.count_review as countReview, " +
                    "       r.average_star as averageStar, " +
                    "       GROUP_CONCAT(DISTINCT m.menu_name SEPARATOR ', ') AS menuNames " +
                    "FROM restaurant as r " +
                    "LEFT JOIN menu as m ON r.restaurant_id = m.restaurant_id " +
                    "LEFT JOIN restaurant_tag_map as rtm ON r.restaurant_id = rtm.restaurant_id " +
                    "LEFT JOIN restaurant_tag as rt ON rtm.restaurant_tag_id = rt.restaurant_tag_id " +
                    "WHERE r.name LIKE %:keyword% " +
                    "   OR m.menu_name LIKE %:keyword% " +
                    "   OR rt.tag_name LIKE %:keyword% " +
                    "GROUP BY r.restaurant_id, r.name, r.main_img, r.count_heart, r.count_bookmark, r.count_review, r.average_star " +
                    "ORDER BY r.average_star desc",
            nativeQuery = true
    )
    public List<RestaurantSummaryInterface> findSearchList(@Param("keyword")String keyword);

    @Query(
            value = "SELECT r.restaurant_id as restaurantId, " +
                    "       r.name as restaurantName," +
                    "       r.main_img as mainImg, " +
                    "       r.count_heart as countHeart, " +
                    "       r.count_bookmark as countBookmark, " +
                    "       r.count_review as countReview, " +
                    "       r.average_star as averageStar, " +
                    "       GROUP_CONCAT(DISTINCT m.menu_name SEPARATOR ', ') AS menuNames " +
                    "FROM restaurant as r " +
                    "LEFT JOIN menu as m ON r.restaurant_id = m.restaurant_id " +
                    "GROUP BY r.restaurant_id, r.name, r.main_img, r.count_heart, r.count_bookmark, r.count_review, r.average_star " +
                    "ORDER BY r.count_heart desc",
            nativeQuery = true
    )
    public List<PopularRestaurantInterface> findPopularRestaurants();


}
