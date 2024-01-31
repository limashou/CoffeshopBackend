package spring.backend.configuration;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.backend.helpers.ErrorDto;
import spring.backend.exception.*;

@ControllerAdvice
public class RestExceptionHandler extends Throwable {
    @ExceptionHandler(value = {AppRuntimeException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(AppRuntimeException ex){
        return ResponseEntity.status(ex.getStatus()).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(value = {AppUsernameNotFoundException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(AppUsernameNotFoundException ex){
        return ResponseEntity.status(ex.getStatus()).body(new ErrorDto(ex.getMessage()));
    }
    @ExceptionHandler(value = {AppIOException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(AppIOException ex){
        return ResponseEntity.status(ex.getStatus()).body(new ErrorDto(ex.getMessage()));
    }
    @ExceptionHandler(value = {AppMessagingException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(AppMessagingException ex){
        return ResponseEntity.status(ex.getStatus()).body(new ErrorDto(ex.getMessage()));
    }
    @ExceptionHandler(value = {AppExpiredJwtException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(AppExpiredJwtException ex){
        return ResponseEntity.status(ex.getStatus()).body(new ErrorDto(ex.getMessage()));
    }
}
