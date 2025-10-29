package by.antohakon.vetclinicemployes.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.UUID;

@RestControllerAdvice
@Slf4j
@Data
public class GlobalExceptionHandler {

    @Data
    @AllArgsConstructor
    private static class ErrorResponse {
        private String errorType;
        private String message;
    }


    @ExceptionHandler(EmployeDuplicationException.class)
    public ResponseEntity<ErrorResponse> handle(EmployeDuplicationException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        e.getMessage(),
                        e.getClass().getSimpleName()
                ));
    }

    @ExceptionHandler(EmployeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(EmployeNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        e.getMessage(),
                        e.getClass().getSimpleName()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation Error");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        e.getClass().getSimpleName(),
                        errorMessage
                ));

    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentException(MethodArgumentTypeMismatchException e) {

        if (e.getRequiredType() == UUID.class) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(
                            e.getClass().getSimpleName(),
                            "Некорректный UUID - " + e.getValue()
                    ));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        e.getClass().getSimpleName(),
                        e.getMessage()
                ));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        ex.getClass().getSimpleName()
                ));
    }

}
