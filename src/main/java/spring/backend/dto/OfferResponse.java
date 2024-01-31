package spring.backend.dto;
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
    private Map<Long,Integer> itemsWithQuanity;
    private double price;
}
