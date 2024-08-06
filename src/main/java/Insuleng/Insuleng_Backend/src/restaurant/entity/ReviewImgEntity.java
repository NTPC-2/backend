package Insuleng.Insuleng_Backend.src.restaurant.entity;

import Insuleng.Insuleng_Backend.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Table(name = "review_img")
@NoArgsConstructor
@DynamicInsert
public class ReviewImgEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_img_id")
    private Long reviewImgId;

    @Column(name = "review_img_url")
    private String reviewImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewEntity reviewEntity;

    public ReviewImgEntity(String reviewImgUrl, ReviewEntity reviewEntity){
        this.reviewImgUrl = reviewImgUrl;
        this.reviewEntity = reviewEntity;
    }

}
