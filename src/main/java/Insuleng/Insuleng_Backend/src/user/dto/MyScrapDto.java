package Insuleng.Insuleng_Backend.src.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyScrapDto {

    private Long postId;
    private String topic;
    private String contents;
    private int countLike;
    private int countComment;
    private String timeLine;
    private String userNickname;

}
