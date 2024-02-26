package spring.backend.dto.order;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemsDTOResponse {

    String name;
    int quantity;
    double price;

}
