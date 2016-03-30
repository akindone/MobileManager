package com.jike.mobilemanager_jk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.jike.application.MyApplication;

public class SettingActivity extends ActionBarActivity {

    private static final String TAG = "SettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();
    }


    public void bt_setting_mytoast_bg_blue(View view) {
        MyApplication.setConfigValue("ll_mytoast_bg",R.drawable.call_locate_blue);
    }

    public void bt_setting_mytoast_bg_grey(View view) {
        MyApplication.setConfigValue("ll_mytoast_bg",R.drawable.call_locate_gray);

    }

    public void bt_setting_mytoast_bg_green(View view) {
        MyApplication.setConfigValue("ll_mytoast_bg",R.drawable.call_locate_green);

    }

    public void bt_setting_mytoast_location(View view) {
        Log.e(TAG,"bt_setting_mytoast_location");
        startActivity(new Intent(this,SetMyToastLocation.class));


    }
}
