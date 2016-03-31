package com.jike.mobilemanager_jk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.jike.application.MyApplication;
import com.jike.service.MyNumberLocationService;
import com.jike.utils.PackageUtils;
import com.jike.view.SettingItem;

public class SettingActivity extends ActionBarActivity {

    private static final String TAG = "SettingActivity";
    private SettingItem si_setup_telLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate  setContentView之前");
        setContentView(R.layout.activity_setting);
        Log.e(TAG, "onCreate  setContentView之后");
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();
    }

    //然并卵 因为再次可见时，系统不会调用SettingItem的构造方法
    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onCreate  onStart");
        si_setup_telLocation = (SettingItem) findViewById(R.id.si_setup_telLocation);
        //checkbox 既是用户开启服务的开关 也是服务是否开启的反映
        //判断服务开启状态
        boolean isStarted = PackageUtils.isServiceStarted("com.jike.mobilemanager_jk.MyNumberLocationService", this);
        if (isStarted){
            MyApplication.setConfigValue("getTelLocation",true);
        }else MyApplication.setConfigValue("getTelLocation", false);

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
