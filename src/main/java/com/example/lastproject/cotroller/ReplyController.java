package com.example.lastproject.cotroller;

import com.example.lastproject.exception.NotBoardException;
import com.example.lastproject.exception.ReplyUserdException;
import com.example.lastproject.model.dto.request.*;
import com.example.lastproject.model.dto.response.ErrorResponse;
import com.example.lastproject.model.dto.response.VerifyEmailCodeResponse;
import com.example.lastproject.repository.ReplyRepository;
import com.example.lastproject.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "댓글" , description = "댓들에 관련된 api입니다.")
@RestController
@RequestMapping({"/reply"})
@Slf4j
public class ReplyController {


    @Autowired
    ReplyService replyService;

    /**댓글 작성*/
    @Operation(summary = "댓글작성 메서드" , description = "댓글작성 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201" , description = "게시글에 정삭적으로 댓글이 달렸을때 나오는 메세지입니다."),
            @ApiResponse(responseCode = "400" , description = "삭제된 게시글에 댓글을 작성하면 나오는 에러입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/create/{boardId}")
    public ResponseEntity<?> replyCreateHandle(boardIdRequest req, @AuthenticationPrincipal String principal, replyRequest replyReq) throws ReplyUserdException, NotBoardException {
        log.warn("boardId=",String.valueOf(req.getBoardId()));

        replyService.replyCreate(req, principal, replyReq);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**댓글 삭제*/
    @Operation(summary = "댓글삭제 메서드" , description = "댓글삭제 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "삭제되었을때 나오는 메세지입니다."),

            @ApiResponse(responseCode = "400" , description = "삭제할려는 댓글의 작성자가 아니거나 admin이상의 권한을 가진 유저가 아닐때 나오는 에러입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/delete/{replyId}")
    public ResponseEntity<?> replyDeleteHandle (@AuthenticationPrincipal String principal, replyDeleteRequest req) throws ReplyUserdException {
        replyService.replyDelete(principal,req);

        return new ResponseEntity(HttpStatus.OK);
    }

    /**댓글 수정*/
    @Operation(summary = "댓글수정 메서드" , description = "댓글수정 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "댓글이 수정되었을때 나오는 메세지입니다."),

            @ApiResponse(responseCode = "400" , description = "수정할려는 댓글의 작성자가 아니때 나오는 에러입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/patch")
    public ResponseEntity<?> replyPatchhandle(@AuthenticationPrincipal String principal, replyPacthRequest req) throws ReplyUserdException {
        replyService.replyPatch(principal,req);

        return new ResponseEntity(HttpStatus.OK);
    }
}
