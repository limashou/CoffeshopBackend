package spring.backend.dto;

import lombok.*;
import spring.backend.entity.Coffeeshop;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DopInfoCreateDTO {
    private Long id;
    private Coffeeshop coffeeshop;
    private String city;
    private String address;
}
