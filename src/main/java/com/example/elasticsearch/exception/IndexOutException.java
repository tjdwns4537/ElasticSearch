package com.example.elasticsearch.exception;

public class IndexOutException extends IndexOutOfBoundsException{
    public IndexOutException() {

    }

    public IndexOutException(String message) {
        super(message);

        System.out.println("error : " + message);
    }

    public static IndexOutOfBoundsException throwException(String message) {
        throw new IndexOutException(message);
    }
}
