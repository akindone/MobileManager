package com.jike.mobilemanager_jk;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jike.beans.AppInfo;
import com.jike.utils.PackageUtils;

import java.util.ArrayList;

public class PackageManagerActivity extends Activity {

    private TextView tv_pma_sdspace;
    private TextView tv_pma_romspace;
    private TextView tv_pma_reminder;
    private ListView lv_pma_content;

    private ArrayList<AppInfo> installedApps;
    private ArrayList<AppInfo> installedApps_user;
    private ArrayList<AppInfo> installedApps_system;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_manager);

        tv_pma_sdspace = (TextView) findViewById(R.id.tv_pma_sdspace);
        tv_pma_romspace = (TextView) findViewById(R.id.tv_pma_romspace);
        lv_pma_content = (ListView) findViewById(R.id.lv_pma_content);
        tv_pma_reminder = (TextView) findViewById(R.id.tv_pma_reminder);

        String rom_space="ROM可用空间\r\n"+PackageUtils.getROMAvailableSpace(this);
        String sd_space="SDcard可用空间\r\n"+PackageUtils.getSdAvailableSpace(this);

        tv_pma_romspace.setText(rom_space);
        tv_pma_sdspace.setText(sd_space);

        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();


    }

    class MyAsyncTask extends AsyncTask<Void,Void,Void> {

        private static final String TAG = "MyAsyncTask";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * 在子线程执行耗时操作
         * @param params
         * @return
         */
        @Override
        protected Void doInBackground(Void... params) {
            //list  获得数据源
            installedApps = PackageUtils.getInstalledApps(PackageManagerActivity.this);
            //把应用列表分为两列
            installedApps_user = new ArrayList<>();
            installedApps_system = new ArrayList<>();

            for (int i = 0; i < installedApps.size(); i++) {
                if (installedApps.get(i).isSystem()) installedApps_system.add(installedApps.get(i));
                else installedApps_user.add(installedApps.get(i));
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        /**
         * 在DoInBackGround执行完后调用，在主线程执行
         * @param aVoid
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //把列表填充到lv
            lv_pma_content.setAdapter(new MyBaseAdapter());

            //获取屏幕显示出的第一个item的position
            lv_pma_content.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {}

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem > installedApps_user.size())
                        tv_pma_reminder.setText("系统的应用：" + installedApps_user.size() + "个");
                    else
                        tv_pma_reminder.setText("用户安装的应用：" + installedApps_system.size() + "个");
                }
            });
        }
    }

    class MyBaseAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return installedApps.size()+2;
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
            Holder holder;
            View view;
            AppInfo appInfo=null;
            if (position==0){
                TextView v=new TextView(PackageManagerActivity.this);
                v.setBackgroundColor(Color.GRAY);
                v.setText("用户安装的应用：" + installedApps_user.size() + "个");
                return v;
            } else if (position<installedApps_user.size()+1){
                appInfo  = installedApps_user.get(position - 1);
            } else if (position==installedApps_user.size()+1){
                TextView v=new TextView(PackageManagerActivity.this);
                v.setText("系统的应用："+installedApps_system.size()+"个");
                v.setBackgroundColor(Color.GRAY);
                return v;
            } else if (position<installedApps.size()+2){
                appInfo  = installedApps_system.get(position - installedApps_user.size()-2);
            }
            if (convertView!=null&& convertView instanceof RelativeLayout){
                view=convertView;
                holder = (Holder) view.getTag();
            }else {
                holder=new Holder();
                view= View.inflate(PackageManagerActivity.this, R.layout.listitem_appinfo_pma, null);
                holder.iv_item_pma_icon       = (ImageView) view.findViewById(R.id.iv_item_pma_icon);
                holder.tv_item_pma_label      = (TextView) view.findViewById(R.id.tv_item_pma_label);
                holder.tv_item_pma_isinSdcard = (TextView) view.findViewById(R.id.tv_item_pma_isinSdcard);
                view.setTag(holder);
            }
            holder.iv_item_pma_icon.setBackground(appInfo.getIcon());
            holder.tv_item_pma_label      .setText(appInfo.getName());
            if (appInfo.isSDcard())
                holder.tv_item_pma_isinSdcard .setText("位于sd卡");
            else holder.tv_item_pma_isinSdcard .setText("位于Rom");
            return view;
        }
    }

    class Holder{
        private ImageView iv_item_pma_icon;
        private TextView tv_item_pma_label;
        private TextView tv_item_pma_isinSdcard;
    }
}
