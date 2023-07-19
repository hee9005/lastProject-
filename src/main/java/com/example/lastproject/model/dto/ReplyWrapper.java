package com.example.lastproject.model.dto;

import com.example.lastproject.model.entity.Board;
import com.example.lastproject.model.entity.Reply;
import com.example.lastproject.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyWrapper {
    private Integer id;
    private String comment;
    private Integer boardId; // 대신에 ID 값만 포함
    private Integer userId; // 대신에 ID 값만 포함

    public ReplyWrapper(Reply reply) {
        this.id = reply.getId();
        this.comment = reply.getComment();
        this.userId = reply.getUser().getId(); // User의 ID 값만 저장
        this.boardId = reply.getBoard().getId(); // Board의 ID 값만 저장
    }
}

