package Insuleng.Insuleng_Backend.src.community.dto;

import Insuleng.Insuleng_Backend.utils.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentInfoDto {

    private Long userId;
    private String nickname;
    private String userImgUrl;

    private String contents;
    private Integer countLike;
    private Boolean isMyLike;

    private String timeLine;

    public CommentInfoDto(Long userId, String nickname, String userImgUrl, String contents, Integer countLike, Boolean isMyLike, LocalDateTime localDateTime){
        this.userId = userId;
        this.nickname = nickname;
        this.userImgUrl = userImgUrl;
        this.contents = contents;
        this.countLike = countLike;
        this.isMyLike = isMyLike;
        this.timeLine = TimeUtil.getTimeLine(localDateTime);

    }

}
