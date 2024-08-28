package Insuleng.Insuleng_Backend.src.community.dto;

import Insuleng.Insuleng_Backend.utils.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

//검색 결과로 반환할 게시글의 정보
public class PostSummaryDto {
    private Long postId;
    private String topic;
    private String contentsSnippet;
    private int countLike;
    private int countComment;
    private String imgUrl;
    private String authorName;
    private int countScrap;
    private String timeLine;

    public PostSummaryDto(Long postId, String topic, String contentsSnippet, int countLike, int countComment, String imgUrl, String authorName, int countScrap, LocalDateTime localDateTime){
        this.postId = postId;
        this.topic = topic;
        this.contentsSnippet = contentsSnippet;
        this.countLike = countLike;
        this.countComment = countComment;
        this.imgUrl = imgUrl;
        this.authorName = authorName;
        this.countScrap = countScrap;
        this.timeLine = TimeUtil.getTimeLine(localDateTime);
    }

}
