package com.byteshaft.towerinfo;

import android.app.Application;
import android.content.Context;

public class AppGlobals  extends Application {

    private static Context sContext;
    public static String CURRENT_STATE = "";
    public static String schedule = "SCHEDULED";
    public static String call_dropped = "CALL_DROPPED";
    public static String suspend = "DATA_SUSPEND";
    public static final String KEY = "hash_set";
    public static String LOCATION = "Lat null" + ",Long null ";
    public static boolean SCHEDULE_STATE = false;
    public static final String SEND_BROAD_CAST = "send_broad_cast";
    public static boolean APP_FOREGROUND = false;
    public static boolean Uploaded = false;
    public static boolean IS_CALL_DROPPED = false;
    public static final int ONE_SECOND = 1000;
    public static final int ONE_MINUTE = ONE_SECOND * 60;
    public static final int TEN_MINUTES = ONE_MINUTE * 10;
    public static final int THIRTY_MINUTES= TEN_MINUTES * 3;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
