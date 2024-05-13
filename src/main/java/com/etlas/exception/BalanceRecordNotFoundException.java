package com.etlas.exception;

public class BalanceRecordNotFoundException extends RuntimeException{
    public BalanceRecordNotFoundException(String message) {
        super(message);
    }
}
