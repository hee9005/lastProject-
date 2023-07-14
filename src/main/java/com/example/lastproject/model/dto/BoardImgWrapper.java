package com.example.lastproject.model.dto;

import com.example.lastproject.model.entity.BoardImg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**게시글 이미지 가져오기*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardImgWrapper {
    private Integer id;
    private String type;
    private String mediaUrl;

    public BoardImgWrapper(BoardImg boardImg) {
        this.id = boardImg.getId();
        this.type = boardImg.getType();
        this.mediaUrl = boardImg.getMediaUrl();
    }
}
