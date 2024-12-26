package com.example.textilproject.security.JWT;

import com.example.textilproject.repository.TokenRepository;
import com.example.textilproject.security.utility.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService ;

    private final CustomUserDetailsService customUserDetailsService  ;

     private final TokenRepository tokenRepository ;

    public JWTAuthenticationFilter(JWTService jwtService, CustomUserDetailsService customUserDetailsService, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if ((authHeader == null) || !authHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = authHeader.substring(7);
            String email = jwtService.extractEmailFromJwt(jwt);
            if (email == null || SecurityContextHolder.getContext().getAuthentication() != null) { // true if USER email don't exist
                // or the user is already authenticated
                filterChain.doFilter(request, response);
                return;
            }

            UserDetails user = customUserDetailsService.loadUserByUsername(email);
            log.info("user loaded {} :", user);
            if (!jwtService.isTokenValid(user, jwt)) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtService.validateToken(jwt)) {
                filterChain.doFilter(request, response);
                return;
            }

            var isTokenValid = tokenRepository.findByToken(jwt).map(t -> (!t.isExpired() && !t.isRevoked())).orElse(false);
            var TokenSaved = tokenRepository.findByToken(jwt).orElse(null);

            if (!isTokenValid) {
                throw new IllegalArgumentException("token invalid");
            }
            if (TokenSaved == null) {
                throw new IllegalArgumentException("Invalid not found");
            }

            if (jwtService.isTokenExpired(jwt)) {
                throw new IllegalArgumentException("token expired");
            }

            if (TokenSaved.isRevoked()) {
                throw new IllegalArgumentException("token revoked");
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);
        }
        catch (Exception e)
        {
            log.error("Error logging in: {}",e.getMessage());
            response.setHeader("error",e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            Map<String, String> error = new HashMap<>();
            error.put("time_stamp" , String.valueOf(LocalDateTime.now()));
            error.put("status" , String.valueOf(HttpServletResponse.SC_FORBIDDEN));
            error.put("error_message", e.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(),error);
        }
    }
}
