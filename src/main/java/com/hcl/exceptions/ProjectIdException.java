package com.hcl.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) //make sure whenever I throw this exception, it throws a bad request code
public class ProjectIdException extends RuntimeException {

    public ProjectIdException(String message) {
        super(message);
    }

}
