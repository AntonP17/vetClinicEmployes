package by.antohakon.vetclinicemployes.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public ResponseEntity<ErrorResponse> handleDuplicate(EmployeDuplicationException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        e.getMessage(),
                        e.getClass().getSimpleName()
                ));
    }

    @ExceptionHandler(EmployeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeNotFound(EmployeNotFoundException e) {
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        String errorMessage = extractUserFriendlyMessage(e);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        e.getClass().getSimpleName(),
                        errorMessage
                ));

    }

    private String extractUserFriendlyMessage(HttpMessageNotReadableException e) {

        String originalMessage = e.getMessage();

        // Для enum ошибок
        if (originalMessage != null && originalMessage.contains("not one of the values accepted for Enum class")) {
            return extractEnumErrorMessage(originalMessage);
        }

        // Для других ошибок парсинга JSON
        if (originalMessage != null && originalMessage.contains("JSON parse error")) {
            return "Некорректный формат JSON данных";
        }

        return "Ошибка в формате данных";

    }

    private String extractEnumErrorMessage(String originalMessage) {

        // Регулярное выражение для извлечения значений enum
        Pattern pattern = Pattern.compile("\\[([^]]+)\\]");
        Matcher matcher = pattern.matcher(originalMessage);

        if (matcher.find()) {
            String enumValues = matcher.group(1);
            return "Должен быть один из типов: " + enumValues;
        }

        return "Некорректное значение";

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
