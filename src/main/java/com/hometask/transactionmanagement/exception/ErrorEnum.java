package com.hometask.transactionmanagement.exception;

public enum ErrorEnum {
    CREATE_DUPLICATE_EXCEPTION(0001, "重复创建交易单"),
    TRANSACTION_NOT_EXISTS_EXCEPTION(0002, "交易单不存在"),
    TRANSACTION_CHANGED_EXCEPTION(0003, "交易单数据修改中"),
    CONCURRENCY_EXCEPTION(0004, "存在冲突修改，请稍后重试"),
    SYSTEM_EXCEPTION(500, "系统异常请稍后重试");
    private final int code;
    private final String message;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
