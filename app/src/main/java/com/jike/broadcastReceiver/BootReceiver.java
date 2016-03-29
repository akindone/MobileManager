package com.jike.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.jike.application.MyApplication;

/**
 * 静态注册了一个手机开机广播接受者
 * 每次开机去sp查是否开启了手机防盗功能（openPreThiefFunc true？）
 * -->去获取当前sim卡信息，和sp中的比对，如果不相同
 * -->给安全手机号发送短信
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean openPreThiefFunc = MyApplication.getConfigValue("openPreThiefFunc", false);
        if (openPreThiefFunc){
            TelephonyManager teleManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            String number_current = teleManager.getSimSerialNumber();
            String number_saved = MyApplication.getConfigValue("simSerialNumber", "");
            if (!number_current.equals(number_saved)){
                //发短信给安全号码
                String safenum = MyApplication.getConfigValue("safenum", "");
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(safenum,null,"手机sim被替换",null,null);
                Log.e(TAG,"手机sim被替换");
            }
        }


    }
}
