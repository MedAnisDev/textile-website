package com.example.textilproject.exceptions.custom;

import com.example.textilproject.exceptions.responseHandling.CustomException;

public class EmailSendingCustomException extends CustomException {
    public EmailSendingCustomException(String message) {
        super(message);
    }
}
