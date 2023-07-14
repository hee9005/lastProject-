package com.example.lastproject.model.dto;

import com.example.lastproject.model.entity.BoardImg;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class boardImgWrapper {
    public boardImgWrapper(BoardImg t) {
        this.type = t.getType();
        this.mediaUrl = t.getMediaUrl();
    }
    private Long id;

    private String type;

    private String mediaUrl;
}
