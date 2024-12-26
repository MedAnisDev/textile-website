package com.example.textilproject.DTO.auth;

import com.example.textilproject.DTO.user.UserDTO;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class RegisterResponseDTO {

    private String refreshToken;
    private UserDTO userDTO ;

    public RegisterResponseDTO(String refreshToken ,UserDTO userDTO){
        this.refreshToken =refreshToken ;
        this.userDTO =userDTO ;
    }
}
