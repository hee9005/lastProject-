//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "boardImgs")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardImg {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**게시글 id*/
    @JsonIgnoreProperties("boardId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private Board boardId;

    /**이미지 타입*/
    private String type;
    /**이미지 저장 주소*/
    private String mediaUrl;

}
