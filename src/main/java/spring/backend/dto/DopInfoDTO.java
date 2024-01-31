package spring.backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DopInfoDTO {
    private Long id;
    private Long shop_id;
    private String city;
    private String address;
    private double rating;
}
