package com.myitsolver.baseandroidapp.models;

import androidx.annotation.Keep;

import com.squareup.moshi.JsonClass;

/**
 * Created by Peter on 2016. 08. 10..
 */
@Keep
public class MessageWrapper {
    private String message;

    public MessageWrapper() {
    }

    public MessageWrapper(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
