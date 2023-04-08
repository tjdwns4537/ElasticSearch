package com.example.elasticsearch.helper;

public enum StatusEnum {
    OK(200, "OK"),
    NOT_FOUND(404, "NOT FOUND"),
    BAD_REQUEST(400, "BAD REQUSET");

    int code;
    String message;

    StatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
