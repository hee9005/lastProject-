package com.example.lastproject.cotroller;

import com.example.lastproject.exception.ReplyUserdException;
import com.example.lastproject.model.dto.request.*;
import com.example.lastproject.repository.ReplyRepository;
import com.example.lastproject.service.ReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/reply"})
@Slf4j
public class ReplyController {


    @Autowired
    ReplyService replyService;

    /**댓글 작성*/
    @PostMapping("/create/{boardId}")
    public ResponseEntity<?> replyCreateHandle(boardIdRequest req, @AuthenticationPrincipal String principal, replyRequest replyReq) throws ReplyUserdException {
        log.warn("boardId=",String.valueOf(req.getBoardId()));

        replyService.replyCreate(req, principal, replyReq);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**댓글 삭제*/
    @DeleteMapping("/delete/{replyId}")
    public ResponseEntity<?> replyDeleteHandle (@AuthenticationPrincipal String principal, replyDeleteRequest req) throws ReplyUserdException {
        replyService.replyDelete(principal,req);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/patch")
    public ResponseEntity<?> replyPatchhandle(@AuthenticationPrincipal String principal, replyPacthRequest req) throws ReplyUserdException {
        replyService.replyPatch(principal,req);

        return new ResponseEntity(HttpStatus.OK);
    }
}
