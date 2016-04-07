package com.jike.mobilemanager_jk;

import android.content.pm.ApplicationInfo;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.widget.TextView;

import com.jike.utils.PackageUtils;

import java.util.List;

public class DataUsageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_usage);

        TextView tv_datausage_rx = (TextView) findViewById(R.id.tv_datausage_rx);
        TextView tv_datausage_tx = (TextView) findViewById(R.id.tv_datausage_tx);
        TextView tv_datausage_total = (TextView) findViewById(R.id.tv_datausage_total);


        TrafficStats trafficStats = new TrafficStats();
        long mobileTxBytes = trafficStats.getMobileTxBytes();
        long mobileRxBytes = trafficStats.getMobileRxBytes();
        long total = mobileRxBytes+mobileTxBytes;

        String txSize = Formatter.formatFileSize(this, mobileTxBytes);
        String rxSize = Formatter.formatFileSize(this, mobileRxBytes);
        String totalSize = Formatter.formatFileSize(this, total);

        tv_datausage_rx.setText("下载总流量："+rxSize);
        tv_datausage_tx.setText("上传总流量："+txSize);
        tv_datausage_total.setText("总流量："+totalSize);

        List<ApplicationInfo> infoList = PackageUtils.getApplicationInfoList(this);
        for (ApplicationInfo info :
                infoList) {
            int uid = info.uid;
            long uidRxBytes = trafficStats.getUidRxBytes(uid);
            long uidTxBytes = trafficStats.getUidTxBytes(uid);
            String uidRxSize = Formatter.formatFileSize(this, uidRxBytes);
            String uidTxSize = Formatter.formatFileSize(this, uidTxBytes);
//            Log.e("DataUsageActivity",info.packageName+"---uidRxSize:"+uidRxSize+",uidTxSize"+uidTxSize);
        }


    }
}
