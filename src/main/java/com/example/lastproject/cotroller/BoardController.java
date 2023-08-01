//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.cotroller;

import com.example.lastproject.exception.*;
import com.example.lastproject.model.dto.BoardDetaitWrapper;
import com.example.lastproject.model.dto.boardsWrapper;
import com.example.lastproject.model.dto.request.*;
import com.example.lastproject.model.dto.response.BoardListResponse;
import com.example.lastproject.model.dto.response.ErrorResponse;
import com.example.lastproject.model.dto.response.passwordRequest;
import com.example.lastproject.model.entity.Reply;
import com.example.lastproject.service.BoardService;


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

import java.io.IOException;
import java.util.List;

@Tag(name = "게시글" , description = "게시글들에 관련된 api입니다.")
@RestController
@RequestMapping({"/board"})
@Slf4j
public class BoardController {
    @Autowired
    BoardService boardService;

    @Autowired
    ReplyService replyService;

    /**게시글 작성*/
    @Operation(summary = "게시글 작성 메서드" , description = "게시글 작성 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201" , description = "게시글을 작성했을때 나오는 메세지입니다."),

            @ApiResponse(responseCode = "400" , description = "게시글을 작성하기 위한 필수 값들이 빠져있을때 나오는 에러입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping({"/private/create"})
    public ResponseEntity<?> createBoard(@AuthenticationPrincipal String principal, BoardRequest req) throws IOException, boardsExcpiton {
        boolean ref =boardService.createBoard(principal, req);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**게시글 수정*/
    @Operation(summary = "게시글 수정 메서드" , description = "게시글 수정 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "게시글을 수정했을때 나오는 메세지입니다."),
            @ApiResponse(responseCode = "400" , description = "삭제된 게시글이거나 게시글을작성한 사용자가 아닐때 나오는 에러입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/private/patch")
    public ResponseEntity<?> boardArraches(@AuthenticationPrincipal String principal, boardArrachesRequest req) throws IOException, NotBoardException, notCreateUserBoard {
        boolean ref =boardService.boardArraches(principal, req);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**게시글 삭제*/
    @Operation(summary = "게시글 삭제 메서드" , description = "게시글 삭제 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "게시글을 삭제했을때 나오는 메세지입니다."),
            @ApiResponse(responseCode = "400" , description = "삭제된 게시글이거나 게시글을 작성한 사용자가 아닐때 나오는 에러입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/private/delete/{password}")
    public ResponseEntity<?> boardDelete(@AuthenticationPrincipal String principal, passwordRequest pass, boardIdRequest req) throws NotBoardException, notCreateUserBoard {
        boardService.boardDeleteHandle(principal,pass,req);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**게시글 전체 가져오기*/
    @Operation(summary = "게시글 목록 불러오는 메서드" , description = "게시글 목록 불러오는 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "게시글 목록을 정상적으로 가져왔을때 나오는 메세지입니다."),
    })
    @GetMapping("/boardAll")
    public ResponseEntity<?> readBoardAllHandle(@RequestParam(defaultValue = "1") int page){
        long total =boardService.totalCount();
        List<boardsWrapper> boardList = boardService.pageload(page);
        BoardListResponse response = new BoardListResponse(total, boardList);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**특정 카테고리글 목록가져오기*/
    @Operation(summary = "특정 카테고리 목로 가져오기 메서드" , description = "특정 카테고리로 작성된 게시글들의 목록을 가져오는  메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "특정 카테고리로 작성된 게시글들의 목록을 가져왔을때 나오는 메세지입니다.")
    })
    @GetMapping("/categorys")
    public ResponseEntity<?> readCategoryBoards(@RequestParam("category") String categorys) {
        categoryRequest req = new categoryRequest(categorys);
        long total = boardService.categoryTotalCount(req);
        List<boardsWrapper> boardList = boardService.readCategoryBoard(req);
        BoardListResponse response = new BoardListResponse(total, boardList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**게시판 상세보기*/
    @Operation(summary = "게시글 상세보기 메서드" , description = "게시글 상세페이지 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "상세보기 페이지를 불러왔을때 나오는 메세지입니다."),
            @ApiResponse(responseCode = "403" , description = "게시글을 볼수있는 권한이 없을때 나오는 에러입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/detail/{boardId}")
    public ResponseEntity<?> derailPageHandle(@AuthenticationPrincipal String principal,detailId req) throws verifyCodeException, NoPermissionException {

        List<BoardDetaitWrapper> boardList =boardService.detailPage(principal,req);

        return new ResponseEntity<>(boardList,HttpStatus.OK);
    }
}
