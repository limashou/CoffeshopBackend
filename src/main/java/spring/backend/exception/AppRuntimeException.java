package spring.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppRuntimeException extends RuntimeException {
    private final HttpStatus status;
    public AppRuntimeException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
}
