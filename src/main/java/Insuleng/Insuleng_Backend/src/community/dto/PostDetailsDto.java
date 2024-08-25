package Insuleng.Insuleng_Backend.src.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PostDetailsDto {
    private Long postId;
    private String topic;
    private String contents;
    private int countLike;
    private int countComment;
    private int countScrap;
    private String imgUrl;
    private String authorName;
    private LocalDateTime createdAt;
}
