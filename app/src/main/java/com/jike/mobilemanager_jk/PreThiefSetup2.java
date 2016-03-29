package com.jike.mobilemanager_jk;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.jike.application.MyApplication;
import com.jike.view.SettingItem;

public class PreThiefSetup2 extends MyBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_thief_setup2);
        SettingItem si_setup2_simband = (SettingItem) findViewById(R.id.si_setup2_simband);
//        设置监听动作
        si_setup2_simband.setMyOnclickListener(new SettingItem.MyOnclickListener() {
            @Override
            public void doBindSim() {
                //获取当前sim卡的IMSI，写入sp  permission.READ_PHONE_STATE
                TelephonyManager teleManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String simSerialNumber = teleManager.getSimSerialNumber();
                MyApplication.setConfigValue("simSerialNumber",simSerialNumber);
            }

            @Override
            public void cancelBindSim() {
                //把当前sp的IMSI抹去
                MyApplication.setConfigValue("simSerialNumber","");
            }
        });
    }
    public void next(View v){
        String simSerialNumber = MyApplication.getConfigValue("simSerialNumber", "");
        if (simSerialNumber.isEmpty()){
        Toast.makeText(PreThiefSetup2.this, "请绑定sim卡，否则手机防盗功能无法使用", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(new Intent(this, PreThiefSetup3.class));
            overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);

        }
    }
    public void previous(View v){
        startActivity(new Intent(this, PreThiefSetup1.class));
        overridePendingTransition(R.anim.slideinleft, R.anim.slideoutright);
    }
}
