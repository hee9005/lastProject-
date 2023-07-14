package com.example.lastproject.model.dto;

import com.example.lastproject.model.entity.Reply;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplyWrapper {
    public ReplyWrapper(Reply e) {
        this.Writer = String.valueOf(e.getUserId().getId());
        this.boardId= String.valueOf(e.getBoardId().getId());
        this.comment=e.getComment();
        this.replyId = e.getId();
    }

    private  Integer replyId;
    private String Writer;

    private String boardId;

    private String comment;
}
