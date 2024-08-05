package Insuleng.Insuleng_Backend.src.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatics {

    private int countBookmark;
    private int countMyReview;
    private int countMyHeart;
    private int countMyPost;
    private int countMyScrap;

    public UserStatics(Long countBookmark, Long countMyReview, Long countMyHeart, Long countMyPost, Long countMyScrap){
        this.countBookmark = countBookmark.intValue();
        this.countMyReview = countMyReview.intValue();
        this.countMyHeart = countMyHeart.intValue();
        this.countMyPost = countMyPost.intValue();
        this.countMyScrap = countMyScrap.intValue();

    }


}
