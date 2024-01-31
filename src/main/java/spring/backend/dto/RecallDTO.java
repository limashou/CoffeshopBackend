package spring.backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecallDTO {
    private String text;
    private int rating;
}
