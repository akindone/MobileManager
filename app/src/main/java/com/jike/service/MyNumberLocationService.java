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
import android.widget.TextView;

import com.jike.dao.NumberLocationDao;
import com.jike.mobilemanager_jk.R;

public class MyNumberLocationService extends Service {
    public MyNumberLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
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

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
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
            if (wm!=null){
                wm.removeView(inflate);
            }
        }

        private void showPhoneLocation(String num) {
            String phoneLocation = NumberLocationDao.getPhoneLocation(null, num);
            showMyToast(phoneLocation);

        }

        private void showMyToast(String phoneLocation) {
            inflate = View.inflate(MyNumberLocationService.this, R.layout.mytoast, null);
            TextView tv_mytoast_msg = (TextView) inflate.findViewById(R.id.tv_mytoast_msg);
            tv_mytoast_msg.setText(phoneLocation);

            wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            WindowManager.LayoutParams params=new WindowManager.LayoutParams();
            params.width=WindowManager.LayoutParams.WRAP_CONTENT;
            params.height=WindowManager.LayoutParams.WRAP_CONTENT;
            params.flags=WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    |WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    |WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            params.format= PixelFormat.TRANSLUCENT;
            params.type=WindowManager.LayoutParams.TYPE_TOAST;


            wm.addView(inflate, params);
        }
    }
}
