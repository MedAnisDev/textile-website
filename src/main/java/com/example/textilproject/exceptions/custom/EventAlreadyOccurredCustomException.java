package com.example.textilproject.exceptions.custom;

import com.example.textilproject.exceptions.responseHandling.CustomException;

public class EventAlreadyOccurredCustomException extends CustomException {
    public EventAlreadyOccurredCustomException(String message) {
        super(message);
    }
}
