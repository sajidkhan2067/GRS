package com.revesoft.grs.util;


public class CheckBaseURL {

    String BASE_URL = "";

    public String URL (){
        if (Constant.KEY_IS_DEVELOPMENT_BUILD){
            BASE_URL = "http://www.grs.gov.bd";
        }
        else {
            BASE_URL = "http://www.grs.gov.bd";
        }
        return BASE_URL;
    }

}
