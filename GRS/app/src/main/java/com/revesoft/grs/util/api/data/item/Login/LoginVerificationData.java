package com.revesoft.grs.util.api.data.item.Login;

import com.google.gson.annotations.SerializedName;

public class LoginVerificationData {

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;



    public LoginVerificationData() {
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
