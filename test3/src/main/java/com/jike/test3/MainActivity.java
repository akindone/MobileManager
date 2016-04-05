package com.jike.test3;

import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageManager mPm = getPackageManager();
        try {
            final Class<?> pmClass = MainActivity.this.getClassLoader().loadClass("android.content.pm.PackageManager");

            final Method getPackageSizeInfo = pmClass.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);

            getPackageSizeInfo.invoke(mPm, getPackageName(), mStatsObserver);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void getCacheSize(View view) {
        PackageManager mPm = getPackageManager();
        try {
            final Class<?> pmClass = MainActivity.this.getClassLoader().loadClass("android.content.pm.PackageManager");

            final Method getPackageSizeInfo = pmClass.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);

            getPackageSizeInfo.invoke(mPm, getPackageName(), mStatsObserver);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub(){
        @Override
        public void onGetStatsCompleted(final PackageStats pStats, boolean succeeded) throws RemoteException {
            final String packageName = pStats.packageName;
            final long cacheSize = pStats.cacheSize;
            Log.e("onGetStatsCompleted", packageName+ "---" +cacheSize);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, packageName + "---" + cacheSize, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
}
