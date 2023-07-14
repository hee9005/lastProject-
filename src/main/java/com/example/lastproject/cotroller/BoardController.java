//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.cotroller;

import com.example.lastproject.exception.verifyCodeException;
import com.example.lastproject.model.dto.boardWrapper;
import com.example.lastproject.model.dto.boardsWrapper;
import com.example.lastproject.model.dto.request.*;
import com.example.lastproject.model.dto.response.BoardListResponse;
import com.example.lastproject.model.entity.Reply;
import com.example.lastproject.service.BoardService;


import com.example.lastproject.service.ReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping({"/board"})
@Slf4j
public class BoardController {
    @Autowired
    BoardService boardService;

    @Autowired
    ReplyService replyService;
    /**게시글 작성*/
    @PostMapping({"/private/create"})
    public ResponseEntity<?> createBoard(@AuthenticationPrincipal String principal, BoardRequest req) throws IOException {
        boolean ref =boardService.createBoard(principal, req);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**게시글 수정*/
    @PatchMapping("/private/patch/{password}")
    public ResponseEntity<?> boardArraches(@AuthenticationPrincipal String principal, boardArrachesRequest req, passwordRequest pass) throws IOException {
        boolean ref =boardService.boardArraches(principal, req,pass);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**게시글 삭제*/
    @DeleteMapping("/private/delete/{password}")
    public ResponseEntity<?> boardDelete(@AuthenticationPrincipal String principal, passwordRequest pass, boardIdRequest req){
        boardService.boardDeleteHandle(principal,pass,req);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**게시글 전체 가져오기*/
    @GetMapping("/boardAll")
    public ResponseEntity<?> readBoardAllHandle(@RequestParam(defaultValue = "1") int page){
        long total =boardService.totalCount();
        List<boardsWrapper> boardList = boardService.pageload(page);
        BoardListResponse response = new BoardListResponse(total, boardList);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**특정 카테고리글 전체가져오기*/
    @GetMapping("/category/{category}")
    public ResponseEntity<?> readCategoryBoard(categoryRequest req,@RequestParam(defaultValue = "1") int page){
        long total =boardService.categoryTotalCount(req);
        List<boardsWrapper> boardList =  boardService.readCategoryBoard(req,page);
        BoardListResponse response = new BoardListResponse(total, boardList);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**게시판 상세보기*/
    @GetMapping("/detail/{boardId}")
    public ResponseEntity<?> derailPageHandle(@AuthenticationPrincipal String principal,boardIdRequest req) throws verifyCodeException {
        log.info("boardIdCon = {}"+req);
        List<Reply> replts = replyService.findbyReply(req);
        List<boardWrapper> boardList =   boardService.detailPage(principal,req,replts);

        return new ResponseEntity<>(boardList,HttpStatus.OK);
    }
}
