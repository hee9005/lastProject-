package com.example.lastproject.model.dto.response;

import com.example.lastproject.model.dto.boardsWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BoardListResponse {

    private Long total;

    private List<boardsWrapper> boards;
}
