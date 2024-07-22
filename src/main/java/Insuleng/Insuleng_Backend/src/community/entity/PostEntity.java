package Insuleng.Insuleng_Backend.src.community.entity;

import Insuleng.Insuleng_Backend.config.BaseEntity;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Table(name = "post")
@NoArgsConstructor
@DynamicInsert
public class PostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(nullable = false) // 제목 글자 수 제한 추가하기
    private String topic;

    @Column(nullable = false) // 내용 글자 수 제한 추가하기
    private String contents;
    @Column(name = "count_like", nullable = false)
    private int countLike;
    @Column(name = "count_comment", nullable = false)
    private int countComment;
    @Column(name = "count_parent_comment", nullable = false)
    private int countParentComment;

    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;


}
