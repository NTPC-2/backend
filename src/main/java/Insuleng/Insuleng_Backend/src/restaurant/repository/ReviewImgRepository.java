package Insuleng.Insuleng_Backend.src.restaurant.repository;

import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.restaurant.entity.ReviewEntity;
import Insuleng.Insuleng_Backend.src.restaurant.entity.ReviewImgEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewImgRepository extends JpaRepository<ReviewImgEntity, Long> {

    //기능: 리뷰를 업데이트 하면서 해당 review_id를 가진 Img의 status 변경
    //Dirty Checking이 아닌 여러 번의 벌크 연산을 할 경우 1차 캐시를 사용하지 않고 DB에 직접 쿼리를 날려 예외가 발생하기에 @Modifying 사용!
    //또한 1차 캐시를 사용하지 않고 DB에 직접 쿼리를 날리기 때문에 1차 캐시를 비워줘야 한다 -> 1차 캐시와 DB 동기화
    @Transactional
    @Modifying/*(clearAutomatically = true)*/ // 해당 코드가 영속성 컨텍스트를 초기화해주는 건데, 이 앞에 save가 있을 경우 영속성 컨텍스트의 내용이 초기화 되기 때문에
    //앞의 코드와의 관계로 이번엔 해당코드를 주석처리한다.
    @Query("update ReviewImgEntity  as r set r.status = :status where r.reviewEntity = :review")
    void updateStatusOfRestaurantImgEntities(@Param("status")Status status, @Param("review") ReviewEntity reviewEntity);

    List<ReviewImgEntity> findReviewImgEntitiesByReviewEntityAndStatus(ReviewEntity reviewEntity, Status status);


}
