package com.revesoft.grs.util;

/**
 * Created by Droid on 21-Apr-16.
 */
public class CheckBaseURL {

    String BASE_URL = "";

    public String URL (){
        if (Constant.KEY_IS_DEVELOPMENT_BUILD){
            BASE_URL = "http://mapi9.shohoz.com";
        }
        else {
            BASE_URL = "http://mapi.shohoz.com";
        }
        return BASE_URL;
    }

}
