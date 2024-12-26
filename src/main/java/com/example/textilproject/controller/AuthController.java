package com.example.textilproject.controller;

import com.example.textilproject.DTO.auth.*;
import com.example.textilproject.service.AuthService;
import com.example.textilproject.service.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController( AuthServiceImpl authServiceImpl) {
        this.authService = authServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@NonNull @Valid @RequestBody RegisterDTO registerDTO ){
        return authService.register(registerDTO) ;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@NonNull @Valid @RequestBody LoginDTO loginDTO ){
        return authService.login(loginDTO) ;
    }

    @GetMapping("/refresh/{refreshToken}")
    public ResponseEntity<NewAccessTokenResponseDTO> refreshAccessToken(@PathVariable("refreshToken") final String refreshToken , @RequestParam("expiredToken") final String expiredToken){
        return authService.refreshAccessToken( expiredToken , refreshToken) ;
    }
}
