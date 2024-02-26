package spring.backend.dto.info_and_coffeeshop;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoffeeshopDTO {
    private Long id;
    private String name;
    private String email;
    private String description;
}
