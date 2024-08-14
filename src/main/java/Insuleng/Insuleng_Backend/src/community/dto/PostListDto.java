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

//PostSummaryDto의 리스트를 포함하여 전체 검색 결과
public class PostListDto {
    private int countPost;
    private List<PostSummaryDto> postSummaryDtoList;
}
