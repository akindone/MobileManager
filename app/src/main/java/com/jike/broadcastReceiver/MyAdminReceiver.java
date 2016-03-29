package com.jike.broadcastReceiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by wancc on 2016/3/29.
 */
public class MyAdminReceiver extends DeviceAdminReceiver {
    void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, "admin_receiver_status_enabled");
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "admin_receiver_status_disable_warning";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, "admin_receiver_status_disabled");
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        showToast(context,"admin_receiver_status_pw_changed");
    }



}
