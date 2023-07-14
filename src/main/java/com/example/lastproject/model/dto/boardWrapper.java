package com.example.lastproject.model.dto;

import com.example.lastproject.model.entity.Board;
import com.example.lastproject.model.entity.BoardImg;
import com.example.lastproject.model.entity.Reply;
import com.example.lastproject.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class boardWrapper {
    public boardWrapper(Board boards) {
        this.id = boards.getId();
        this.category=boards.getCategory();
        this.postTitle=boards.getPostTitle();
        this.description=boards.getDescription();
        this.Writer=boards.getWriter();
        this.boardDate= String.valueOf(boards.getBoardDate());
        this.replyList = boards.getReplyList().stream().map(e-> new ReplyWrapper(e)).toList();
        this.boardImg = boards.getBoardImg().stream().map(e -> new boardImgWrapper(e)).toList();
        //댓글 대댓글 구현하면 추가로작성
    }

    private Integer id;

    private String category;

    private String postTitle;

    private String description;

    private User Writer;

    private String boardDate;

    private List<ReplyWrapper> replyList;

    private List<boardImgWrapper> boardImg;
}
