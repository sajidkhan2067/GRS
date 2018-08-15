package com.revesoft.grs.util.api.data.item.Login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sajid on 12/14/2015.
 */
public class LoginVerification {

    @SerializedName("data")
    private LoginVerificationData data;

//    @SerializedName("error")
//    private Error error;

    public LoginVerification() {
    }

    public LoginVerification(LoginVerificationData data, Error error) {

        this.data = data;
      //  this.error = error;
    }

    public LoginVerificationData getData() {

        return data;
    }

    public void setData(LoginVerificationData data) {
        this.data = data;
    }

//    public Error getError() {
//        return error;
//    }
//
//    public void setError(Error error) {
//        this.error = error;
//    }





}
