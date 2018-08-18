package com.revesoft.grs.util.api.data.item.Login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sajid on 2/8/2018.
 */
public class LoginVerification {

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
//    @SerializedName("error")
//    private Error error;

    public LoginVerification() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
