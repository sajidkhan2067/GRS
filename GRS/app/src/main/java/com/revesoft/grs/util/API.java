package com.revesoft.grs.util;

import com.android.volley.Request;

/**
 * Created by sajid on 5/5/2018.
 */
public interface API {

//    TypedArray a = getApp.obtainStyledAttributes(attrs, R.styleable.BuildType);
    String APP_URL = new CheckBaseURL().URL();

    String COMPLAINANT_DASHBOARD_TAG = "viewGrievances.do";
    String ADMIN_DASHBOARD_TAG = "viewMyGrievances.do";
    String SIGN_UP_TAG = "register.do";
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


    interface Method {
        int PUT_API_METHOD = Request.Method.PUT;
        int SEARCH_TRIPS_API_METHOD = Request.Method.GET;
        int SEAT_LAYOUT_API_METHOD = Request.Method.GET;
        int HANDSHAKE_API_METHOD = Request.Method.POST;
        int BOOK_TRIP_API_METHOD = Request.Method.POST;
        int BKASH_VERIFICATION_API_METHOD = Request.Method.POST;
        int COUPON_VERIFICATION_API_METHOD = Request.Method.POST;
        int GCM_REGISTRATION_API_METHOD = Request.Method.POST;
        int UPCOMING_TRIPS_DETAIL_API_METHOD = Request.Method.POST;
        int SSL_STATUS_API_METHOD = Request.Method.POST;
        int PAST_TRIPS_DETAIL_API_METHOD = Request.Method.POST;
        int CANCELLED_TRIPS_DETAIL_API_METHOD = Request.Method.POST;
        int USER_REGISTRATION_API_METHOD = Request.Method.POST;
        int USER_lOGIN_API_METHOD = Request.Method.POST;
        int SEARCH_TICKET_API_METHOD = Request.Method.POST;
        int TICKET_WINNING_API_METHOD= Request.Method.POST;
    }

    interface Parameter {
        String ANDROID_APP_VERSION = "android_app_version";
        String ANDROID_DEVICE_ID = "android_device_id";
        String HASH = "hash";
        String FROM_CITY = "from_city";
        String TO_CITY = "to_city";
        String DATE_OF_JOURNEY = "date_of_journey";
        String TRIP_ID = "trip_id";
        String ROUTE_ID = "route_id";
        String TICKET_ID = "ticket_id";
        String TRIP_ROUTE_ID = "trip_route_id";
        String RESERVATION_REFERENCES = "reservation_ref";
        String BKASH_TRANSACTION_ID = "bkash_transaction_id";
        String COUPON_CODE = "coupon_code";
        String MOBILE_NUMBER = "mobile_number";
        String AMOUNT = "amount";
        String TOTAL_TICKET = "total_ticket";
        String REGISTRATION_ID = "registration_id";
        String FULL_NAME = "full_name";
        String PASSWORD = "password";
        String PASSWORD_CONFIRMATION = "password_confirmation";
        String SHOHOZ_DISCOUNT = "shohoz_discount";
        String EMAIL_ADDRESS = "email_address";
        String DEVICE_MANUFACTURER ="device_manufacturer";
        String DEVICE_MODEL ="device_model";
        String DEVICE_RESOLUTION ="device_resolution";
        String CARRIER ="carrier";
        String FCM_REGISTRATION_ID = "registration_id";
        String PNR = "pnr";
        String CONTACT_NUMBER = "contact_number";
        String ORDER_ID = "order_id";
        String COMPANY_ID = "company_id";
        String BUS_TYPE = "bus_type";
    }
}
