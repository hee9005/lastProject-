//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "boards")
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

    @NotNull
    /**게시판 열람등급*/
    private String boardRoles;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne()
    /**게시글 작성자id*/
    @JoinColumn(name = "writer")
    @NotNull
    @JsonManagedReference
    private  User writer;
    @NotNull
    /**게시글 작성날짜*/
    private LocalDateTime boardDate;
    /**게시글 이미지*/
    @OneToMany(mappedBy = "boardId")
    @JsonIgnore
    @JsonBackReference
    private List<BoardImg> boardImg;

    /**댓글*/
    @OneToMany(mappedBy = "board")
    @JsonIgnore
    @JsonBackReference
    private List<Reply> replyList;

    /**추천*/
    @OneToMany(mappedBy = "boardId")
    private List<BoardGood> boardGoods;

    @PrePersist
    public void prePersist() {
        this.boardDate = LocalDateTime.now();
    }


}
