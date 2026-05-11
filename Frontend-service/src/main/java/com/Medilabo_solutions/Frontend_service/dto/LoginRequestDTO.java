package com.Medilabo_solutions.Frontend_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequestDTO {
    private String username;
    private String password;
}
