package spring.backend.exception;

import jakarta.mail.MessagingException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppMessagingException extends MessagingException {
    private final HttpStatus status;
    public AppMessagingException(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
