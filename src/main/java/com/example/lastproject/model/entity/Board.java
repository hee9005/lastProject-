//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boatds")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    /**게시글 카테고리*/
    private @NotNull String category;
    /**게시글 제목*/
    private @NotNull String postTitle;
    /**게시글내용*/
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    /**게시글 작성자id*/
    @JoinColumn(name = "Writer")
    private @NotNull User Writer;
    /**게시글 작성날짜*/
    private LocalDateTime boardDate;
    /**게시글 이미지*/
    @OneToMany(mappedBy = "boardId", fetch = FetchType.LAZY)
    private List<BoardImg> boardImg = new ArrayList();
    /**댓글*/
    @OneToMany(mappedBy = "boardId", fetch = FetchType.LAZY)
    private List<Reply> replyList;
    /**추천*/
    @OneToMany(mappedBy = "boardId", fetch = FetchType.LAZY)
    private List<BoardGood> boardGoods;

    @PrePersist
    public void prePersist() {
        this.boardDate = LocalDateTime.now();
    }

}
