package com.jike.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.jike.mobilemanager_jk.R;

/**
 * 业务逻辑:点击一键清理的button后，widget发广播给进程管理，进程管理接收广播后执行清理当前所有可用进程
 * 清理完后，把正在执行的进程数和可用ram大小发回给widget
 * widget把数据显示出来
 * Created by wancc on 2016/4/3.
 */
public class MyAppWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "MyAppWidgetProvider";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.e(TAG,"onReceive");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.e(TAG, "onUpdate");
        Intent intent = new Intent();
        intent.setAction("com.jike.mobilemanager_jk.widget");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,100,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.processmanager_appwidget);
        remoteViews.setOnClickPendingIntent(R.id.btn_widget_clear, pendingIntent);

        ComponentName provider =new ComponentName(context,MyAppWidgetProvider.class);
        appWidgetManager.updateAppWidget(provider,remoteViews);

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.e(TAG, "onDeleted");

    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.e(TAG, "onEnabled");

    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.e(TAG, "onDisabled");

    }


}
