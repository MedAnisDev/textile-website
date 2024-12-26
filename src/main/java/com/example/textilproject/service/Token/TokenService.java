package com.example.textilproject.service.Token;

import com.example.textilproject.model.token.Token;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface TokenService {
    Token save(@NotNull Token token) ;
    List<Token> fetchAllValidTokenByUserId(Long userId);

    void saveAll(List<Token> tokens);

    Token fetchByToken(String expiredToken);

    void deleteByUserId(Long userId) ;
}
