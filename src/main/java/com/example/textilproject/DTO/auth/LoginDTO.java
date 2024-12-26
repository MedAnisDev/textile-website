package com.example.textilproject.DTO.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginDTO {
    @NotBlank(message = "please enter your username")
    private String email;
    @NotBlank(message = "please enter your password")
    private String password;


}
