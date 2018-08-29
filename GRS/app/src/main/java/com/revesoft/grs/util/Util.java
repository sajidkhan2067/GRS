package com.revesoft.grs.util;

import java.net.URL;

public class Util {

    /**
     *
     * @param userName
     * @return
     */
   public static String autoChangeUserName(String userName){


            String strPaddedBy = "";
            if (userName.length() > 2 && userName.length() < 7) {
                if (userName.startsWith("1")) {
                    strPaddedBy = "100000";
                } else if (userName.startsWith("2")) {
                    strPaddedBy = "200000";
                } else {
                    //others
                }

                if (userName.startsWith("1") || userName.startsWith("2")) {
                    userName = userName.substring(1, (userName.length()));
                    if (userName.length() == 6) {
                        userName = strPaddedBy + userName;
                    } else if (userName.length() == 5) {
                        userName = strPaddedBy + "0" + userName;
                    } else if (userName.length() == 4) {
                        userName = strPaddedBy + "00" + userName;
                    } else if (userName.length() == 3) {
                        userName = strPaddedBy + "000" + userName;
                    } else if (userName.length() == 2) {
                        userName = strPaddedBy + "0000" + userName.substring(1, (userName.length()));
                    }

                }
            }
            return userName;
        }

    
}
