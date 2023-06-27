package com.example.elasticsearch.exception;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ElasticSearchException extends Throwable{

    public ElasticSearchException(String message, Throwable cause) {
        super(message, cause);
    }



}
