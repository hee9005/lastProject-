//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.repository;

import com.example.lastproject.model.entity.VerificationCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {
    Optional<VerificationCode> findTop1ByEmailOrderByCreatedDesc(String email);

    void deleteByEmail(String email);
}
