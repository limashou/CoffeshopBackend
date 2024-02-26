package spring.backend.dto.order;

import lombok.*;
import spring.backend.entity.Order;
import spring.backend.entity.OrderItems;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemsDto {
    private Long id;
    private Long item;
    private OrderItems.ItemType itemType;
    private int quantity;
}
