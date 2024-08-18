package Insuleng.Insuleng_Backend.src.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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
}
