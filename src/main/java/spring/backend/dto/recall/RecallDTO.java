package spring.backend.dto.recall;

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
