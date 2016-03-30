package com.jike.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by wancc on 2016/3/30.
 */
public class WindowUtils {
    /**
     * 获取屏幕的metrics对象，从该对象获知屏幕宽高、密度
     * @param ctx
     * @return
     */
    public static DisplayMetrics getDisplayMetrix(Context ctx){
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        defaultDisplay.getMetrics(metrics);
        return metrics;
    }

    public static float getDensity(Context ctx){
        DisplayMetrics displayMetrix = getDisplayMetrix(ctx);
        return displayMetrix.density;
    }

    public static int getWidthPixels(Context ctx){
        DisplayMetrics displayMetrix = getDisplayMetrix(ctx);
        return displayMetrix.widthPixels;
    }

    public static int getHeightPixels(Context ctx){
        DisplayMetrics displayMetrix = getDisplayMetrix(ctx);
        return displayMetrix.heightPixels;
    }
}
