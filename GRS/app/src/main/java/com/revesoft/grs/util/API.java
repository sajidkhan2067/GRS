package com.revesoft.grs.util;

import com.android.volley.Request;

/**
 * Created by sajid on 11/2/2015.
 */
public interface API {

//    TypedArray a = getApp.obtainStyledAttributes(attrs, R.styleable.BuildType);
    String SHOHOZ_API_URL = new CheckBaseURL().URL();
//        String SHOHOZ_API_URL = "http://mapi9.shohoz.com";
//        String SHOHOZ_API_URL = "http://mapi.saltchilli.com";
    String SHOHOZ_API_VERSION = "v1.0";
    String RECENT_SEARCH_API_TAG = "recent-searches";
    String SEARCH_TRIPS_API_TAG = "search-trips";
    String SEAT_LAYOUT_API_TAG = "seat-layout";
    String HANDSHAKE_API_TAG = "handshake";
    String BOOK_TRIP_API_TAG = "book-trip";
    String RELEASE_SEAT_API_TAG = "release-seat";
    String RESERVE_SEAT_API_TAG = "reserve-seat";
    String BKASH_VERIFICATION_API_TAG = "verify-bkash-transaction";
    String COUPON_VERIFICATION_API_TAG = "redeem-coupon";
    String GCM_REGISTRATION_API_TAG = "register-gcm-client";
    String TRIP_UPCOMING_API_TAG = "trips/upcoming";
    String TRIP_PAST_API_TAG = "trips/past";
    String PAYMENT_SUCCESS_API_TAG="payment-success";
    String PAYMENT_FAILURE_API_TAG="payment-failure";
    String TRIP_CANCELLED_API_TAG  = "trips/cancelled";
    String USERS_TAG = "users";
    String USER_REGISTRATION_API_TAG = "users/register";
    String USER_LOGIN_API_TAG = "users/verify";
    String DISCOUNT_API_TAG = "preview-discount";
    String SEARCH_TICKET_API_TAG = "search-ticket";
    String TICKET_WINNING_API_TAG = "pick-hourly-winner";



    String HANDSHAKE_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + HANDSHAKE_API_TAG;

    String RECENT_SEARCH_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + RECENT_SEARCH_API_TAG;

    String SEARCH_TRIPS_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + SEARCH_TRIPS_API_TAG;

    String SEAT_LAYOUT_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + SEAT_LAYOUT_API_TAG;

    String RESERVE_SEAT_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + RESERVE_SEAT_API_TAG;

    String RELEASE_SEAT_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + RELEASE_SEAT_API_TAG;

    String BOOK_TRIP_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + BOOK_TRIP_API_TAG;

    String BKASH_VERIFICATION_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + BKASH_VERIFICATION_API_TAG;

    String COUPON_VERIFICATION_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + COUPON_VERIFICATION_API_TAG;

    String GCM_REGISTRATION_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + GCM_REGISTRATION_API_TAG;

    String TRIP_DETAILS_HISTORY_BASE_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + USERS_TAG;

    String USER_REGISTRATION_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + USER_REGISTRATION_API_TAG;
    String PAYMENT_SUCCESS_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + PAYMENT_SUCCESS_API_TAG;

    String PAYMENT_FAILURE_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + PAYMENT_FAILURE_API_TAG;

    String USER_LOGIN_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + USER_LOGIN_API_TAG;

    String DISCOUNT_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + DISCOUNT_API_TAG;

    String SEARCH_TICKET_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + SEARCH_TICKET_API_TAG;

    String TICKET_WINNING_API_URL = SHOHOZ_API_URL + "/" + SHOHOZ_API_VERSION + "/" + TICKET_WINNING_API_TAG;

    interface Method {
        int RECENT_SEARCH_API_METHOD = Request.Method.GET;
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
