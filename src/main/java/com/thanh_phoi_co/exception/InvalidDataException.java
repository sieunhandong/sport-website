package com.thanh_phoi_co.exception;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message){
        super(message);
    }
    public InvalidDataException(String message, Throwable cause){
        super(message,cause);
    }
}
