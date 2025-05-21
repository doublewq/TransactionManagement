package com.hometask.transactionmanagement.exception;

import lombok.Data;

@Data
public class TransactionException extends RuntimeException {
    private final int code;
    private final String message;

    public TransactionException(ErrorEnum errorEnum) {
        this.code = errorEnum.getCode();
        this.message = errorEnum.getMessage();
    }

    public TransactionException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
