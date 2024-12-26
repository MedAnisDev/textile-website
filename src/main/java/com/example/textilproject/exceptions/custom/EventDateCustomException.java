package com.example.textilproject.exceptions.custom;

import com.example.textilproject.exceptions.responseHandling.CustomException;

public class EventDateCustomException extends CustomException {
    public EventDateCustomException(String message) {
        super(message);
    }
}
