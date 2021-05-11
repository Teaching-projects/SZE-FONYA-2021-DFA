package com.myitsolver.baseandroidapp.models;

import androidx.annotation.Keep;

/**
 * Created by Patrik on 2017. 03. 17..
 */
@Keep
public class ErrorWrapper implements CommonError {
    private String message;
    private int errorCode;

    public ErrorWrapper() {

    }

    public ErrorWrapper(int errorCode, String message) {
        this.message = message==null?"NullPointerException: please contact with the developers":message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String getError() {
        return errorCode+"";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "ErrorWrapper{" +
                "message='" + message + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
