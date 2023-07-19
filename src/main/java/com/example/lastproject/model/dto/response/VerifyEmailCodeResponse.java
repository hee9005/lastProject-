package com.example.lastproject.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyEmailCodeResponse {
    private int code;
    private String message;

    private String emailCode;
}
