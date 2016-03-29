package com.jike.mobilemanager_jk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.jike.application.MyApplication;

/**
 * 进入安全防盗功能的人口，判断用户之前是否设置过来决定跳转到哪个页面
 */
public class PreThiefStart extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_thief_start);
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean openFunc = MyApplication.getConfigValue("openPreThiefFunc", false);//如果之前开启了手机防盗功能，则返回true
        if (openFunc){
            //开启了，进入展示页面
            startActivity(new Intent(PreThiefStart.this,PreThiefEnd.class));
        }else{
            //如果没开启，进入向导页面
            startActivity(new Intent(PreThiefStart.this,PreThiefSetup1.class));
        }
    }
}
