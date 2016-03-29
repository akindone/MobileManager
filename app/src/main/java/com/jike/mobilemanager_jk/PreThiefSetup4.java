package com.jike.mobilemanager_jk;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jike.broadcastReceiver.MyAdminReceiver;

public class PreThiefSetup4 extends MyBaseActivity {

    private static final int REQUEST_CODE_ENABLE_ADMIN = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_thief_setup4);
    }

    @Override
    void next(View v) {
        done(v);
    }

    public void done(View v){
        Toast.makeText(PreThiefSetup4.this, "设置成功", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, PreThiefEnd.class));
        overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
    }
    public void previous(View v){
        startActivity(new Intent(this, PreThiefSetup3.class));
        overridePendingTransition(R.anim.slideinleft, R.anim.slideoutright);
    }

    public void active(View v){
        // Launch the activity to have the user enable our admin.
        ComponentName mDeviceAdminReceiver = new ComponentName(this, MyAdminReceiver.class);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,mDeviceAdminReceiver);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"helloworld");
        startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_ENABLE_ADMIN){
            if (resultCode==RESULT_OK)
                Toast.makeText(PreThiefSetup4.this, "超级管理员激活成功", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(PreThiefSetup4.this, "超级管理员激活失败，擦除数据和远程锁屏功能将无法使用",
                        Toast.LENGTH_SHORT).show();
        }
    }
}
