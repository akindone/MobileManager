package com.jike.mobilemanager_jk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PreThiefSetup4 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_thief_setup4);

//        SettingItem si_setup4_doPreThief = (SettingItem) findViewById(R.id.si_setup4_doPreThief);


    }

    public void done(View v){
        Toast.makeText(PreThiefSetup4.this, "设置成功", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, PreThiefEnd.class));
    }
    public void previous(View v){
        startActivity(new Intent(this, PreThiefSetup3.class));
    }
}
