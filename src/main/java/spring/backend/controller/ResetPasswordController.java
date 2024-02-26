package spring.backend.controller;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.backend.dto.reset_password.PasswordResetDTO;
import spring.backend.dto.reset_password.PasswordResetRequestDTO;
import spring.backend.entity.User;
import spring.backend.service.EmailService;
import spring.backend.service.JwtService;
import spring.backend.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/api/reset")
@AllArgsConstructor
public class ResetPasswordController {
    UserService userService;
    JwtService jwtService;
    EmailService emailService;
    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody PasswordResetRequestDTO requestDTO)throws MessagingException, IOException {
        User user = userService.findByEmail(requestDTO.getEmail());
        if (user != null) {
            String resetToken;
            if (requestDTO.getToken() == null || requestDTO.getToken().isEmpty()) {
                resetToken = jwtService.generatePasswordResetToken(user);
            } else {
                resetToken = requestDTO.getToken();
            }
            String resetUrl = "http://localhost:8080/api/reset/reset-password";
            emailService.sendResetTokenByEmail(user.getEmail(), resetUrl, resetToken);
            return ResponseEntity.ok("Password reset token sent successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody PasswordResetDTO reset) {
        if (jwtService.validatePasswordResetToken(token)) {
            jwtService.resetPassword(token,reset.getNewPassword());
            return ResponseEntity.ok("Password reset successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }
    }
}
