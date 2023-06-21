package com.example.elasticsearch.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomRuntimeException extends RuntimeException {
    public CustomRuntimeException() {

    }

    public CustomRuntimeException(String message, Throwable cause) {
        super(message,cause);
        System.out.println(message);
    }

    public static void throwException(String occur) {
        throw new CustomRuntimeException(occur, new RuntimeException());
    }
}
