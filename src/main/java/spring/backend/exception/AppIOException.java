package spring.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.IOException;
@Getter
public class AppIOException extends IOException {
    private final HttpStatus status;

    public AppIOException(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
