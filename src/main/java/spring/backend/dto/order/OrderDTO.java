package spring.backend.dto.order;

import lombok.*;
import spring.backend.entity.DopInfoCoffeeshop;
import spring.backend.entity.Order;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private Long dopInfoCoffeeshop_id;
    private Order.PaymentMethod paymentMethod;
    private Order.PaymentStatus paymentStatus;
    private List<ItemsDto> itemsList;
}
