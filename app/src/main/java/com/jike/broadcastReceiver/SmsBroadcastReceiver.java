package com.jike.broadcastReceiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.jike.application.MyApplication;
import com.jike.mobilemanager_jk.R;
import com.jike.service.GetLocationService;

/**
 * 短信广播接受者
 * 当收到来自安全手机号发出的短信时，根据短信内容开启服务
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsBroadcastReceiver";

    public SmsBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[])intent.getExtras().get("pdus");
        for (Object pud:pdus){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pud);
            String body = smsMessage.getMessageBody();
            String originatingAddress = smsMessage.getOriginatingAddress();
            Log.e(TAG,body+"----"+originatingAddress);

            if (body.equals("*#alarm#*")){
                Log.e(TAG,"*#alarm#*");
                playalarm(context);
            }
            else if (body.equals("*#location#*")){
                Log.e(TAG,"*#location#*");
                getlocation(context);
            }else if (body.equals("*#wipedata#*")){
                wipedata(context);
            }else if (body.equals("*#lockscreen#*")){
                lockscreen(context);

            }
        }
    }

    private void lockscreen(Context context) {
        DevicePolicyManager mDPM =
                (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDPM.lockNow();
        mDPM.resetPassword("123",0);
    }

    private void wipedata(Context context) {
        DevicePolicyManager mDPM =
                (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDPM.wipeData(0);


    }


    private void getlocation(Context context) {
        context.startService(new Intent(context, GetLocationService.class));
        new Thread(){
            @Override
            public void run() {
                super.run();
                String latitude="" ;
                String longitude="";
                while (latitude.isEmpty()&&longitude.isEmpty()){
                    latitude = MyApplication.getConfigValue("latitude", "");
                    longitude = MyApplication.getConfigValue("longitude", "");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String safenum = MyApplication.getConfigValue("safenum", "");
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(safenum,null,latitude+"---"+longitude,null,null);

                Log.e(TAG,latitude+"---"+longitude);
            }
        }.start();
    }

    private void playalarm(Context context) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        /*MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(context, Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm));
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }
}
