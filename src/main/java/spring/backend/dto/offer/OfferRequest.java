package spring.backend.dto.offer;

import lombok.*;
import spring.backend.entity.Menu;

import java.util.List;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferRequest {
    private Long id;
    private String name;
    private Long coffeeshopId;
    private Map<String,Integer> itemsWithQuantity;
    private double price;

}
