package com.example.dichotomousspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        System.out.println("SecurityFilterChain activated");
        http
                .authorizeRequests()
                // .antMatchers and mvcMatchers were both deprecated
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/keys").authenticated()
                .anyRequest().denyAll()
                .and()
                .cors(cors -> cors.configurationSource(corsConfigurationSource))  // Use the custom CorsConfigurationSource
                // https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html#_supplying_audiences
                // The default behavior should validate aud because we specified it in application.properties
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

//    Over Spring Boot 2.7 so can skip the custom JWT decoder because it supports audience validation out of the box

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        System.out.println("Cors Configuration Source activated");

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // or "*" to allow all origins
        configuration.setAllowedMethods(Arrays.asList("GET", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // or specify headers

        // allows you to provide different cors configurations for different url patterns. Not currently using
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

//        return request -> {
//            System.out.println("Handling CORS for: " + request.toString());
//            return configuration;
//        };

        return source;
    }

    // oauth2ResourceServer() Http builder method that takes a oauth2ResourceServerCustomizer() –
    // the Customizer to provide more options for the OAuth2ResourceServerConfigurer and returns the httpsecurity object for further customization
//    OAuth2ResourceServerConfigurer - has a jwt() method that takes a jwtCustomizer argument, which
//    configures a BearerTokenAuthenticationFilter with a JwtDecoder

}
