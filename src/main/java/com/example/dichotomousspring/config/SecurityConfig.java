package com.example.dichotomousspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // .antMatchers and mvcMatchers were both deprecated
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/keys").authenticated()
                .anyRequest().permitAll()
                .and()
                .cors(Customizer.withDefaults())
                .oauth2ResourceServer(Customizer.withDefaults());
        return http.build();
    }

//    Over Spring Boot 2.7 so can skip the custom JWT decoder because it supports audience validation out of the box
}
