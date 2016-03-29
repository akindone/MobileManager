package com.jike.application;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by wancc on 2016/3/25.
 */
public class MyApplication extends Application {

    public static SharedPreferences config;
    private static SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        config = getSharedPreferences("config", MODE_PRIVATE);
        editor = config.edit();
    }

    public static void setConfigValue(String key,String value){
        editor.putString(key,value);
        editor.commit();
    }

    public static String getConfigValue(String key,String defValue){
        String value = config.getString(key, defValue);
        return value;
    }

    public static void setConfigValue(String key,boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }

    public static boolean getConfigValue(String key,boolean defValue){
        boolean value = config.getBoolean(key, defValue);
        return value;
    }
}
