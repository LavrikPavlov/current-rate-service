package ru.kazan.currencyrateservice.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        //todo: переписать когда-нибудь более качествено
        var errorResponse = new ErrorResponse();

        errorResponse.setCode(getHttpStatusCode(ex));
        errorResponse.setMessage(ex.getMessage());

        if (ex instanceof MethodArgumentTypeMismatchException) {
            errorResponse.setSimpleMessage("Неверный тип аргумента в запросе.");
        } else if (ex instanceof MethodArgumentNotValidException) {
            errorResponse.setSimpleMessage("Ошибка валидации данных.");
        } else if (ex instanceof ConstraintViolationException) {
            errorResponse.setSimpleMessage("Ошибка с ограничениями данных.");
        } else if (ex instanceof IllegalArgumentException) {
            errorResponse.setSimpleMessage("Некорректный аргумент.");
        } else if (ex instanceof NullPointerException) {
            errorResponse.setSimpleMessage("Произошла ошибка из-за отсутствия значения (null).");
        } else if (ex instanceof MissingRequestHeaderException) {
            errorResponse.setSimpleMessage("Отсутствует обязательный заголовок запроса.");
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private int getHttpStatusCode(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException ||
                ex instanceof ConstraintViolationException ||
                ex instanceof MethodArgumentTypeMismatchException) {
            return 400;
        } else if (ex instanceof IllegalArgumentException) {
            return 400;
        } else if (ex instanceof NullPointerException) {
            return 500;
        } else if (ex instanceof MissingRequestHeaderException) {
            return 400;
        } else {
            return 500;
        }
    }
}
