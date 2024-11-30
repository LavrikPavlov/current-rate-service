package ru.kazan.currencyrateservice.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.kazan.api.generated.model.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            IllegalArgumentException.class,
            NullPointerException.class,
            MissingRequestHeaderException.class
    })
    public ResponseEntity<ErrorResponse> catchMethodArgumentTypeMismatchException(HttpServletRequest request) {
        var exception = new ErrorResponse();
        exception.setCode(500);
        exception.setMessage("exception");
        return ResponseEntity.badRequest().body(exception);
    }
}
