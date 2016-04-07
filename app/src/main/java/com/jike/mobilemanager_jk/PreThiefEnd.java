package com.jike.mobilemanager_jk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jike.application.MyApplication;

public class PreThiefEnd extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_thief_end);

        TextView et_safetelnum = (TextView) findViewById(R.id.et_safetelnum);
        ImageView et_doPreThief = (ImageView) findViewById(R.id.et_doPreThief);

        String safenum = MyApplication.getConfigValue("safenum", "");
        boolean openFunc = MyApplication.getConfigValue("openPreThiefFunc", false);

        et_safetelnum.setText(safenum);
        if (openFunc){
            et_doPreThief.setImageResource(R.drawable.lock);
        }
    }

    public void gotoSetupStart(View v){
        startActivity(new Intent(PreThiefEnd.this,PreThiefSetup1.class) );
    }

    public void gotoHome(View view) {
        startActivity(new Intent(PreThiefEnd.this,Home.class) );
    }
}
