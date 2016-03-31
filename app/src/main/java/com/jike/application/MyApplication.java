package com.jike.application;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.jike.service.MyNumberLocationService;

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
        //根据用户的选择来开启服务
        if (getConfigValue("getTelLocation",false))
            startService(new Intent(this, MyNumberLocationService.class));
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

    @Override
    public void onTerminate() {
        super.onTerminate();
//        stopService(new Intent(this, MyNumberLocationService.class));//使用者并不想应用退出后服务停止
    }

    public static int getConfigValue(String key, int defValue) {
        int value = config.getInt(key, defValue);
        return value;
    }

    public static void setConfigValue(String key, int value) {
        editor.putInt(key,value);
        editor.commit();
    }
}
