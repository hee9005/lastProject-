package com.example.lastproject.model.dto.request;

import com.example.lastproject.model.entity.Board;
import com.example.lastproject.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**엔티티 대신 reply값 받아주는 용도*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplysRequest {

    private Board boardId;

    private User userId;

    private String comment;

    public ReplysRequest(ReplysRequest req) {
        this.boardId = req.getBoardId();
        this.userId = req.getUserId();
        this.comment = req.comment;
    }
}
