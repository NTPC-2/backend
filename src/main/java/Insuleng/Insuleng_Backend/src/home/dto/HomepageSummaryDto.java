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
public class HomepageSummaryDto {

    private List<PopularRestaurantInterface> popularRestaurantInterfaceList;
    private List<PopularPostDto> popularPostDtoList;
}
