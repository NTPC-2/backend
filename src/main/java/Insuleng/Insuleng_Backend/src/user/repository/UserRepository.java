package Insuleng.Insuleng_Backend.src.user.repository;

import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.community.entity.PostEntity;
import Insuleng.Insuleng_Backend.src.restaurant.entity.HeartEntity;
import Insuleng.Insuleng_Backend.src.restaurant.entity.RestaurantEntity;
import Insuleng.Insuleng_Backend.src.restaurant.entity.ReviewEntity;
import Insuleng.Insuleng_Backend.src.user.dto.UserStatics;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//CRUD 함수를 JpaRepository가 들고 있음
//@Repository 어노테이션이 없어도 JpaRepository를 상속했기 때문에 자동으로 IoC된다.
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsUserEntitiesByEmailAndStatus(String email, Status status);
    Boolean existsUserEntitiesByNicknameAndStatus(String nickname, Status status);

    Optional<UserEntity> findUserEntityByEmailAndStatus(String email, Status status);
    Optional<UserEntity> findUserEntityByNicknameAndPhoneNumberAndStatus(String nickname, int phoneNumber, Status status);
    Optional<UserEntity> findUserEntityByUserIdAndStatus(Long userId, Status status);

    //@Query 문에서 Entity가 아닌 Dto로 반환을 받고 싶다면 (select new Dto 클래스의 경로명) 을 지정해주면 된다.
    @Query("select new Insuleng.Insuleng_Backend.src.user.dto.UserStatics("
            + "(select count(b) from BookmarkEntity b where b.userEntity.userId = :userId and b.status = :status), "
            + "(select count(r) from ReviewEntity r where r.userEntity.userId = :userId and r.status = :status), "
            + "(select count(h) from HeartEntity h where h.userEntity.userId = :userId and h.status = :status), "
            + "(select count(p) from PostEntity p where p.userEntity.userId = :userId and p.status = :status), "
            + "(select count(s) from ScrapEntity s where s.userEntity.userId = :userId and s.status = :status)) "
            + "FROM UserEntity u WHERE u.userId = :userId")
    public UserStatics findUserStatics(@Param("userId")Long userId, @Param("status")Status status);

    @Query("select r from BookmarkEntity as b inner join b.restaurantEntity r where b.userEntity.userId = :userId and b.userEntity.status = :status and b.status =:status")
    List<RestaurantEntity> findMyBookmarks(@Param("userId")Long userId, @Param("status")Status status);

    @Query("select p from PostEntity as p where p.userEntity = :userEntity and p.status =:status")
    List<PostEntity> findMyPosts(@Param("userEntity")UserEntity userEntity, @Param("status")Status status);

    @Query("select r from HeartEntity as h inner join h.restaurantEntity r where h.userEntity = :userEntity and h.userEntity.status =:status and h.status =:status")
    List<RestaurantEntity> findMyHearts(@Param("userEntity")UserEntity userEntity, @Param("status")Status status);

  /*  @Query("select r from ReviewEntity as r where r.userEntity = :userEntity and r.status =:status")
    List<ReviewEntity> findMyReviews(@Param("userEntity")UserEntity userEntity, @Param("status")Status status);*/
    //fetch join으로 연관된 이미지까지 한번에 가져오기

    //원래 reviewImg 중 status의 상태에 따라 join을 다르게 해야하지만 JPQL로는 쿼리에 조건식을 넣을 수 없어서
    //일단 firstImg 없이 구현하고, 나중에 querydsl을 통해 구현하기
    @Query("select r from ReviewEntity r left join fetch r.reviewImgEntityList as img where r.userEntity =:user and r.status = :status")
    List<ReviewEntity> findMyReviews(@Param("user")UserEntity userEntity, @Param("status")Status status);

}
