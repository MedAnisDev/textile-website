package com.example.textilproject.exceptions.custom;

import com.example.textilproject.exceptions.responseHandling.CustomException;

public class EmailAlreadyRegisteredCustomException extends CustomException {
    public EmailAlreadyRegisteredCustomException(String message){
        super(message);
    }
}
