package com.example.textilproject.DTO.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterDTO {

    private String name;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$" , message = "Invalid email address")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 8 , max=20 , message = "Invalid password length")
    private String password;

}
