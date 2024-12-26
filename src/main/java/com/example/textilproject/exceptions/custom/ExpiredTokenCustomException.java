package com.example.textilproject.exceptions.custom;

import com.example.textilproject.exceptions.responseHandling.CustomException;

public class ExpiredTokenCustomException extends CustomException {
    public ExpiredTokenCustomException(String message) {
        super(message);
    }
}
