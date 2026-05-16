package com.Medilabo_solutions.Frontend_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * Security configuration for the Frontend Service.
 * This configuration disables CSRF protection, defines authorization rules for the application endpoints, and configures form-based login and logout functionality. The /login endpoint and static resources are publicly accessible, while all other endpoints require authentication. It also sets up an authentication entry point to redirect unauthenticated users to the login page.
 *
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/error/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .disable()

                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("/login")
                        )
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                );

        return http.build();
    }

}
