package com.revesoft.grs.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AppManager {

    private static final int PRIVATE_MODE = Context.MODE_PRIVATE;
    private static final String PREF_NAME = "ShohozPref";
    private static final String KEY_DEVICE_ID = "androidDeviceId";
    private static final String KEY_HASH_KEY = "serverHashKey";
    private static final String KEY_APP_START_FIRST_TIME = "appStartFirstTime";
    private static final String KEY_SELECTED_BUS_TYPE= "selectedBusType";
    private static final String KEY_SELECTED_BUS_ROUTE_ID= "selectedBusRouteId";
    private static final String KEY_SHOHOZ_FEE = "shohozFee";
    private static final String KEY_BKASH_FEE = "bkashFee";
    private static final String KEY_SSL_FEE = "sslFee";
    private static final String KEY_SSL_CARD_NAME = "cardName";
    private static final String KEY_OFFERS = "shohozOffer";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_FIRST_NAME = "first-name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_USER_MAIL = "mail";
    public static final String KEY_DISCOUNT = "discount";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private Activity activity;
    private PackageInfo packageInfo;


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    private static final SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.US);
    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.US);
    private static final String dateFormat = "%s %s %s %d";

    public AppManager(Activity activity) {
        this.activity = activity;
        mSharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = mSharedPreferences.edit();
        try {
            packageInfo = this.activity.getPackageManager().getPackageInfo(this.activity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    private SharedPreferences getSharedPreferences(final String prefName, final int mode) {
        return this.activity.getSharedPreferences(prefName, mode);
    }

    public String getDeviceId() {
        return mSharedPreferences.getString(KEY_DEVICE_ID, "?");
    }


    public void setDeviceId(String deviceId) {
        editor.putString(KEY_DEVICE_ID, deviceId);
        editor.commit();
    }

    public String getHashkey() {
        return mSharedPreferences.getString(KEY_HASH_KEY, "");
    }


    public void setHashkey(String hashKey) {
        editor.putString(KEY_HASH_KEY, hashKey);
        editor.commit();
    }

    public String getKeyUserMail() {
        return mSharedPreferences.getString(KEY_USER_MAIL, "");
    }


    public void setKeyUserMail(String keyUserMail) {
        editor.putString(KEY_USER_MAIL, keyUserMail);
        editor.commit();
    }

    public String getOffers() {
        return mSharedPreferences.getString(KEY_OFFERS, "");
    }


    public void setOffers(String offersJson) {
        editor.putString(KEY_OFFERS, offersJson);
        editor.commit();
    }

    public boolean getAppStartFirstTime() {
        return mSharedPreferences.getBoolean(KEY_APP_START_FIRST_TIME, true);
    }


    public void setAppStartFirstTime(boolean appStartFirstTime) {
        editor.putBoolean(KEY_APP_START_FIRST_TIME, appStartFirstTime);
        editor.commit();
    }

    public boolean getDiscount() {
        return mSharedPreferences.getBoolean(KEY_DISCOUNT, false);
    }


    public void setDiscount(boolean discount) {
        editor.putBoolean(KEY_DISCOUNT, discount);
        editor.commit();
    }

    public String getAppVersion() {
        return packageInfo.versionName;
    }

    public String getTime(String time) {
        String tripTime;
        String subTime[] = time.split(":");
        tripTime = (Integer.parseInt(subTime[0]) <= 12) ? subTime[0].equals("00") ? "12" : subTime[0] : (Integer.parseInt(subTime[0]) - 12) + "";
        tripTime += ":";
        tripTime += subTime[1];
        tripTime += " ";
        tripTime += (Integer.parseInt(subTime[0]) < 12) ? "AM" : "PM";
        return tripTime;
    }

    public void setBKashFee(float bKashFee) {
        editor.putFloat(KEY_BKASH_FEE, bKashFee);
        editor.commit();
    }
    public void setSSLFee(float sslFee) {
        editor.putFloat(KEY_SSL_FEE, sslFee);
        editor.commit();
    }
    public void setShohozFee(int shohozFee) {
        editor.putInt(KEY_SHOHOZ_FEE, shohozFee);
        editor.commit();
    }
    public void setCardName(String cardName) {
        editor.putString(KEY_SSL_CARD_NAME, cardName);
        editor.commit();
    }
    public String getCardName() {
        return mSharedPreferences.getString(KEY_SSL_CARD_NAME, "?");
    }
    public float getBKashFee() {
        return mSharedPreferences.getFloat(KEY_BKASH_FEE, +0.0f);
    }
    public float getSSLFee() {
        return mSharedPreferences.getFloat(KEY_SSL_FEE, +0.0f);
    }
    public float getShohozFee() {
        return mSharedPreferences.getInt(KEY_SHOHOZ_FEE, 0);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public String getFormatDate(Date date) {
        return simpleDateFormat.format(date);
    }

    public String useDateTerminology(int date) {
        String htmlDate;
        String term;
        if (date / 10 == 1) {
            term = "th";
        } else {
            switch (date % 10) {
                case 1:
                    term = "st";
                    break;
                case 2:
                    term = "nd";
                    break;
                case 3:
                    term = "rd";
                    break;
                default:
                    term = "th";
                    break;
            }
        }
        htmlDate = String.format("%d", date);
        return htmlDate;
    }

    public String getBusType(String tripDetail) {
        String busTypes[] = tripDetail.split(",");
        String busType = "";
        if (busTypes.length > 1) {
            Log.d(busTypes.length + " \"bustype\"-", busTypes[0] + " " + busTypes[1]);
        }
        for (int i = 0; i < busTypes.length; i++) {
            busType += busTypes[i];
            if (i != busTypes.length - 1 && busType.trim().length() != 0) {
                busType += ", ";
            }
        }
        return busType.trim();
    }

    public void setSelectedBusType(String selectedBusType) {
        editor.putString(KEY_SELECTED_BUS_TYPE, selectedBusType);
        editor.commit();
    }

    public String getSelectedBusType() {
        return mSharedPreferences.getString(KEY_SELECTED_BUS_TYPE, "?");
    }

    public void setSelectedBusRouteId(String selectedBusRouteId) {
        editor.putString(KEY_SELECTED_BUS_ROUTE_ID, selectedBusRouteId);
        editor.commit();
    }

    public String getSelectedBusRouteId() {
        return mSharedPreferences.getString(KEY_SELECTED_BUS_ROUTE_ID, "1");
    }


    public void callOrProcessTheNumber(String number, String action) {
        number = number.replaceAll("-", "");
        String uri = "tel:" + number.trim();
        Intent intent = new Intent(action);
        intent.setData(Uri.parse(uri));
        activity.startActivity(intent);
    }
}