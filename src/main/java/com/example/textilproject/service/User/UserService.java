package com.example.textilproject.service.User;


import com.example.textilproject.model.Admin;

public interface UserService{

    Admin fetchUserWithEmail(final String email);

    boolean isEmailRegistered( final String email);

    Admin getAdminById(final Long athleteId) ;
}
