package spring.backend.exception;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class AppExpiredJwtException extends ExpiredJwtException {
    private final HttpStatus status;
    public AppExpiredJwtException(Header header, Claims claims, String message,HttpStatus status) {
        super(header, claims, message);
        this.status = status;
    }

}
