//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.model.dto.response;

import com.example.lastproject.model.entity.VerificationCode;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerificationCodeResponseDate {
    private Integer id;
    private String code;
    private String email;
    private String created;
    private String state;

    public VerificationCodeResponseDate(VerificationCode saved) {
        LocalDateTime now = LocalDateTime.now();
        this.id = saved.getId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.code = saved.getCode();
        this.email = saved.getEmail();
        this.created = saved.getCreated().format(formatter);
        this.state = saved.getState();
    }



}
