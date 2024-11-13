package experience.demo.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NegativeStockException.class)
    public ResponseEntity<String> handleNegativeStockException(NegativeStockException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}