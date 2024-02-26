package spring.backend.dto.offer;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferResponse {
    private Long id;
    private String name;
    private Long coffeeshopId;
    private Map<Long,Integer> itemsWithQuantity;
    private double price;
}

