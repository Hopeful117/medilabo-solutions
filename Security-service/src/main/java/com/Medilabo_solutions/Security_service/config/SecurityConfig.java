package com.Medilabo_solutions.Security_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the Security Service.
 * This configuration sets up stateless session management, disables CSRF protection, and defines authorization rules for the API endpoints. The /auth/** endpoints are publicly accessible, while all other endpoints require authentication.
 * It also provides a bean for the AuthenticationManager and a PasswordEncoder for encoding user passwords.
 *
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    /**
     * Provides the AuthenticationManager bean, which is used for authenticating user credentials.
     *
     * @param config the AuthenticationConfiguration used to build the AuthenticationManager
     * @return the AuthenticationManager bean
     * @throws Exception if an error occurs while building the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    /**
     * Configures the security filter chain for the application.
     * Disables CSRF protection, sets session management to stateless, and defines authorization rules for API endpoints.
     *
     * @param http the HttpSecurity object used to configure security settings
     * @return the configured SecurityFilterChain bean
     * @throws Exception if an error occurs while configuring the security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**","/swagger-ui/**","/docs/**","/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
    /**
     * Provides a PasswordEncoder bean that uses BCrypt hashing algorithm for encoding user passwords.
     *
     * @return the PasswordEncoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
