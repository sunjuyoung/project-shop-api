package com.project.shop.global.exception;

public class AlreadyExistEmailException extends RuntimeException{

    public AlreadyExistEmailException(String message) {
        super(message);
    }
}
