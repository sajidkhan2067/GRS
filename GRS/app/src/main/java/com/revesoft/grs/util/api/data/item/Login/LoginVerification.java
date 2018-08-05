package com.revesoft.grs.api.data.item.Login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sajid on 12/14/2015.
 */
public class LoginVerification {

    @SerializedName("data")
    private com.revesoft.grs.api.data.item.Login.LoginVerificationData data;

    @SerializedName("error")
    private Error error;

    public LoginVerification() {
    }

    public LoginVerification(com.revesoft.grs.api.data.item.Login.LoginVerificationData data, Error error) {

        this.data = data;
        this.error = error;
    }

    public com.revesoft.grs.api.data.item.Login.LoginVerificationData getData() {

        return data;
    }

    public void setData(com.revesoft.grs.api.data.item.Login.LoginVerificationData data) {
        this.data = data;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof LoginVerification)) return false;

        LoginVerification that = (LoginVerification) o;

        if (!data.equals(that.data)) return false;
        return error.equals(that.error);

    }

    @Override
    public int hashCode() {
        int result = data.hashCode();
        result = 31 * result + error.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LoginVerification" +
                "data=" + data +
                ", error=" + error +
                '}';
    }
}
