package com.example.textilproject.exceptions.custom;

import com.example.textilproject.exceptions.responseHandling.CustomException;

public class ResourceNotFoundCustomException extends CustomException {
    public ResourceNotFoundCustomException(String message) {
        super(message);
    }
}
