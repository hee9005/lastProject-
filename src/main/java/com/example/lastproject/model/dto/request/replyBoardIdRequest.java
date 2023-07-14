package com.example.lastproject.model.dto.request;

import com.example.lastproject.model.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class replyBoardIdRequest {

    private Board boardId;
}
