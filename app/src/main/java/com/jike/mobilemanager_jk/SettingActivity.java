package com.jike.mobilemanager_jk;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jike.application.MyApplication;
import com.jike.service.BlackListService;
import com.jike.service.MyNumberLocationService;
import com.jike.utils.PackageUtils;
import com.jike.view.SettingItem;

public class SettingActivity extends Activity {

    private static final String TAG = "SettingActivity";
    private SettingItem si_setup_telLocation;
    private SettingItem si_setup_blacklist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate  setContentView之前");
        setContentView(R.layout.activity_setting);
        Log.e(TAG, "onCreate  setContentView之后");
        /*ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();*/
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onCreate  onStart");
        si_setup_telLocation = (SettingItem) findViewById(R.id.si_setup_telLocation);
        si_setup_blacklist = (SettingItem) findViewById(R.id.si_setup_blacklist);

        //checkbox 既是用户开启服务的开关 也是服务是否开启的反映
        //判断服务开启状态
        boolean isStarted_numLoc = PackageUtils.isServiceStarted("com.jike.service.MyNumberLocationService", this);
        boolean isStarted_blacklist = PackageUtils.isServiceStarted("com.jike.service.BlackListService", this);

        Log.e("isStarted_numLoc",""+isStarted_numLoc);
        Log.e("isStarted_blacklist",""+isStarted_blacklist);


        si_setup_telLocation.setCheckbox(isStarted_numLoc);
        si_setup_blacklist.setCheckbox(isStarted_blacklist);


        //根据用户点击进行开关
        si_setup_telLocation.setMyOnclickListener(new SettingItem.MyOnclickListener() {
            @Override
            public void doBindSim() {
                startService(new Intent(SettingActivity.this, MyNumberLocationService.class));
            }

            @Override
            public void cancelBindSim() {
                stopService(new Intent(SettingActivity.this, MyNumberLocationService.class));
            }
        });

        //根据用户点击进行开关
        si_setup_blacklist.setMyOnclickListener(new SettingItem.MyOnclickListener() {

            private BlackListService.MySmsBroadcastReceiver smsBrdRec;
            boolean isRegister = false;

            @Override
            public void doBindSim() {
                startService(new Intent(SettingActivity.this, BlackListService.class));
                //开启短信拦截
                IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
                smsBrdRec = new BlackListService().new MySmsBroadcastReceiver();
                registerReceiver(smsBrdRec , filter);
                isRegister= true;
            }

            @Override
            public void cancelBindSim() {
                //防止多次点击，导致崩溃 Receiver not registered: null
                if (isRegister){
                    stopService(new Intent(SettingActivity.this, BlackListService.class));
                    unregisterReceiver(smsBrdRec);
                    isRegister= false;
                }


            }
        });
    }

    public void bt_setting_mytoast_bg_blue(View view) {
        MyApplication.setConfigValue("ll_mytoast_bg",R.drawable.call_locate_blue);
    }

    public void bt_setting_mytoast_bg_grey(View view) {
        MyApplication.setConfigValue("ll_mytoast_bg",R.drawable.call_locate_gray);

    }

    public void bt_setting_mytoast_bg_green(View view) {
        MyApplication.setConfigValue("ll_mytoast_bg", R.drawable.call_locate_green);

    }

    public void bt_setting_mytoast_location(View view) {
        Log.e(TAG,"bt_setting_mytoast_location");
        startActivity(new Intent(this,SetMyToastLocation.class));
    }

}
