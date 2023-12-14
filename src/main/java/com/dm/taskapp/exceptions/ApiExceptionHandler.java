package com.dm.taskapp.exceptions;

import com.dm.taskapp.app.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ApiResponse> catchResourceNotFoundException(ResourceNotFound e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InsufficientPermissionsException.class)
    public ResponseEntity<ApiResponse> catchInsufficientPermissionsException(InsufficientPermissionsException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> catchApiException(ApiException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> catchIllegalArgumentException(IllegalArgumentException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> catchMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
