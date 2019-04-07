package com.revesoft.grs.util;

import com.android.volley.Request;

/**
 * Created by sajid on 8/8/2018.
 */
public interface API {

    String APP_URL = new CheckBaseURL().URL();

    String COMPLAINANT_DASHBOARD_TAG = "viewGrievances.do";
    String ADMIN_DASHBOARD_TAG = "viewMyGrievances.do";
    String SIGN_UP_TAG = "mobileRegistration.do";
    String FORGOT_PASSWORD_TAG = "api/citizen/reset/pincode/";
    String COMPLAINANT_SIGN_IN_TAG = "login?a=0";
    String COMPLAINANT_SIGN_IN_SUCCESS_TAG = "login/success";
    String COMPLAINANT_SIGN_IN_FAILURE_TAG = "login?a=0&error";
    String COMPLAINANT_LOG_OUT_TAG = "logout";
    String ADMIN_SIGN_IN_TAG = "mobile-login";
    String ADMIN_LOG_OUT_TAG = "ssologout";
    String ADMIN_SIGN_IN_SUCCESS_TAG = "login/success";
    String ADMIN_SIGN_IN_FAILURE_TAG = "login?a=2";
    String PIN_CHANGE_TAG = "api/citizen/reset/pincode";
    String POLICY_TAG = "grsApplicationPrivacyPolicy.do";



    String SIGN_UP_TAG_URL = APP_URL + "/"  + SIGN_UP_TAG;

    String FORGOT_PASSWORD_TAG_URL = APP_URL + "/"  + FORGOT_PASSWORD_TAG;

    String COMPLAINANT_SIGN_IN_TAG_URL = APP_URL + "/"  + COMPLAINANT_SIGN_IN_TAG;

    String COMPLAINANT_LOG_OUT_TAG_URL = APP_URL + "/"  + COMPLAINANT_LOG_OUT_TAG;

    String  ADMIN_SIGN_IN_TAG_URL = APP_URL + "/"  + ADMIN_SIGN_IN_TAG;

    String COMPLAINANT_SIGN_IN_SUCCESS_TAG_URL = APP_URL + "/"  + COMPLAINANT_SIGN_IN_SUCCESS_TAG;

    String  COMPLAINANT_SIGN_IN_FAILURE_TAG_URL = APP_URL + "/"  + COMPLAINANT_SIGN_IN_FAILURE_TAG;

    String  COMPLAINANT_DASHBOARD_TAG_URL = APP_URL + "/"  + COMPLAINANT_DASHBOARD_TAG;

    String  ADMIN_DASHBOARD_TAG_URL = APP_URL + "/"  + ADMIN_DASHBOARD_TAG;

    String ADMIN_SIGN_IN_SUCCESS_TAG_URL = APP_URL + "/"  + ADMIN_SIGN_IN_SUCCESS_TAG;

    String ADMIN_SIGN_IN_FAILURE_TAG_URL = APP_URL + "/"  + ADMIN_SIGN_IN_FAILURE_TAG;

    String ADMIN_LOG_OUT_TAG_URL = APP_URL + "/"  + ADMIN_LOG_OUT_TAG;

    String PIN_CHANGE_TAG_URL = APP_URL + "/"  + PIN_CHANGE_TAG;

    String POLICY_TAG_URL = APP_URL + "/"  + POLICY_TAG;


    interface Method {
        int PUT_API_METHOD = Request.Method.PUT;
        int SEARCH_TRIPS_API_METHOD = Request.Method.GET;
        int HANDSHAKE_API_METHOD = Request.Method.POST;
    }

    interface Parameter {
        String ANDROID_APP_VERSION = "android_app_version";
        String ANDROID_DEVICE_ID = "android_device_id";
        String MOBILE_NUMBER = "mobile_number";
        String PASSWORD = "password";
    }
}
