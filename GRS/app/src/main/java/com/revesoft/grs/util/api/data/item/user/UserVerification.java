package com.revesoft.grs.util.api.data.item.user;

import com.google.gson.annotations.SerializedName;

import java.lang.Error;

/**
 * Created by sajid on 12/14/2015.
 */
public class UserVerification {

    @SerializedName("data")
    private UserVerificationData data;

    @SerializedName("error")
    private Error error;

    public UserVerification() {
    }

    public UserVerification(UserVerificationData data, Error error) {

        this.data = data;
        this.error = error;
    }

    public UserVerificationData getData() {

        return data;
    }

    public void setData(UserVerificationData data) {
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
        if (!(o instanceof UserVerification)) return false;

        UserVerification that = (UserVerification) o;

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
        return "UserVerification" +
                "data=" + data +
                ", error=" + error +
                '}';
    }
}
