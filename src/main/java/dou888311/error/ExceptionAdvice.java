package dou888311.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Error> validationException(Exception e) {
        Error error = new Error(400, "Validation Failed");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Error> notFoundException(Exception e) {
        Error error = new Error(404, "Item not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
