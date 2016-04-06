package com.jike.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.jike.dao.BlackListDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BlackListService extends Service {

    private static final String TAG = "BlackListService";
    private BlackListDao blackListDao;

    public BlackListService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        blackListDao = new BlackListDao(this);
        Log.e(TAG,"onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //拦截来电
        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telManager.listen(new MyPhoneStateListener(),PhoneStateListener.LISTEN_CALL_STATE);

        return super.onStartCommand(intent, flags, startId);
    }

    class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    int mode = blackListDao.queryMode(incomingNumber);
                    if (mode==1||mode==3){
                        Log.e("onCallStateChanged","endCall");
                        endCall();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
            }
        }

        private void endCall() {
            //挂断电话
//                        getITelephony().endCall();
//                        ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE))
//                        import android.os.ServiceManager;
//                        import com.android.internal.telephony.ITelephony;
            try {
                Class<?> aClass = Class.forName("android.os.ServiceManager");
                Method getService = aClass.getMethod("getService", String.class);
                IBinder iBinder = (IBinder) getService.invoke(null, Context.TELEPHONY_SERVICE);//返回值？？？类型 Ibinder

                ITelephony.Stub.asInterface(iBinder).endCall();
                Log.e("onCallStateChanged", "endCall");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    public class MySmsBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //拦截短信 只适用与4.4版本下
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object obj:pdus) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);
                String add = message.getOriginatingAddress();
                if (blackListDao==null){
                    blackListDao = new BlackListDao(BlackListService.this);
                }
                int mode = blackListDao.queryMode(add);
                if (mode==1||mode==3){
                    abortBroadcast();
                    Log.e("abortBroadcast","abortBroadcast");
                }
            }
        }
    }
}
