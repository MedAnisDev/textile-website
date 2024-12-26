package com.example.textilproject.service.User;

import com.example.textilproject.exceptions.custom.ResourceNotFoundCustomException;
import com.example.textilproject.model.Admin;
import com.example.textilproject.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository ;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Admin fetchUserWithEmail(@NotNull final String email) {
        log.info("email "+ email);
        return userRepository.fetchUserWithEmail(email)
                .orElseThrow(() -> new ResourceNotFoundCustomException("user not found")) ;
    }

    @Override
    public boolean isEmailRegistered( final String email) {
        return userRepository.isEmailRegistered(email);
    }

    @Override
    public Admin getAdminById(Long adminId) {
        return userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundCustomException("admin not found"));
    }
}
