package com.jike.application;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by wancc on 2016/3/25.
 */
public class MyApplication extends Application {

    public static SharedPreferences config;

    @Override
    public void onCreate() {
        super.onCreate();
        config = getSharedPreferences("config", MODE_PRIVATE);
    }
}
