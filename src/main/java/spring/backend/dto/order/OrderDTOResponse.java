package spring.backend.dto.order;

import lombok.*;
import spring.backend.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTOResponse {
    private Long id;
    private Long dopInfoCoffeeshop_id;
    private Long order_number;
    private LocalDateTime order_date;
    private LocalDateTime possibly_ready_time;
    private Order.PaymentMethod paymentMethod;
    private Order.PaymentStatus paymentStatus;
    private Order.Status status;
    private List<?> itemsList;
}
