package Insuleng.Insuleng_Backend.src.home.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkRestaurantDto {

    private Long restaurantId;
    private String restaurantName;

}
