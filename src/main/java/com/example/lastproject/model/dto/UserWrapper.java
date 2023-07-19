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

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWrapper extends User{
    private Integer id;
    private String email;
    private String password;
    private @NotNull String name;
    private String profilelmage;
    private @NotNull LocalDateTime joinDate;
    private String social;
    private String roles;




    public UserWrapper(User req) {
        this.id = req.getId();
        this.email = req.getEmail();
        this.password = req.getPassword();
        this.name = req.getName();
        this.profilelmage = req.getProfilelmage();
        this.joinDate = req.getJoinDate();
        this.social = req.getSocial();
        this.roles = req.getRoles();
    }



    @PrePersist
    public void prePersist() {
        this.joinDate = LocalDateTime.now();
    }

}
