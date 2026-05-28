package com.Medilabo_solutions.Frontend_service.controller;

import com.Medilabo_solutions.Frontend_service.dto.LoginRequestDTO;
import com.Medilabo_solutions.Frontend_service.service.AuthClientService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
/**
 * Controller responsible for handling user login requests.
 * Provides endpoints for displaying the login page and processing login submissions.
 * Upon successful authentication, it stores the JWT token and user information in the session and security context.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
   private final AuthClientService authClientService;


    @GetMapping("/login")
    public String login() {
        log.info("Displaying login page");
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginRequestDTO loginRequestDTO, HttpSession session, Model model) {
    log.info("Processing login for user: {}", loginRequestDTO.getUsername());
        try {
            String jwt = authClientService.authenticate(loginRequestDTO).getToken();
            String username = loginRequestDTO.getUsername();

            SecurityContextHolder.getContext()
                    .setAuthentication(
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
                            )
                    );

            session.setAttribute("jwt", jwt);
            session.setAttribute("username", username);
            session.setAttribute("SPRING_SECURITY_CONTEXT",
                    SecurityContextHolder.getContext());

            return "redirect:/patients";
        } catch (Exception e) {
            model.addAttribute("error", "Identifiants invalides");
            log.warn("Login failed for user: {}", loginRequestDTO.getUsername());
            return "login";
        }
    }


}
