package com.revesoft.grs.api.data.item.Login;

import com.google.gson.annotations.SerializedName;

public class LoginVerificationData {

    @SerializedName("user_id")
    private String user_id;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("mobile_number")
    private String mobile_number;


    public LoginVerificationData() {
    }


    public LoginVerificationData(String user_id, String first_name, String last_name, String mobile_number) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mobile_number = mobile_number;

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }


    @Override
    public String toString() {
        return "Data{" +
                "user_id='" + user_id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", mobile_number='" + mobile_number + '\'' +
                '}';
    }

}
