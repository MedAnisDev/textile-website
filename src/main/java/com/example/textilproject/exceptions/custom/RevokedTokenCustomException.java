package com.example.textilproject.exceptions.custom;

import com.example.textilproject.exceptions.responseHandling.CustomException;

public class RevokedTokenCustomException extends CustomException {
    public RevokedTokenCustomException(String message) {
        super(message);
    }
}
