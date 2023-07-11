//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.lastproject.config;

import com.example.lastproject.config.support.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor

public class SecurityConfiguration {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain finalAppSecurityChain(HttpSecurity http) throws Exception {

        http.csrf(t -> t.disable());
        http.sessionManagement(t -> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 경로에 따른 인증이 필요한가.. 아닌가 설정 하는 작업
        http.authorizeHttpRequests(t -> t //
                .requestMatchers("/user/private/**").authenticated() //
                .requestMatchers(HttpMethod.POST,"/board/private/create").authenticated()
                .anyRequest().permitAll());



        http.anonymous(t -> t.disable());
        http.logout(t -> t.disable());
        http.addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class);


        return http.build();
    }
}