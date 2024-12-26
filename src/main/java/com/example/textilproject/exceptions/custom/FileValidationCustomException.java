package com.example.textilproject.exceptions.custom;

import com.example.textilproject.exceptions.responseHandling.CustomException;

public class FileValidationCustomException extends CustomException {
    public FileValidationCustomException(String message) {
        super(message);
    }
}
