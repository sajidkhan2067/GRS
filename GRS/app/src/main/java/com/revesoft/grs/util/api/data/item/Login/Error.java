package com.revesoft.grs.api.data.item.Login;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sajid on 12/14/2015.
 */
public class Error {
    @SerializedName("code")
    private int code;
    @SerializedName("messages")
    private ArrayList<String> messages;


    public Error() {
    }

    public Error(int code, ArrayList<String> messages) {

        this.code = code;
        this.messages = messages;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Error)) return false;

        Error error = (Error) o;

        if (code != error.code) return false;
        return messages.equals(error.messages);

    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + messages.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Error{" +
                "code=" + code +
                ", messages=" + messages +
                '}';
    }
}
