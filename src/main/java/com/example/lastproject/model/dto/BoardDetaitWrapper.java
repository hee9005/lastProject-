package com.example.lastproject.model.dto;

import com.example.lastproject.model.entity.Board;
import com.example.lastproject.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**게시판 상세페이지 가죠오기*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDetaitWrapper {
    private Integer id;
    private String category;
    private String postTitle;
    private String description;
    private User writer;
    private String boardDate;
    private List<ReplyWrapper> replyList;
    private List<BoardImgWrapper> boardImg;

    public BoardDetaitWrapper(Board board) {
        this.id = board.getId();
        this.category = board.getCategory();
        this.postTitle = board.getPostTitle();
        this.description = board.getDescription();
        this.writer = board.getWriter();
        this.boardDate = String.valueOf(board.getBoardDate());

        if (board.getReplyList() != null) {
            this.replyList = board.getReplyList().stream().map(ReplyWrapper::new).toList();
        }

        // Modify boardImg mapping
        if (board.getBoardImg() != null) {
            this.boardImg = board.getBoardImg().stream().map(BoardImgWrapper::new).toList();
        }
    }
}
