package Insuleng.Insuleng_Backend.src.community.entity;

import Insuleng.Insuleng_Backend.config.BaseEntity;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor
@DynamicInsert
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(nullable = false) // 댓글 내용 글자 수 조건 추가
    private String contents;

    @Column(nullable = false, name = "parent_comment_id")
    private Long parentCommentId;
    @Column(nullable = false, name = "comment_level")
    private Integer commentLevel;
    @Column(nullable = false, name = "group_number")
    private Integer groupNumber;
    @Column(nullable = false, name = "count_like")
    private Integer countLike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

}
