package spring.backend.dto.reset_password;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetRequestDTO {
    private String token;
    private String email;
}
