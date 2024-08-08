package Insuleng.Insuleng_Backend.src.restaurant.entity;

import Insuleng.Insuleng_Backend.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Table(name = "restaurant_tag_map")
@NoArgsConstructor
@DynamicInsert
public class RestaurantTagMapEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long restaurantTagMapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurantEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_tag_id")
    private RestaurantTagEntity restaurantTagEntity;


}
