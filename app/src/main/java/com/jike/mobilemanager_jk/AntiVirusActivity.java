package com.jike.mobilemanager_jk;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jike.beans.AppResult;
import com.jike.dao.AntiVirusDbDao;
import com.jike.utils.MD5Utils;
import com.jike.utils.PackageUtils;

import java.util.ArrayList;
import java.util.List;

public class AntiVirusActivity extends Activity {


    private ListView lv_antivirus_content;
    private TextView tv_antivirus_text;
    private ProgressBar pb_antivirus_progress;

    private MyAntiVirusAdapter adapter;
    private ArrayList<AppResult> appResults;
    private RotateAnimation rotateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_virus);

        lv_antivirus_content = (ListView) findViewById(R.id.lv_antivirus_content);
        tv_antivirus_text = (TextView) findViewById(R.id.tv_antivirus_text);
        pb_antivirus_progress = (ProgressBar) findViewById(R.id.pb_antivirus_progress);

        appResults = new ArrayList<>();

        adapter = new MyAntiVirusAdapter();
        lv_antivirus_content.setAdapter(adapter);

        new MyAntiVirusAsyncTask().execute();

        startScan();

    }

    class MyAntiVirusAsyncTask extends AsyncTask<Void,AppResult,Void>{

        private List<ApplicationInfo> infoList;
        int count = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            infoList = PackageUtils.getApplicationInfoList(AntiVirusActivity.this);
            pb_antivirus_progress.setMax(infoList.size());

        }

        @Override
        protected Void doInBackground(Void... params) {

            for (final ApplicationInfo appInfo : infoList) {

                String sourceDir = appInfo.sourceDir;//获得apk所在的路径

                String apkMd5 = MD5Utils.getApkMd5(sourceDir);

                boolean isVirus = AntiVirusDbDao.isVirus(apkMd5);


                AppResult appResult = new AppResult(appInfo.packageName, isVirus);
                publishProgress(appResult);
                count++;


                //java.lang.IllegalStateException: The content of the adapter has changed but ListView did not receive a notification.
                //因为增加元素和 notifydatasetChange 不在同一个同步代码块中，会造成异步
                //if (mItemCount != mAdapter.getCount()) {throw new IllegalStateException(xxxx)}

               /* appResults.add(0,new AppResult(appInfo.packageName,isVirus));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_antivirus_text.setText(appInfo.packageName);
                    }
                });
                publishProgress(++count);*/

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(AppResult... values) {
            super.onProgressUpdate(values);

            appResults.add(0,values[0]);
            tv_antivirus_text.setText(values[0].getLabel());
            adapter.notifyDataSetChanged();
            pb_antivirus_progress.setProgress(appResults.size());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tv_antivirus_text.setText("扫描完成！");
            rotateAnimation.cancel();
        }
    }

    class MyAntiVirusAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return appResults.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AppResult appResult = appResults.get(position);
            TextView textView;
            if (convertView!=null){
                textView= (TextView) convertView;
            } else textView = new TextView(AntiVirusActivity.this);
            textView.setText(appResult.getLabel());
            if (appResult.isVirus()) textView.setTextColor(Color.RED);

            return textView;
        }
    }

    private void startScan() {
        ImageView iv_antivirus_scanner_act = (ImageView) findViewById(R.id.iv_antivirus_scanner_act);
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5F,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(5000);
        rotateAnimation.setRepeatCount(-1);
        iv_antivirus_scanner_act.setAnimation(rotateAnimation);

        rotateAnimation.start();
    }
}
