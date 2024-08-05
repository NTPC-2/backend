package Insuleng.Insuleng_Backend.src.restaurant.entity;

import Insuleng.Insuleng_Backend.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "restaurant")
@NoArgsConstructor
@DynamicInsert
public class RestaurantEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long restaurantId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column
    private int phoneNumber;

    @Column(length = 500)
    private String details;

    @Column
    private String address;

    @Column(nullable = false)
    private int countHeart;

    @Column(nullable = false)
    private int countReview;

    @Column(nullable = false)
    private int sumStar;

    @Column(nullable = false)
    private int countBookmark;

    @Column
    private String mainImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    @OneToMany(mappedBy = "restaurantEntity", cascade = CascadeType.ALL)
    List<HeartEntity> heartEntityList;

    @OneToMany(mappedBy = "restaurantEntity", cascade = CascadeType.ALL)
    List<MenuEntity> menuEntityList;

    @OneToMany(mappedBy = "restaurantEntity", cascade = CascadeType.ALL)
    List<BookmarkEntity> bookmarkEntityList;


    //음식점 좋아요를 누르면 실행
    public void increaseCountHeart(){
        this.countHeart++;
    }

    //음식점 좋아요를 해제하면 실행
    public void decreaseCountHeart(){
        this.countHeart--;
    }




}
