package com.example.textilproject.exceptions.custom;

import com.example.textilproject.exceptions.responseHandling.CustomException;

public class DatabaseCustomException extends CustomException {
    public DatabaseCustomException(String message) {
        super(message);
    }
}
