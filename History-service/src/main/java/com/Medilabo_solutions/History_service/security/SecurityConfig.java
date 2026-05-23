package com.Medilabo_solutions.History_service.security;


import com.Medilabo_solutions.History_service.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/** * Security configuration for the Patient Service.
 * This configuration disables CSRF protection and allows all requests without authentication.
 * The authentication is handled by the Security Service, so the Patient Service does not require its own authentication mechanism.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtService jwtService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )

        .addFilterBefore(new JwtFilter(jwtService), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
