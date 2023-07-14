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
import lombok.*;

@Table(name = "replys")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**게시판 id*/
    @JsonIgnoreProperties("boardId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private Board boardId;

    /**댓글 내용*/
    private String comment;

    /**댓글 작성자*/
    @JsonIgnoreProperties("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private User userId;




    public void Reply(Reply reply) {
        this.boardId= reply.getBoardId();
        this.userId= reply.getUserId();
        this.comment = reply.getComment();
        this.id = reply.getId();
    }


}
