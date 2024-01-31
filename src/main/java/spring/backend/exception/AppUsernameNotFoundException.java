package spring.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Getter
public class AppUsernameNotFoundException extends UsernameNotFoundException {
    private final HttpStatus status;
    public AppUsernameNotFoundException(String message,HttpStatus status){
        super(message);
        this.status = status;
    }
}
