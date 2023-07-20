package com.acl.dronedispatcher.controller.advice;

import com.acl.dronedispatcher.service.exception.ClientException;
import com.acl.dronedispatcher.service.exception.ErrorResponse;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    private static final String ERROR_MESSAGE = "[error message] {}";
    private static final String BAD_REQUEST = "Bad request exception";

    /**
     * This method is used to get only one ErrorResponse.
     * @param exception This is the first paramter to method.
     * @return one ErrorResponse.
     */
    @ExceptionHandler(value = {ServerWebInputException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWebInputException(ServerWebInputException exception) {
        log.error(ERROR_MESSAGE, exception.getMessage(), exception);
        return ErrorResponse
                .builder()
                .message(exception.getMessage())
                .build();
    }

    /**
     * This method is used to get only one ErrorResponse.
     * @param exception This is the first paramter to method.
     * @return one ErrorResponse.
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error(ERROR_MESSAGE, exception.getMessage(), exception);
        return ErrorResponse
                .builder()
                .message(BAD_REQUEST)
                .errors(exception.getFieldErrors().stream()
                        .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * This method is used to get only one ErrorResponse.
     * @param exception This is the first paramter to method.
     * @return one ErrorResponse.
     */
    @ExceptionHandler(value = {WebExchangeBindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWebExchangeBindException(WebExchangeBindException exception) {
        log.error(ERROR_MESSAGE, exception.getMessage(), exception);
        return ErrorResponse
                .builder()
                .errors(exception.getFieldErrors().stream()
                        .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Business exception handler.
     * @param exception Exception thrown in business layer.
     * @return ErrorResponse
     */
    @ExceptionHandler(value = ClientException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(ClientException exception) {
        log.error(ERROR_MESSAGE, exception.getMessage(), exception);
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(exception.getMessage()).build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Contraint exception handler.
     * @param exception ConstraintViolationException thrown in controller layer.
     * @return ErrorResponse
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleConstraintException(ConstraintViolationException exception) {
        log.error(ERROR_MESSAGE, exception.getMessage(), exception);
        var exceptionMessages = exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(BAD_REQUEST)
                .errors(exceptionMessages)
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * This method is used to get Generic Error.
     * @param exception This is the first paramter to method.
     * @return one ErrorResponse.
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericError(Exception exception) {
        log.error(ERROR_MESSAGE, exception.getMessage(), exception);
        return ErrorResponse
                .builder()
                .message(exception.getMessage())
                .build();
    }
}
