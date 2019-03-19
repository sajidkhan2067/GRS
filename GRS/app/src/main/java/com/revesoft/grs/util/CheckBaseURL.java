package com.revesoft.grs.util;


public class CheckBaseURL {

    String BASE_URL = "";

    public String URL() {
        if (Constant.KEY_IS_DEVELOPMENT_BUILD) {
//            BASE_URL = "http://www.grs.gov.bd";
        } else {
            BASE_URL = "http://103.48.18.9:81";
            // BASE_URL = "http://192.168.18.170:8080";
        }
        return BASE_URL;
    }

}
