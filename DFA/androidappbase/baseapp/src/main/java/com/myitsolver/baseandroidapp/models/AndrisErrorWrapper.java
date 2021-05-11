package com.myitsolver.baseandroidapp.models;

import androidx.annotation.Keep;

/**
 * Created by Patrik on 2017. 03. 17..
 */
@Keep
public class AndrisErrorWrapper implements CommonError {
    private String message;
    private String errorCode;

    public AndrisErrorWrapper() {
    }

    public AndrisErrorWrapper(String errorCode, String message) {
        this.message = message==null?"NullPointerException: please contact with the developers":message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String getError() {
        return errorCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
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
