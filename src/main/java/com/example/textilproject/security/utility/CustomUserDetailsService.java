package com.example.textilproject.security.utility;

import com.example.textilproject.exceptions.custom.ResourceNotFoundCustomException;
import com.example.textilproject.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    final UserRepository userRepository ;

    public CustomUserDetailsService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.fetchUserWithEmail(email).orElseThrow(() -> new ResourceNotFoundCustomException("user not found"));}
}
