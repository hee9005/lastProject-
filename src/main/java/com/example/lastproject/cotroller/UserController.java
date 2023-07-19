//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.cotroller;

import com.example.lastproject.exception.*;
import com.example.lastproject.model.dto.ReplyWrapper;
import com.example.lastproject.model.dto.UserWrapper;
import com.example.lastproject.model.dto.boardsWrapper;
import com.example.lastproject.model.dto.request.*;
import com.example.lastproject.model.dto.response.*;
import com.example.lastproject.model.entity.User;
import com.example.lastproject.service.BoardService;
import com.example.lastproject.service.JwtService;
import com.example.lastproject.service.ReplyService;
import com.example.lastproject.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "사용자" , description = "사용자에 관련된 api입니다.")
@RestController
@RequestMapping({"/user"})
@Slf4j
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;

    @Autowired
    BoardService boardService;

    @Autowired
    ReplyService replyService;

    /**인증코드 발급*/
    @Operation(summary = "인증코드 메서드" , description = "인증코드 발급 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201" , description = "입력한 email로 인증코드가 정상 발급이 되었습니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = VerifyEmailCodeResponse.class))),
            @ApiResponse(responseCode = "400" , description = "입력한 email로 인증을 완료한 이메일입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping({"/free/authentication-Code"})
    public ResponseEntity<VerifyEmailResponse> authenticationCode(emaliRequest req) throws AlreadyVerifedException, MessagingException {
        VerificationCodeResponseDate date =userService.createCode(req);
        VerifyEmailCodeResponse response = new VerifyEmailCodeResponse(201, "인증코드가 발송되었습니다 이메일을 확인하세요",date.getCode());
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    /**인증코드 확인*/
    @Operation(summary = "인증코드확인 메서드" , description = "인증코드 확인 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "발급된 인증코드하고  입력한 코드가 같의면 인증 완료되었습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VerifyEmailResponse.class))),
            @ApiResponse(responseCode = "400" , description = "인증이 완료된 email이거나 인증코드가 틀렸들때 또는  유효시간이 만료되었을때 나오는 에러입니다..",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping({"/free/mail-certifed"})
    public ResponseEntity<VerifyEmailResponse> emailCertifed(emailCertifed req) throws NotExistCodeException, verifyCodeException, UserPassedException {
      userService.emailCertifed(req);
        VerifyEmailResponse response = new VerifyEmailResponse(200, "인증완료되었습니다.");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**회원가입*/
    @Operation(summary = "인증받은 사용자 회원가입" , description = "인증을 받은 email로 회원가입을 하는 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201" , description = "정상적으로 회원가입에 성고하였습니다."),
            @ApiResponse(responseCode = "400" , description = "인증을 받은 기록이 없을때 나오는 에러입니다..",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401" , description = "발급받은 코드가 아직 미인증 상태일때 나오는 에러입니다..",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping({"/free/join"})
    public ResponseEntity<?> userJoinHandle(joinRequest req) throws ExistUserEmailException, AlreadyVerifedException, verifyCodeException, UnauthorizedException {
        userService.createUser(req);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**로그인*/
    @Operation(summary = "로그인" , description = "가입한 아이디로 로그인하는 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "정상적으로 로그인에 성공하고 토큰이 정상 발급되었습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VerifyEmailResponse.class))),
            @ApiResponse(responseCode = "400" , description = "등록된 사용자가 없을때 나오는 에러입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400" , description = "사용자의 비밀번호와 입력한 비밀번호가 같지않을때 나오는 에러입니다..",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping({"/free/login"})
    public ResponseEntity<?> userLoginHandle(loginRequest req) throws verifyCodeException, notJoinUserException, NotPassWordException {
        userService.loginHandle(req);
        String token = jwtService.createToken(req.getEmail());
        ValidateUserResponse response = new ValidateUserResponse(200, token, req.getEmail());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**유저삭제*/
    @Operation(summary = "사용자 삭제" , description = "사용자가 관한 데이터드를 삭제하는 메서드 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "정상적으로  사용자에 관한 정보들이 삭제되었습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VerifyEmailResponse.class))),
            @ApiResponse(responseCode = "400" , description = "사용자의 비밀번호와 입력한 비밀번호가 같지않을때 나오는 에러입니다..",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/delete/{password}")
    public ResponseEntity<Void> userDeleteHandle(@AuthenticationPrincipal String principal, passwordRequest req) throws verifyCodeException, NotPassWordException {
        userService.userDelete(principal, req);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**회원정보 수정*/
    @Operation(summary = "사용자 정보 수정" , description = "사용자의 정보를 수정하는 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "정상적으로 사용자 정보가 변경되었습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = userWrapperResponse.class)))
    })
    @PatchMapping("/modifyMemberInfo")
    public ResponseEntity<UserWrapper> modifyMemberInfo(@AuthenticationPrincipal String principal,modifyMemberInfoRequest req) throws IOException {
        UserWrapper userWrapper = userService.modifyMemberInfo(principal,req);
        userWrapperResponse response = new userWrapperResponse(200, "인증완료되었습니다.", userWrapper.getName(),userWrapper.getProfilelmage());
        return new ResponseEntity(response,HttpStatus.OK);
    }

    /**유저가 작성한 게시글목록 보기*/
    @Operation(summary = "사용자가 작성한 글목록" , description = "사용자가 작성한 게시글 목록 전체를 불러오는 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "정상적으로 사용자 가 작성한 게시글들을 불러왔습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = userWrapperResponse.class))),
            @ApiResponse(responseCode = "400" , description = "등록된 사용자가 없을때 나오는 에러입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/page/posts/{userId}")
    public ResponseEntity<BoardListResponse> userMypagePosts(@AuthenticationPrincipal String principal, UserIdRequest req) throws verifyCodeException, notJoinUserException {
        long total = boardService.boardCount(req);
        List<boardsWrapper> boardList = userService.userPostsPage(req);
        BoardListResponse response = new BoardListResponse(total, boardList);

        return new ResponseEntity<BoardListResponse>(response,HttpStatus.OK);
    }
    /**유저가 작성한 댓글목로 보기*/
    @Operation(summary = "사용자가 작성한 댓글목록" , description = "사용자가 작성한 댓글 목록 전체를 불러오는 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "정상적으로 사용자 가 작성한 댓글들을 불러왔습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = userWrapperResponse.class))),
            @ApiResponse(responseCode = "400" , description = "등록된 사용자가 없을때 나오는 에러입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/page/reply/{userId}")
    public ResponseEntity<ReplyListResponse> userMypageReply(@AuthenticationPrincipal String principal, UserIdRequest req) throws verifyCodeException, JsonProcessingException {
        List<ReplyWrapper> repltList = userService.userReplyPage(req);
        long total = replyService.replyCount(req);
        ReplyListResponse response = new ReplyListResponse(total, repltList);
        return new ResponseEntity<ReplyListResponse>(response,HttpStatus.OK);
    }
}
