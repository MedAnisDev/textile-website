package com.example.textilproject.security.JWT;

import com.example.textilproject.security.utility.SecurityConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Configuration
@Service
@Slf4j
public class JWTService {

    public String generateToken(UserDetails userDetails){
        log.info("token generated for {}",userDetails.getUsername());
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis()+ SecurityConstants.ACCESS_JWT_EXPIRATION))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(getSignInKey() , SignatureAlgorithm.HS256)
                .compact();
    }
    private Key getSignInKey(){
        byte [] keyBytes = Decoders.BASE64.decode(SecurityConstants.ACCESS_JWT_SECRET_KEY) ;
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public boolean validateToken(String token) {
        //Return true if claims can be extracted
        try {
            Claims claims = extractAllClaims(token);
        } catch (ExpiredJwtException e) {
            log.error("Token has expired: {}", e.getMessage()); // Log to see if this block is reached
            throw new IllegalArgumentException("Token has expired");
        } catch (MalformedJwtException e) {
            log.error("Malformed token: {}", e.getMessage()); // Log to see if this block is reached
            throw new IllegalArgumentException("Malformed token");
        }
        return true ;
    }

    public boolean isTokenValid(@NonNull UserDetails userDetails , String token){
        return (userDetails.getUsername().equals(extractEmailFromJwt(token)) && !isTokenExpired(token)) ;
    }

    public String extractEmailFromJwt(String token){
        return extractClaim(token , Claims::getSubject ) ;
    }

    public boolean isTokenExpired(String token) {
        return (extractExpirationToken(token).before(new Date(System.currentTimeMillis()))) ;
    }

    private Date extractExpirationToken(String token) {
        return extractClaim(token , Claims::getExpiration) ;
    }

    private <T> T extractClaim(String token, Function<Claims , T> claimResolver) {
        Claims claims = extractAllClaims(token) ;
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

            return Jwts
                    .parserBuilder()        // Creates a new JwtParserBuilder instance
                    .setSigningKey(getSignInKey())
                    .build()                // Builds the JwtParser instance
                    .parseClaimsJws(token)
                    .getBody();
    }

}
