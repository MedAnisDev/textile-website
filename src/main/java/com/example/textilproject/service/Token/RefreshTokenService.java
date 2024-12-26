package com.example.textilproject.service.Token;

import com.example.textilproject.model.Admin;
import com.example.textilproject.model.token.RefreshToken;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface RefreshTokenService {
    String generateRefreshToken(@NotNull Admin admin);

    List<RefreshToken> fetchAllValidRefreshTokenByUserId(Long userId);

    void saveAll(List<RefreshToken> validRefreshTokens);

    void save(RefreshToken refreshToken);

    RefreshToken fetchTokenByToken(String refreshToken);

    boolean validateRefreshToken(String refreshToken);

    void deleteTokenByUserId(Long userId);
}
