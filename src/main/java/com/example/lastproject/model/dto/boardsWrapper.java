package com.example.lastproject.model.dto;

import com.example.lastproject.model.entity.Board;
import com.example.lastproject.model.entity.User;
import lombok.Data;

/**게시판 글목록 가져오기*/
@Data
public class boardsWrapper {
    public boardsWrapper(Board board) {
        this.id = board.getId();
        this.category=board.getCategory();
        this.postTitle=board.getPostTitle();
        this.description=board.getDescription();
        this.Writer=board.getWriter();
        this.boardDate= String.valueOf(board.getBoardDate());
    }

    private Integer id;

    private String category;

    private String postTitle;

    private String description;

    private User Writer;

    private String boardDate;
}
