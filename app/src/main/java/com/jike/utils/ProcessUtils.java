package com.jike.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.text.format.Formatter;

import com.jike.beans.ProcessInfo;
import com.jike.mobilemanager_jk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wancc on 2016/4/2.
 */
public class ProcessUtils {


    /**
     * 获取当前运行的进程
     * @param ctx
     * @return
     */
    public static ArrayList<ProcessInfo>  getAllProcess(Context ctx) {
        ArrayList<ProcessInfo> processInfos = new ArrayList<>();

        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();

        PackageManager packageManager = ctx.getPackageManager();



        for(ActivityManager.RunningAppProcessInfo processInfo:runningAppProcesses){

            String pkgname=processInfo.processName;

            int pid=processInfo.pid;
            Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{pid});
            int totalPss = processMemoryInfo[0].getTotalPss();//kB

            Drawable icon=ctx.getResources().getDrawable(R.mipmap.ic_launcher);
            String label=pkgname;
            boolean isSystem=false;
            try {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(pkgname, 0);
                if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM)!=0)
                    isSystem = true ;

                label = appInfo.loadLabel(packageManager).toString();

                icon = packageManager.getApplicationIcon(pkgname);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } finally {
                ProcessInfo procInfo = new ProcessInfo(label, icon, totalPss + "", isSystem, pkgname, false);
                processInfos.add(procInfo);
            }
        }

        return processInfos;
    }

    /**
     * 获取当前运行的进程数
     * @param ctx
     * @return
     */
    public static String getAllProcessNum(Context ctx) {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();

        return runningAppProcesses.size()+"";
    }

    /**
     * 获取可用的ram大小
     * @param ctx
     * @return
     */
    public static String getTotalRAM (Context ctx) {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo= new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        long totalMem = outInfo.totalMem;
        String size = Formatter.formatFileSize(ctx, totalMem);
        return size;
    }

    /**
     * 获取ram的总大小
     * @param ctx
     * @return
     */
    public static String getAvailableRAM(Context ctx) {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo= new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        long availMem = outInfo.availMem;
        String size = Formatter.formatFileSize(ctx, availMem);
        return size;
    }
}
