package com.illumio.analyzers.logs.exception;

public class FlowLogException extends Exception {

     String errorCode;
     String errorMessage;
     Exception rootException;

    public FlowLogException(ErrorCodes errorCodeEnum) {
        this.errorCode = errorCodeEnum.getCode();
        this.errorMessage = errorCodeEnum.getMessage();
    }

    //With custom error message
    public FlowLogException(ErrorCodes errorCodeEnum, String errorMesage) {
        this.errorCode = errorCodeEnum.getCode();
        this.errorMessage = errorCodeEnum.getMessage() + " " + errorMesage;
    }

    public FlowLogException(ErrorCodes errorCodeEnum, Exception exception) {
        this.errorCode = errorCodeEnum.getCode();
        this.errorMessage = errorCodeEnum.getMessage();
        this.rootException = exception;
    }

    public FlowLogException(ErrorCodes errorCodeEnum, String errorMesage, Exception exception) {
        this.errorCode = errorCodeEnum.getCode();
        this.errorMessage = errorCodeEnum.getMessage() + " " + errorMesage;
        this.rootException = exception;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage () {
        return this.errorMessage;
    }

    public Exception getRootException() {
        return this.rootException;
    }

    public String toString() {
        String message = this.errorCode + " " + this.errorMessage;
        if (rootException != null) message = message + " " + this.rootException;
        return message;
    }
}
