package com.example.textilproject.service.Token;

import com.example.textilproject.exceptions.custom.DatabaseCustomException;
import com.example.textilproject.exceptions.custom.ExpiredTokenCustomException;
import com.example.textilproject.exceptions.custom.ResourceNotFoundCustomException;
import com.example.textilproject.exceptions.custom.RevokedTokenCustomException;
import com.example.textilproject.model.Admin;
import com.example.textilproject.model.token.RefreshToken;
import com.example.textilproject.repository.RefreshTokenRepository;
import com.example.textilproject.security.utility.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransactionException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenRepository refreshTokenRepository ;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public String generateRefreshToken(@NotNull Admin user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.REFRESH_JWT_EXPIRATION))
                .signWith(getSignInKey() , SignatureAlgorithm.HS256)
                .compact() ;
    }

    @Override
    public List<RefreshToken> fetchAllValidRefreshTokenByUserId(final Long userId) {
        return refreshTokenRepository.fetchAllValidRefreshTokenByUserId(userId);
    }

    @Override
    public void saveAll(List<RefreshToken> refreshTokens) {
        try{
            refreshTokenRepository.saveAll(refreshTokens);
        }catch(DataAccessException da ) {
            throw new DatabaseCustomException("saving refresh token : An error occurred while accessing the database");
        }
        catch(TransactionException te){
            throw new DatabaseCustomException("saving refresh token : An error occurred while processing the transaction");
        }
    }

    @Override
    public void save(@NotNull final RefreshToken refreshToken) {
         try{
             refreshTokenRepository.save(refreshToken);
         }catch(DataAccessException da ) {
             throw new DatabaseCustomException("saving refresh token : An error occurred while accessing the database");
         }
         catch(TransactionException te){
             throw new DatabaseCustomException("saving refresh token : An error occurred while processing the transaction");
         }catch (ConstraintViolationException cve) {
             throw new DatabaseCustomException("A database constraint was violated when saving refresh token");
         }

    }

    @Override
    public RefreshToken fetchTokenByToken(final String refreshToken) {
        return refreshTokenRepository.fetchTokenByToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundCustomException("refresh token not found"));
    }

    @Override
    public boolean validateRefreshToken(final String refreshToken) {
        RefreshToken refToken = fetchTokenByToken(refreshToken) ;
        if(refToken.isExpired()){
            throw new ExpiredTokenCustomException("sorry , your refresh token is expired");
        }
        if(refToken.isRevoked()){
            throw new RevokedTokenCustomException("sorry , your refresh token is revoked") ;
        }
        return true ;
    }

    @Override
    public void deleteTokenByUserId(Long userId) {
        boolean isRefreshTokenExists = refreshTokenRepository.fetchByUserId(userId);
        log.info("isRefreshTokenExists :"+isRefreshTokenExists);

        if(isRefreshTokenExists){
            refreshTokenRepository.deleteRefreshTokenByUserId(userId);
            log.info("Refresh Token token deleted");
        }
    }

    private Key getSignInKey() {
    byte [] keyBytes = Decoders.BASE64.decode(SecurityConstants.REFRESH_JWT_SECRET_KEY) ;
    return Keys.hmacShaKeyFor(keyBytes) ;
    }
}
