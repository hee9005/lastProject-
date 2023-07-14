//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.model.dto;

import com.example.lastproject.model.entity.User;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**유저 생성*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWrapper {
    private String email;
    private String password;
    private @NotNull String name;
    private String profilelmage;
    private @NotNull LocalDateTime joinDate;
    private String social;
    private String roles;

    public UserWrapper(User req) {
        this.email = req.getEmail();
        this.password = req.getPassword();
        this.name = req.getName();
        this.roles = "basic";
    }

    @PrePersist
    public void prePersist() {
        this.joinDate = LocalDateTime.now();
    }

}
