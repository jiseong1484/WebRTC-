package com.fermi.signaling.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        // 개발용: localhost 및 같은 와이파이의 사설 IP(주로 10/8, 172.16/12, 192.168/16)에서 열린 프론트(8000) 허용
                        .allowedOriginPatterns(
                                "http://localhost:8000",
                                "http://127.0.0.1:8000",
                                "http://192.168.*.*:8000",
                                "http://10.*.*.*:8000",
                                "http://172.*.*.*:8000"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}