package com.example.textilproject.DTO.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewAccessTokenResponseDTO {
    private String accessToken ;
    private String refreshToken ;
}
