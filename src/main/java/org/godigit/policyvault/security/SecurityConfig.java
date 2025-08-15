package org.godigit.policyvault.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/audit/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable()); // ✅ Updated way to disable CSRF

        return http.build();
    }
}