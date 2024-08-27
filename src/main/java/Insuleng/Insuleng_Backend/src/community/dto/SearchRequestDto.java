package Insuleng.Insuleng_Backend.src.community.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestDto {

    @Schema(description = "검색 키워드", nullable = false, example = "맛집")
    private String keyword;
}
