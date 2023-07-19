//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.config;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.lastproject.exception.*;
import com.example.lastproject.model.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerConfiguration {

    @ExceptionHandler({NoPermissionException.class})
    public ResponseEntity<ErrorResponse> NoPermissionException(NoPermissionException ex) {
        ErrorResponse response = new ErrorResponse(403, ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler({notCreateUserBoard.class})
    public ResponseEntity<ErrorResponse> notCreateUserBoardException(notCreateUserBoard ex) {
        ErrorResponse response = new ErrorResponse(400, ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({UserPassedException.class})
    public ResponseEntity<ErrorResponse> UserPassedException(UserPassedException ex) {
        ErrorResponse response = new ErrorResponse(400, ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler({ UnauthorizedException.class})
    public ResponseEntity<ErrorResponse>  UnauthorizedExceptionHabdle( UnauthorizedException e) {
        ErrorResponse response = new ErrorResponse(401, e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
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

    @ExceptionHandler({NotBoardException.class})
    public ResponseEntity<ErrorResponse> NotBoardException(NotBoardException ex){
        ErrorResponse response = new ErrorResponse(400, ex.getMessage(), System.currentTimeMillis());
        return  new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({ReplyUserdException.class})
    public ResponseEntity<ErrorResponse> exceptionHandle(ReplyUserdException ex){
        ErrorResponse response = new ErrorResponse(400, ex.getMessage(), System.currentTimeMillis());
        return  new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({notJoinUserException.class})
    public ResponseEntity<ErrorResponse> notJoinUserExceptionHandle(notJoinUserException ex){
        ErrorResponse response = new ErrorResponse(400, ex.getMessage(), System.currentTimeMillis());
        return  new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({NotPassWordException.class})
    public ResponseEntity<ErrorResponse> NotPassWordExceptionExceptionHandle(NotPassWordException ex){
        ErrorResponse response = new ErrorResponse(400, ex.getMessage(), System.currentTimeMillis());
        return  new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({boardsExcpiton.class})
    public ResponseEntity<ErrorResponse> boardsExcpitonHandle(boardsExcpiton ex){
        ErrorResponse response = new ErrorResponse(400, ex.getMessage(), System.currentTimeMillis());
        return  new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}


