package spring.backend.dto;

import lombok.*;
import spring.backend.entity.Menu;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuDTO {
    private Long id;
    private String items;
    private double price;
    private Long coffeshop_id;
    private Menu.ItemsType type;
}
