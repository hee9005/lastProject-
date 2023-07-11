//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.cotroller;

import com.example.lastproject.model.dto.request.BoardRequest;
import com.example.lastproject.model.dto.request.boardArrachesRequest;
import com.example.lastproject.model.dto.request.boardIdRequest;
import com.example.lastproject.model.dto.request.passwordRequest;
import com.example.lastproject.service.BoardService;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping({"/board"})
@Slf4j
public class BoardController {
    @Autowired
    BoardService boardService;


    /**게시글 작성*/
    @PostMapping({"/private/create"})
    public ResponseEntity<?> createBoard(@AuthenticationPrincipal String principal, BoardRequest req) throws IOException {
        boolean ref =boardService.createBoard(principal, req);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

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
}
