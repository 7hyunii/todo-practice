package me.seonghyeon.todopractice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTodoNotFoundException(TodoNotFoundException e) {
        ErrorCode errorCode = ErrorCode.TODO_NOT_FOUND;

        ErrorResponse response = new ErrorResponse(errorCode.name(), e.getMessage());

        return ResponseEntity.status(errorCode.getStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;

        FieldError fieldError = e.getBindingResult().getFieldError();
        ErrorResponse response = new ErrorResponse(errorCode.name(), fieldError.getDefaultMessage());

        return ResponseEntity.status(errorCode.getStatus())
                .body(response);
    }

}
