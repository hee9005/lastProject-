package com.example.lastproject.model.dto;

import com.example.lastproject.model.entity.Reply;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyWrapper {
    private Integer id;
    private String comment;
    private UserWrapper user; // User 정보를 포함하는 래퍼 클래스

    public ReplyWrapper(Reply reply) {
        this.id = reply.getId();
        this.comment = reply.getComment();
        this.user = new UserWrapper(reply.getUserId()); // User 정보 래핑
    }
}
