package com.jike.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.jike.dao.LockAppDao;
import com.jike.mobilemanager_jk.LockAppActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MyLockAppService extends Service {

    private static final String TAG = "MyLockAppService";

//    ArrayList<String> ignoreAppList=new ArrayList<String>();
    HashSet ignoreAppSet = new HashSet<>();

    private String currpkgname;
    private LockAppDao lockAppDao;
    private ArrayList<String> allPackagename;

    public MyLockAppService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //借住dao，获取所有被加锁的app包名
        lockAppDao = new LockAppDao(this);
        allPackagename = lockAppDao.getAllPackagename();


        //时刻检测当前开启的应用
        final ActivityManager activityMng = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        //动态注册广播
        IntentFilter filter=new IntentFilter("com.jike.mobilemanager_jk.lockapp");
        registerReceiver(new MyIgnoreLockAppReceiver(),filter);

        getContentResolver().registerContentObserver(Uri.parse("content://com.jike.mobilemanager_jk"), false, new MyObserver(new Handler()));


        new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //获取当前位于前台的activity
                    List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityMng.getRunningAppProcesses();
                    ActivityManager.RunningAppProcessInfo runningAppProcessInfo = runningAppProcesses.get(0);
                    currpkgname = runningAppProcessInfo.processName;
                    Log.e(TAG,"runningAppProcessInfo");


                    if (allPackagename.contains(currpkgname)&&!ignoreAppSet.contains(currpkgname)){
                        //锁定
                        Intent intent1 = new Intent(MyLockAppService.this, LockAppActivity.class);
                        intent1.putExtra("packagename", currpkgname);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent1);
                    }
                }
            }
        }.start();




        return super.onStartCommand(intent, flags, startId);
    }

    class MyIgnoreLockAppReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String ignorePkgname = intent.getStringExtra("tempIgnorePkgname");
            ignoreAppSet.add(ignorePkgname);
            Log.e(TAG,"MyIgnoreLockAppReceiver:"+ignorePkgname);
        }
    }


    class MyObserver extends ContentObserver{


        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //刷新包名list
            allPackagename = lockAppDao.getAllPackagename();
        }
    }
}
