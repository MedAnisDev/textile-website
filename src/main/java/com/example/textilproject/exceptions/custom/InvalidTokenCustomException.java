package com.example.textilproject.exceptions.custom;

import com.example.textilproject.exceptions.responseHandling.CustomException;

public class InvalidTokenCustomException extends CustomException {
    public InvalidTokenCustomException(String message) {
        super(message);
    }
}
