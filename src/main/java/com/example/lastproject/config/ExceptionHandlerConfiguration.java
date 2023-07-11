//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.config;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.lastproject.exception.AlreadyVerifedException;
import com.example.lastproject.exception.ExistUserEmailException;
import com.example.lastproject.exception.verifyCodeException;
import com.example.lastproject.model.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerConfiguration {
    public ExceptionHandlerConfiguration() {
    }

    @ExceptionHandler({AlreadyVerifedException.class})
    public ResponseEntity<ErrorResponse> AlreadyVerifedException(Exception ex) {
        ErrorResponse response = new ErrorResponse(400, ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({verifyCodeException.class})
    public ResponseEntity<ErrorResponse> verifyCodeExceptionHabdle(verifyCodeException e) {
        ErrorResponse response = new ErrorResponse(400, e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({JWTDecodeException.class, TokenExpiredException.class})
    public ResponseEntity<ErrorResponse> jwtException(Exception ex) {
        ErrorResponse response = new ErrorResponse(401, "token value is expired or damaged", System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ExistUserEmailException.class})
    public ResponseEntity<Void> exceptionHandle(ExistUserEmailException ex) {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
