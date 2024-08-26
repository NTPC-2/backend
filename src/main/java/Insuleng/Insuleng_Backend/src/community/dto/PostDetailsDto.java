package Insuleng.Insuleng_Backend.src.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PostDetailsDto {
    private PostInfoDto postInfoDto;
    private List<CommentInfoDto> commentInfoDtoList;
}
