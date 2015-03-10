package com.younguard.CChess.config;

import com.younguard.CChess.param.AccountParams;


/**
 * Created by liwenzhi on 14-9-24.
 */
public class AppConfig {

    //dyna get
    public static String DEVICE_ID;
    //dyna get
    public static String OS_VERSION;

    //4k
    public static String VENDOR = "46098b55-27c7-45f3-afed-4794fb0ccd4d";

    //iGenie
    public static String APP_ID = "8ed18148-5d33-43f1-90f1-98b8bcf323d7";


    public static String APP_VERSION = "Romania_v2.0.23";

    /**
     * logged on user, initialized in UserLoginTask;
     */
    public static AccountParams account;
   


    public static boolean isLoggedIn() {
//        TODO, used in production environment
//        SharedPreferences sp = mContext.getSharedPreferences("SP", Activity.MODE_PRIVATE);
//        LOGGED_ON = sp.getBoolean("LOGGED_ON", false);

        return (account == null)?false:true;
    }

}
