package com.example.lastproject.cotroller;

import com.example.lastproject.exception.ReplyUserdException;
import com.example.lastproject.model.dto.request.NestedrReplyBoardId;
import com.example.lastproject.model.dto.request.NestedrReplyModifyRequest;
import com.example.lastproject.model.dto.request.NestedrReplyRequest;
import com.example.lastproject.model.dto.response.ErrorResponse;
import com.example.lastproject.repository.NestedrReplyRepository;
import com.example.lastproject.service.NestedrReplyService;
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

@Tag(name = "대댓글" , description = "대댓들에 관련된 api입니다.")
@RestController
@RequestMapping({"/NestedrReply"})
@Slf4j
public class NestedrReplyController {

    @Autowired
    NestedrReplyService nestedrReplyService;


    /**대댓글 작성*/
    @Operation(summary = "대댓글 작성 메서드" , description = "대댓글 작성 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201" , description = "대댓글이 작성되었을때 나오는 메세지입니다."),

            @ApiResponse(responseCode = "400" , description = "댓글이 삭제 되었을때 나오는 에러입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/create/{replyId}")
    public ResponseEntity<?> nestedrReplyCreate(@AuthenticationPrincipal String principal, NestedrReplyRequest req, NestedrReplyBoardId nrb) throws ReplyUserdException {
        nestedrReplyService.nestedrReplyCreate(principal,req,nrb);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**대댓글 수정*/
    @Operation(summary = "대댓글 수정 메서드" , description = "대댓글 수정 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "대댓글이 수정되었을때 나오는 메세지입니다."),

            @ApiResponse(responseCode = "400" , description = "수정할려는 대댓글을 찾을수 없거나 대댓글을 작성한 사용자가 아닐때 나오는 에러입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/modify/{nestedrReply}")
    public ResponseEntity<?> nestedrReplyModify(@AuthenticationPrincipal String principal, NestedrReplyModifyRequest req, NestedrReplyRequest nrb) throws ReplyUserdException {
        nestedrReplyService.modifyHandle(principal,req,nrb);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**대댓글 삭제*/
    @Operation(summary = "대댓글 삭제 메서드" , description = "대댓글 삭제 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "대댓글이 삭제되었을때 나오는 메세지입니다."),

            @ApiResponse(responseCode = "400" , description = "삭제할려는 대댓글을 찾을수 없거나 대댓글을 작성한 사용자가 아닐때 나오는 에러입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/delete/{nestedrReply}")
    public ResponseEntity<?> deleteNestedrReply(@AuthenticationPrincipal String principal, NestedrReplyModifyRequest req) throws ReplyUserdException {
        nestedrReplyService.deleteHandle(req,principal);

        return new ResponseEntity(HttpStatus.OK);
    }
}
