package Insuleng.Insuleng_Backend.src.restaurant.entity;

import Insuleng.Insuleng_Backend.config.BaseEntity;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Table(name = "review")
@NoArgsConstructor
@DynamicInsert
public class ReviewEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(nullable = false, length = 200)
    private String contents;

    @Column(nullable = false)
    private Double star;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurantEntity;

    public ReviewEntity(String contents, Double star, UserEntity userEntity, RestaurantEntity restaurantEntity){
        this.contents = contents;
        this.star = star;
        this.userEntity = userEntity;
        this.restaurantEntity = restaurantEntity;
    }

}
