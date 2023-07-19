package com.example.lastproject.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.example.lastproject.model.dto.ReplyWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReplyListResponse {
    private Long total;

    private List<ReplyWrapper> replys;
}
