package com.jike.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import com.jike.beans.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wancc on 2016/3/31.
 */
public class PackageUtils {


    /**
     * 判断某个服务是否开启
     * @param  serviceName 服务的全类名
     * @param ctx
     * @return
     */

    public static boolean isServiceStarted(String serviceName,Context ctx){
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(200);
        for (ActivityManager.RunningServiceInfo service :
                runningServices) {
            String name = service.getClass().getName();
            if (name.equals(serviceName)){
                return true;
            }
        }
        return false;
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
}
