package Insuleng.Insuleng_Backend.src.home.dto;

import Insuleng.Insuleng_Backend.utils.TimeUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PopularPostDto {

    private Long postId;
    private String topic;
    private String contents;
    private Integer countLike;
    private Integer countScrap;
    private Integer countComment;
    private String imgUrl;
    private String authorName;
    private String timeLine;

    public PopularPostDto(Long postId, String topic, String contents, Integer countLike, Integer countScrap, Integer countComment, String imgUrl, String authorName, LocalDateTime localDateTime){
        this.postId = postId;
        this.topic = topic;
        this.contents = contents;
        this.countLike = countLike;
        this.countScrap = countScrap;
        this.countComment = countComment;
        this.imgUrl = imgUrl;
        this.authorName = authorName;
        this.timeLine = TimeUtil.getTimeLine(localDateTime);
    }

}
