//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "verificationCodes")
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    /**인증 코드*/
    private String code;
    /**인증메일*/
    private String email;
    /**인증 상태*/
    private String state;
    /**발급 날짜*/
    private LocalDateTime created;

    public VerificationCode(String code, String email, String state) {
        this.code = code;
        this.email = email;
        this.state = state;
    }

    @PrePersist
    public void prePersist() {created = LocalDateTime.now();
    }




}
