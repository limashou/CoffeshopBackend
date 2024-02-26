package spring.backend.dto.reset_password;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetDTO {
    private String token;
    private String newPassword;
}
