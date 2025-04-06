package ru.savin.currencyhub.handler;

import lombok.Getter;

@Getter
public class CustomBusinessException extends RuntimeException {
    private final String errorCode;

    public CustomBusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
