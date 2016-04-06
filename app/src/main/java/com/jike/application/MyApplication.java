package com.jike.application;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.jike.beans.ProcessInfo;
import com.jike.mobilemanager_jk.R;
import com.jike.service.BlackListService;
import com.jike.service.MyNumberLocationService;
import com.jike.utils.ProcessUtils;
import com.jike.widget.MyAppWidgetProvider;

import java.util.ArrayList;

/**
 * Created by wancc on 2016/3/25.
 */
public class MyApplication extends Application {

    public static SharedPreferences config;
    private static SharedPreferences.Editor editor;
    private MyWidgetReceiver myWidgetReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        config = getSharedPreferences("config", MODE_PRIVATE);
        editor = config.edit();
        //根据用户的选择来开启服务
        if (getConfigValue("getTelLocation",false))
            startService(new Intent(this, MyNumberLocationService.class));
        if (getConfigValue("blacklist",false)){
            //开启电话拦截
            startService(new Intent(this, BlackListService.class));

            //开启短信拦截
            IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            registerReceiver(new BlackListService().new MySmsBroadcastReceiver(),filter);
        }


        //注册广播，以接收来自widget的请求
        myWidgetReceiver = new MyWidgetReceiver();
        IntentFilter filter = new IntentFilter("com.jike.mobilemanager_jk.widget");
        registerReceiver(myWidgetReceiver,filter);

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
        unregisterReceiver(myWidgetReceiver);
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

    //广播，以接收来自widget的请求
    class MyWidgetReceiver extends BroadcastReceiver {
        private static final String TAG = "MyWidgetReceiver";

        @Override

        public void onReceive(Context context, Intent intent) {
            Log.e(TAG,"MyWidgetReceiver onReceive");

            ArrayList<ProcessInfo> allProcess = ProcessUtils.getAllProcess(context);
            ProcessUtils.killProcess(allProcess,context);
            String allProcessNum = ProcessUtils.getAllProcessNum(context);
            String availableRAM = ProcessUtils.getAvailableRAM(context);


            //更新widget
            //法一：发广播，在widget的onreceive 方法里，拿出数据，利用appWedgetManager去updateAppWidget

            //法二：不需要发广播，直接利用appWedgetManager去updateAppWidget
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.processmanager_appwidget);
            remoteViews.setTextViewText(R.id.tv_processwidget_count,"进程数："+allProcessNum+"个");
            remoteViews.setTextViewText(R.id.tv_processwidget_memory,"可用RAM："+availableRAM);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName provider = new ComponentName(context, MyAppWidgetProvider.class);
            appWidgetManager.updateAppWidget(provider,remoteViews);
        }
    }

}
