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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "nestedreplys")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NestedrReply {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**댓글 id*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replyId")
    private Reply replyId;

    /**대댓글 작성자*/
    @JoinColumn(name = "nestedrReplyWriter")
    @ManyToOne(fetch = FetchType.LAZY)
    private User userId;
    /**대댓글 내용*/
    private String nestedrReplyContent;
    /**대댓글 작성 시간*/
    private LocalDateTime nestedrReplydate;

}
