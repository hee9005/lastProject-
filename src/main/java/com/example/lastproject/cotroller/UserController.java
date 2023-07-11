//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.cotroller;

import com.example.lastproject.exception.AlreadyVerifedException;
import com.example.lastproject.exception.ExistUserEmailException;
import com.example.lastproject.exception.NotExistCodeException;
import com.example.lastproject.exception.verifyCodeException;
import com.example.lastproject.model.dto.request.emailCertifed;
import com.example.lastproject.model.dto.request.emaliEequest;
import com.example.lastproject.model.dto.request.joinRequest;
import com.example.lastproject.model.dto.request.loginRequest;
import com.example.lastproject.model.dto.request.passwordRequest;
import com.example.lastproject.model.dto.response.ValidateUserResponse;
import com.example.lastproject.model.dto.response.VerifyEmailResponse;
import com.example.lastproject.service.JwtService;
import com.example.lastproject.service.UserService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/user"})
@Slf4j
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;


    /**인증코드 발급*/
    @PostMapping({"/free/authentication-Code"})
    public ResponseEntity<VerifyEmailResponse> authenticationCode(emaliEequest req) throws AlreadyVerifedException, MessagingException {
        userService.createCode(req);
        VerifyEmailResponse response = new VerifyEmailResponse(200, "인증코드가 발송되었습니다 이메일을 확인하세요");
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    /**인증코드 확인*/
    @PatchMapping({"/free/mail-certifed"})
    public ResponseEntity<VerifyEmailResponse> emailCertifed(emailCertifed req) throws NotExistCodeException, verifyCodeException {
      userService.emailCertifed(req);
        VerifyEmailResponse response = new VerifyEmailResponse(200, "인증완료되었습니다.");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**회원가입*/
    @PostMapping({"/free/join"})
    public ResponseEntity<?> userJoinHandle(joinRequest req) throws ExistUserEmailException, AlreadyVerifedException, verifyCodeException {
        userService.createUser(req);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**로그인*/
    @PostMapping({"/free/login"})
    public ResponseEntity<?> userLoginHandle(loginRequest req) throws verifyCodeException {
        this.userService.loginHandle(req);
        String token = jwtService.createToken(req.getEmail());
        ValidateUserResponse response = new ValidateUserResponse(200, token, req.getEmail());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**유저삭제 (후에 추가 작업필요)*/
    @DeleteMapping("/delete/{password}")
    public ResponseEntity<Void> userDeleteHandle(@AuthenticationPrincipal String principal, passwordRequest req) throws verifyCodeException {
        userService.userDelete(principal, req);
        return new ResponseEntity(HttpStatus.OK);
    }
}
