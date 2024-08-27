package Insuleng.Insuleng_Backend.src.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RouletteListDto {

    private List<BookmarkRestaurantDto> bookmarkRestaurantDtoList;
}
