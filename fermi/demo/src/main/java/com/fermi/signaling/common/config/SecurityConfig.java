package com.fermi.signaling.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 지금은 인증 없이 개발하니까 전부 허용
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // 개발 단계에선 CSRF 때문에 POST가 403 나기 쉬움 -> 끄기
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}