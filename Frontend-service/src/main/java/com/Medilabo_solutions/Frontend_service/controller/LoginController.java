package com.Medilabo_solutions.Frontend_service.controller;

import com.Medilabo_solutions.Frontend_service.dto.LoginRequestDTO;
import com.Medilabo_solutions.Frontend_service.service.AuthClientService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LoginController {
   private final AuthClientService authClientService;


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginRequestDTO loginRequestDTO, HttpSession session, Model model) {

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
            return "login";
        }
    }


}
