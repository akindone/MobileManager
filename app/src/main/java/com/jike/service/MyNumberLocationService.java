package com.jike.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jike.application.MyApplication;
import com.jike.dao.NumberLocationDao;
import com.jike.mobilemanager_jk.R;

public class MyNumberLocationService extends Service {
    private boolean flag;

    public MyNumberLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //来电话时，查询归属地信息，并显示到界面上
        //获取telephonyManager
        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telManager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);


    }


    private class MyPhoneStateListener extends PhoneStateListener {

        private static final String TAG = "MyPhoneStateListener";
        private View inflate;
        private WindowManager wm;
        private LinearLayout ll_mytoast_bg;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    hideMyToast();
                    Log.e(TAG, "CALL_STATE_IDLE");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    showPhoneLocation(incomingNumber);
                    Log.e(TAG,"CALL_STATE_RINGING");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    hideMyToast();
                    Log.e(TAG, "CALL_STATE_OFFHOOK");
                    break;
            }
        }

        private void hideMyToast() {
            if (wm!=null&&flag){
                wm.removeView(inflate);
                flag=false;
            }
        }

        private void showPhoneLocation(String num) {
            String phoneLocation = NumberLocationDao.getPhoneLocation(null, num);
            showMyToast(phoneLocation);
        }

        private void showMyToast(String phoneLocation) {
            if (phoneLocation.isEmpty()){
                Toast.makeText(MyNumberLocationService.this, "输入号码不符合要求", Toast.LENGTH_SHORT).show();
                return;
            }
            inflate = View.inflate(MyNumberLocationService.this, R.layout.mytoast, null);
            ll_mytoast_bg = (LinearLayout) inflate.findViewById(R.id.ll_mytoast_bg);

            //动态给toast设置背景图片
            int resId_bg = MyApplication.getConfigValue("ll_mytoast_bg", R.drawable.call_locate_blue);
            ll_mytoast_bg.setBackgroundResource(resId_bg);

            TextView tv_mytoast_msg = (TextView) inflate.findViewById(R.id.tv_mytoast_msg);
            tv_mytoast_msg.setText(phoneLocation);

            wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            WindowManager.LayoutParams params=new WindowManager.LayoutParams();
            params.width=WindowManager.LayoutParams.WRAP_CONTENT;
            params.height=WindowManager.LayoutParams.WRAP_CONTENT;

           /* inflate.setLeft(MyApplication.getConfigValue("mytoast_location_x",200));
            inflate.setRight(MyApplication.getConfigValue("mytoast_location_y",300));*/

            params.format= PixelFormat.TRANSLUCENT;

            //动态指定window在屏幕上的位置
            params.x=MyApplication.getConfigValue("mytoast_location_x",100);//相对于屏幕
            params.y=MyApplication.getConfigValue("mytoast_location_y", 300);

            Log.e(TAG,MyApplication.getConfigValue("mytoast_location_x",200)+"***"+
                    MyApplication.getConfigValue("mytoast_location_y", 300));

            params.flags=WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    |WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    |WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            params.type=WindowManager.LayoutParams.TYPE_TOAST;

            wm.addView(inflate, params);
            flag=true;

        }
    }
}
