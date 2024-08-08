package Insuleng.Insuleng_Backend.src.restaurant.entity;

import Insuleng.Insuleng_Backend.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Table(name = "restaurant_tag")
@NoArgsConstructor
@DynamicInsert
public class RestaurantTagEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_tag_id")
    private Long restaurantTagId;

    @Column(name = "tag_name")
    private String tagName;

}
