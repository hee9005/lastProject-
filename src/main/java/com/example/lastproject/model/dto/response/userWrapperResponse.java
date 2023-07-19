package com.example.lastproject.model.dto.response;

import com.example.lastproject.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class userWrapperResponse {
    private int code;
    private String message;


    private String name;

    private String img;
}
