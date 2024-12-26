package com.example.textilproject.DTO.user;

import com.example.textilproject.model.Admin;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<Admin, UserDTO> {
    @Override
    public UserDTO apply(Admin user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
