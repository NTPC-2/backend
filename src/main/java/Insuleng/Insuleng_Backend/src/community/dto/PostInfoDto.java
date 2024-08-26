package Insuleng.Insuleng_Backend.src.community.dto;

import Insuleng.Insuleng_Backend.utils.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class PostInfoDto {

    private Long postId;

    private String authorName;
    private String timeLine;
    private String userImgUrl;

    private String topic;
    private String contents;
    private int countLike;
    private int countComment;
    private int countScrap;
    private String postImgUrl;

    private Boolean isMyLike;
    private Boolean isMyScrap;

    public PostInfoDto(Long postId, String authorName, LocalDateTime localDateTime, String userImgUrl, String topic, String contents, int countLike, int countComment,
                       int countScrap, String postImgUrl, Boolean isMyLike, Boolean isMyScrap){
        this.postId = postId;
        this.authorName = authorName;
        this.timeLine = TimeUtil.getTimeLine(localDateTime);
        this.userImgUrl = userImgUrl;
        this.topic = topic;
        this.contents = contents;
        this.countLike = countLike;
        this.countComment = countComment;
        this.countScrap = countScrap;
        this.postImgUrl = postImgUrl;
        this.isMyLike = isMyLike;
        this.isMyScrap = isMyScrap;

    }

}
