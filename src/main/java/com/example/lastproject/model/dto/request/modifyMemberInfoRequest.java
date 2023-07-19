package com.example.lastproject.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class modifyMemberInfoRequest {
    private String name;

    private MultipartFile profilelmage;

}
