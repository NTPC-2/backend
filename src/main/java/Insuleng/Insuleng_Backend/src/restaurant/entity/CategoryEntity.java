package Insuleng.Insuleng_Backend.src.restaurant.entity;

import Insuleng.Insuleng_Backend.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Table(name = "category")
@NoArgsConstructor
@DynamicInsert
public class CategoryEntity extends BaseEntity {
    // 1.한식  2.양식  3.고기/구이  4.씨푸드  5.일/중 세계음식  6.카페/디저트  7.술집

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", length = 20)
    private String categoryName;



}
