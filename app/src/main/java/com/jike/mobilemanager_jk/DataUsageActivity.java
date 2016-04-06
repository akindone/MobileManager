package com.jike.mobilemanager_jk;

import android.content.pm.ApplicationInfo;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.util.Log;

import com.jike.utils.PackageUtils;

import java.util.List;

public class DataUsageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_usage);

        TrafficStats trafficStats = new TrafficStats();
        long mobileTxBytes = trafficStats.getMobileTxBytes();
        long mobileRxBytes = trafficStats.getMobileRxBytes();

        String txSize = Formatter.formatFileSize(this, mobileTxBytes);
        String rxSize = Formatter.formatFileSize(this, mobileRxBytes);

        Log.e("DataUsageActivity","txSize:"+txSize);
        Log.e("DataUsageActivity", "rxSize:" + rxSize);

        List<ApplicationInfo> infoList = PackageUtils.getApplicationInfoList(this);
        for (ApplicationInfo info :
                infoList) {
            int uid = info.uid;
            long uidRxBytes = trafficStats.getUidRxBytes(uid);
            long uidTxBytes = trafficStats.getUidTxBytes(uid);
            String uidRxSize = Formatter.formatFileSize(this, uidRxBytes);
            String uidTxSize = Formatter.formatFileSize(this, uidTxBytes);
            Log.e("DataUsageActivity",info.packageName+"---uidRxSize:"+uidRxSize+",uidTxSize"+uidTxSize);
        }


    }
}
