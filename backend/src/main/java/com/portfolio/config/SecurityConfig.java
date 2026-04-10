package com.portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AdminTokenFilter adminTokenFilter;

    public SecurityConfig(AdminTokenFilter adminTokenFilter) {
        this.adminTokenFilter = adminTokenFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Allow all GET requests except sensitive ones
                .requestMatchers(HttpMethod.GET, "/api/projects/trash").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/contact/messages/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/resume/status").permitAll() // Needed for UI checks
                .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                
                // Secure all modifications
                .requestMatchers(HttpMethod.POST, "/api/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/**").authenticated()
                
                // Specifically secure GitHub sync
                .requestMatchers("/api/projects/sync").authenticated()
                
                // Allow anything else (static resources, etc.)
                .anyRequest().permitAll()
            )
            .addFilterBefore(adminTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
