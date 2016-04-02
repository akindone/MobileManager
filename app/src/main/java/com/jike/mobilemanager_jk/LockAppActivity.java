package com.jike.mobilemanager_jk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

public class LockAppActivity extends ActionBarActivity {

    private EditText et_applock_password;
    private String packagename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_app);
        et_applock_password = (EditText) findViewById(R.id.et_applock_password);

        Intent intent = getIntent();
        packagename = intent.getStringExtra("packagename");

    }

    public void check(View v){
        String pwd = et_applock_password.getText().toString();
        if ("123".equals(pwd)){
            //把该应用名 通过广播 发回给PMA，pma把该应用名加入白名单
            //TODO
            Intent intent = new Intent();
            intent.setAction("com.jike.mobilemanager_jk.lockapp");//接受者的intent filter 也是这个才能过滤到
            intent.putExtra("tempIgnorePkgname",packagename);

            sendBroadcast(intent);
            finish();
        }


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO 回到主页
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        startActivity(intent);
    }
}
