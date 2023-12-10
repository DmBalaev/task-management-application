package com.dm.taskapp.exceptions;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@RequiredArgsConstructor
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException{
    private final String message;
}
