//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.lastproject.exception.notJoinUserException;
import com.example.lastproject.model.entity.User;
import com.example.lastproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    @Value("${jwt.secret.key}")
    String secretKey;

    @Autowired
    UserRepository userRepository;


    @Transactional
    public String createToken(String email) throws notJoinUserException {
        User user =userRepository.findByEmail(email);
        if(user == null){
            throw new notJoinUserException("등록된 계정이 없습니다. 회원가입을 먼저 해주세요");
        }
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create().withIssuer("lastProject").
                withClaim("email", email).
                withClaim("roles", "basic").
                withIssuedAt(new Date(System.currentTimeMillis())).
                withExpiresAt(new Date(System.currentTimeMillis() + 1800000L)).
                sign(algorithm);
    }

    @Transactional
    public String verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        log.info(decodedJWT.getIssuer());
        log.info(decodedJWT.getClaim("email").asString());
        return decodedJWT.getClaim("email").asString();
    }
}
