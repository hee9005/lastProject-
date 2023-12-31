//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    /**가입 이메일*/
    @Column(name = "userId")
    private @NotNull @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}$") String email;
    /**비번*/
    private String password;
    /**닉네임*/
    private @NotNull String name;
    /**프로필 이미지*/
    private String profilelmage;

    /**가입 날짜*/
    private @NotNull LocalDateTime joinDate;
    /**소셜*/
    private String social;
    /**권한*/
    private @NotNull String roles;
    /**작성 게실글목록*/
    @OneToMany(mappedBy = "writer", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Board> boards;
    /**실시간채팅*/
    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<chat> chatList;
    /**댓글 리스트*/
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Reply> replyList;
    @PrePersist
    public void prePersist() {
        this.joinDate = LocalDateTime.now();
    }

}
