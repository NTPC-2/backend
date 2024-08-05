package Insuleng.Insuleng_Backend.src.user.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageDto {

    private int countMyBookmark;
    private int countMyReview;
    private int countMyHeart;
    private int countMyPost;
    private int countMyScrap;

    public MyPageDto(UserStatics userStatics){
        this.countMyBookmark = userStatics.getCountBookmark();
        this.countMyReview = userStatics.getCountMyReview();
        this.countMyHeart = userStatics.getCountMyHeart();
        this.countMyPost = userStatics.getCountMyPost();
        this.countMyScrap = userStatics.getCountMyScrap();
    }

}
