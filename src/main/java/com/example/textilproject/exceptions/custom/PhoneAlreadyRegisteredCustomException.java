package com.example.textilproject.exceptions.custom;

import com.example.textilproject.exceptions.responseHandling.CustomException;

public class PhoneAlreadyRegisteredCustomException extends CustomException {
    public PhoneAlreadyRegisteredCustomException(String message){
        super(message) ;
    }

}
