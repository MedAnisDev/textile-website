package com.example.textilproject.DTO.auth;

import com.example.textilproject.DTO.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginResponseDTO {
    private String accessToken ;
    private String refreshToken ;
    private UserDTO userDTO;

}
