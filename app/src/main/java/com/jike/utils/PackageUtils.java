package com.jike.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import com.jike.beans.AppInfo;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wancc on 2016/3/31.
 */
public class PackageUtils {

    private static final String TAG = "PackageUtils";
    private static long cacheSize;

    public PackageUtils() {
    }

    /**
     * 清除应用缓存
     * @param packageName
     */
    public static void clearCache(String packageName) {

    }


    /**
     * 获取应用的缓存大小,因为涉及多线程 所以每次最好获取一次
     *
     */
    public static String getAppCacheSize(Context ctx,String packagename) {


        IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub(){

            @Override
            public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                final String packageName = pStats.packageName;
                cacheSize = pStats.cacheSize;
                Log.e("onGetStatsCompleted", packageName + "---" + cacheSize);
            }
        };

        PackageManager mPm = ctx.getPackageManager();
        try {
            final Class<?> pmClass = ctx.getClassLoader().loadClass("android.content.pm.PackageManager");
            final Method getPackageSizeInfo = pmClass.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            getPackageSizeInfo.invoke(mPm, packagename, mStatsObserver);

            Thread.sleep(100);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.e(TAG,"getAppCacheInfo");
        return Formatter.formatFileSize(ctx,cacheSize);


    }


    /**
     * 判断某个服务是否开启
     * @param  serviceName 服务的全类名
     * @param ctx
     * @return
     */
    public static boolean isServiceStarted(String serviceName,Context ctx){
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(200);
        for (ActivityManager.RunningServiceInfo serviceInfo :
                runningServices) {
            String name = serviceInfo.service.getClassName();
            Log.e("service name",name);
            if (name.equals(serviceName)){
                return true;
            }
        }
        return false;
    }


    /**
     * 获取所有安装的应用的个数
     * @param ctx
     * @return
     */
    public static int  getInstallAppNum(Context ctx){
        PackageManager pm = ctx.getPackageManager();
        List<ApplicationInfo> infoList = pm.getInstalledApplications(0);
        return infoList.size();
    }

    /**
     * 获取所有安装的应用的信息
     * @param ctx
     * @return
     */
    public static ArrayList<AppInfo> getInstalledApps(Context ctx){
        ArrayList<AppInfo> appInfos=new ArrayList<>();

        PackageManager pm = ctx.getPackageManager();
        List<ApplicationInfo> infoList = pm.getInstalledApplications(0);
        for (ApplicationInfo info :
                infoList) {

            CharSequence label = info.loadLabel(pm);
            Drawable icon = info.loadIcon(pm);
            boolean isSDcard=false;
            boolean isSystem=false;
            if ((info.flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0){
                isSDcard=true;
            }
            if ((info.flags&ApplicationInfo.FLAG_SYSTEM)!=0){
                isSystem=true;
            }

            AppInfo appInfo = new AppInfo(label.toString(), icon, isSDcard, isSystem,info.packageName);
            appInfos.add(appInfo);
        }
        return appInfos;
    }

    /**
     * 获得当前sd卡可用空间
     * @param ctx 上下文
     * @return
     */
    public static String getSdAvailableSpace(Context ctx){
        long size_byte;
        long blockSizeLong;
        long availableBlocksLong;
        File directory = Environment.getExternalStorageDirectory();
        StatFs statFs=new StatFs(directory.getAbsolutePath());
        if (Build.VERSION.SDK_INT>=18){
            blockSizeLong = statFs.getBlockSizeLong();
            availableBlocksLong = statFs.getAvailableBlocksLong();

        }else {
            blockSizeLong = statFs.getBlockSize();
            availableBlocksLong = statFs.getAvailableBlocks();
        }

        size_byte=blockSizeLong*availableBlocksLong;
        String size_m = Formatter.formatFileSize(ctx, size_byte);
        return size_m;
    }

    /**
     * 获得当前ROM的可用空间
     * @param ctx 上下文
     * @return
     */
    public static String getROMAvailableSpace(Context ctx){
        long size_byte;
        long blockSizeLong;
        long availableBlocksLong;
        File directory = Environment.getDataDirectory();
        StatFs statFs=new StatFs(directory.getAbsolutePath());
        if (Build.VERSION.SDK_INT>=18){
            blockSizeLong = statFs.getBlockSizeLong();
            availableBlocksLong = statFs.getAvailableBlocksLong();

        }else {
            blockSizeLong = statFs.getBlockSize();
            availableBlocksLong = statFs.getAvailableBlocks();
        }

        size_byte=blockSizeLong*availableBlocksLong;
        String size_m = Formatter.formatFileSize(ctx, size_byte);
        return size_m;
    }


    public static List<ApplicationInfo> getApplicationInfoList(Context ctx) {
        PackageManager pm = ctx.getPackageManager();
        List<ApplicationInfo> infoList = pm.getInstalledApplications(0);
        return infoList;
    }


}
