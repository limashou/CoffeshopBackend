package spring.backend.dto;

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
