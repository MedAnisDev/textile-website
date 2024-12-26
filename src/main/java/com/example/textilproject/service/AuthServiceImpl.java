package com.example.textilproject.service;

import com.example.textilproject.DTO.auth.*;
import com.example.textilproject.DTO.user.UserDTOMapper;
import com.example.textilproject.exceptions.custom.InvalidTokenCustomException;
import com.example.textilproject.model.Admin;
import com.example.textilproject.model.token.RefreshToken;
import com.example.textilproject.model.token.Token;
import com.example.textilproject.model.token.TokenType;
import com.example.textilproject.repository.UserRepository;
import com.example.textilproject.security.JWT.JWTService;
import com.example.textilproject.security.utility.SecurityConstants;
import com.example.textilproject.service.Token.RefreshTokenService;
import com.example.textilproject.service.Token.TokenService;
import com.example.textilproject.service.User.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
@Slf4j
public class AuthServiceImpl implements AuthService{
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final JWTService jwtService ;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager ;
    private final TokenService tokenService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService ;

    public AuthServiceImpl(JWTService jwtService, PasswordEncoder passwordEncoder, UserDTOMapper userDTOMapper, UserRepository userRepository, AuthenticationManager authenticationManager, TokenService tokenService, UserService userService, RefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userDTOMapper = userDTOMapper;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;

    }


    @Override
    @Transactional
    public ResponseEntity<RegisterResponseDTO> register(@NotNull final RegisterDTO registerDTO) {

        Admin admin = new Admin();
        admin.setName(registerDTO.getName());
        admin.setEmail(registerDTO.getEmail());
        admin.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        final Admin savedUser = userRepository.save(admin);

        //get tokens
        String refreshToken = refreshTokenService.generateRefreshToken(savedUser);

        //response
        final RegisterResponseDTO registerResponse = RegisterResponseDTO
                .builder()
                .userDTO(userDTOMapper.apply(savedUser))
                .refreshToken(refreshToken)
                .build();
        return new ResponseEntity<>(registerResponse , HttpStatus.CREATED) ;
    }

    @Override
    @Transactional
    public ResponseEntity<LoginResponseDTO> login(final LoginDTO loginDTO) {
        Admin user = userService.fetchUserWithEmail(loginDTO.getEmail());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(),
                loginDTO.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        revokeAllUsersAccessToken(user);
        revokeAllUsersRefreshToken(user);
        log.info("Attempting to authenticate user: {}", loginDTO.getEmail());

        var accessToken = generateAndSaveAccessToken(user);
        String refreshToken = generateAndSaveUserRefreshToken(user);

        //response
        final LoginResponseDTO loginResponse = LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userDTO(userDTOMapper.apply(user))
                .build();
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<NewAccessTokenResponseDTO> refreshAccessToken(String expiredToken , String refreshToken) {
        final Token currentAccessToken = tokenService.fetchByToken(expiredToken) ;
        final Admin currentUser = currentAccessToken.getUser() ;

        final RefreshToken currentRefreshToken = refreshTokenService.fetchTokenByToken(refreshToken) ;
        final boolean isRefreshTokenValid = refreshTokenService.validateRefreshToken(currentRefreshToken.getToken()) ;

        if( !isRefreshTokenValid ){
            throw new InvalidTokenCustomException("refresh token has expired or revoked");
        }
        if(!currentRefreshToken.getUser().getId().equals(currentUser.getId())){
            throw new InvalidTokenCustomException("the refresh and access token provided does not belong to the same user");
        }

        revokeAllUsersAccessToken(currentUser);
        String newAccessToken = generateAndSaveAccessToken(currentUser);
        final NewAccessTokenResponseDTO newAccessTokenResponse = NewAccessTokenResponseDTO
                .builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
        return new  ResponseEntity<>(newAccessTokenResponse , HttpStatus.OK );
    }


    private String  generateAndSaveAccessToken(Admin user){
        var accessToken = jwtService.generateToken(user);
        saveUserAccessToken(user ,accessToken);
        return accessToken ;
    }

    private void saveUserAccessToken(@NotNull Admin user ,@NotNull String accessToken){
        Token jwt = Token.builder()
                .user(user)
                .token(accessToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.save(jwt) ;
        logger.info("access Token saved successfully");

    }
    public String generateAndSaveUserRefreshToken(Admin user){
        var refreshToken = refreshTokenService.generateRefreshToken(user);
        saveUserRefreshToken(user , refreshToken);
        return refreshToken ;

    }

    private void saveUserRefreshToken(@NotNull Admin user ,@NotNull String refreshToken){
        Date expirationDate = new Date(System.currentTimeMillis() + SecurityConstants.REFRESH_JWT_EXPIRATION);
        Date issuedDate = new Date(System.currentTimeMillis()) ;

        RefreshToken jwt = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expired(false)
                .revoked(false)
                .issuedAt(issuedDate)
                .expiresAt(expirationDate) //30 minutes
                .build();
        refreshTokenService.save(jwt) ;
        logger.info("refresh Token saved successfully");
    }


    private void revokeAllUsersAccessToken(@NotNull Admin user){
        var validTokens = tokenService.fetchAllValidTokenByUserId(user.getId()) ;
        if(validTokens.isEmpty()){
            logger.info("there is no previous access token to revoke");
            return ;
        }
        validTokens.forEach(t ->{
            t.setRevoked(true);
            t.setExpired(true);
        });
        logger.info("previous access tokens revoked successfully");
        tokenService.saveAll(validTokens) ;
    }

    private void revokeAllUsersRefreshToken(@NotNull Admin admin){
        var validRefreshTokens = refreshTokenService.fetchAllValidRefreshTokenByUserId(admin.getId()) ;
        if(validRefreshTokens.isEmpty()){
            logger.info("there is no previous refresh token to revoke");
            return ;
        }
        validRefreshTokens.forEach(rt -> {
            rt.setRevoked(true);
            rt.setExpired(true);
        });
        logger.info("previous refresh tokens revoked successfully");
        refreshTokenService.saveAll(validRefreshTokens) ;
    }
}
