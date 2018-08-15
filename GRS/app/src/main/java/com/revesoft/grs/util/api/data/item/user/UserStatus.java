package com.revesoft.grs.util.api.data.item.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserStatus {

    private String user_id;  //0=complainant, 1=admin
    private String user_full_name;
    private String user_mobile;
    private String password;
    private boolean isLogin;

    private Context context;
    private SharedPreferences mPrefs;
    public UserStatus(Context context) {

        this.context = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SharedPreferences getPrefs() {
        return mPrefs;
    }

    public String getUser_id() {
        return mPrefs.getString("user_id", "");
    }

    public void setUser_id( String user_id) {
        mPrefs.edit().putString("user_id", user_id).commit();
    }

    public String getUser_full_name() {
        return mPrefs.getString("user_full_name", "");

    }

    public void setUser_full_name(String user_full_name) {

        mPrefs.edit().putString("user_full_name", user_full_name).commit();
    }

    public String getUser_mobile() {
        return mPrefs.getString("user_mobile", "");

    }

    public void setUser_mobile(String user_mobile) {
      //  this.user_mobile = user_mobile;
        mPrefs.edit().putString("user_mobile", user_mobile).commit();
    }

    public String getUser_password() {
        return mPrefs.getString("user_password", "");

    }

    public void setUser_password(String user_password) {
        //  this.user_mobile = user_mobile;
        mPrefs.edit().putString("user_password", user_password).commit();
    }

    public boolean isLogin() {
        return mPrefs.getBoolean("isLogin", false);
    }

    public void setLogin(boolean isLogin) {

        mPrefs.edit().putBoolean("isLogin", isLogin).commit();
    }

    public UserStatus() {
    }



}
