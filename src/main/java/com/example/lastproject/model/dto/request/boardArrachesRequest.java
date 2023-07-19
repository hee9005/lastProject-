package com.example.lastproject.model.dto.request;

import com.example.lastproject.model.entity.Board;
import com.example.lastproject.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class boardArrachesRequest {
    private Board boardId;
    private String category;
    private String postTitle;
    private String description;
    private List<MultipartFile> arraches;
    private  String boardRoles;

}
