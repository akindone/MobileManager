package com.jike.mobilemanager_jk;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jike.beans.AppInfo;
import com.jike.dao.LockAppDao;
import com.jike.service.MyLockAppService;
import com.jike.utils.PackageUtils;

import java.util.ArrayList;

public class PackageManagerActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "PackageManagerActivity";
    private TextView tv_pma_sdspace;
    private TextView tv_pma_romspace;
    private TextView tv_pma_reminder;
    private ListView lv_pma_content;

    private ArrayList<AppInfo> installedApps;
    private ArrayList<AppInfo> installedApps_user;
    private ArrayList<AppInfo> installedApps_system;


    /**因为卸载后会刷新，刷新又new了一个MyAsyncTask，所以如果不把变量作为成员变量，
    而放在MyAsyncTask中做成员变量，
    则每次刷新都会产生一个，导致无法删除的popupwindow*/
    private PopupWindow popupWindow;

    AppInfo curr_click_appinfo;
    AppInfo curr_longclick_appinfo;

    private MyAsyncTask myAsyncTask;
    private ArrayList<String> allPackagename;

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


        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();

        startService(new Intent(this, MyLockAppService.class));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_popup_start:
                start();
                break;
            case R.id.ll_popup_share:
                share();
                break;
            case R.id.ll_popup_uninstall:
                uninstall();
                break;
        }

    }

    /**
     * 卸载应用
     */
    private void uninstall() {

        //有两张不能卸载 系统自带的应用  该应用本身
        if (curr_click_appinfo.getPackagename().equals("com.jike.mobilemanager_jk"))
            Toast.makeText(PackageManagerActivity.this, "不能卸载应用本身", Toast.LENGTH_SHORT).show();
        else if (curr_click_appinfo.isSDcard())
            Toast.makeText(PackageManagerActivity.this, "不能卸载系统自带应用", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + curr_click_appinfo.getPackagename()));
            startActivityForResult(intent,100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            //刷新列表,因为一个task只能execute一次，所以要新new一个
            myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute();
        }
    }

    private void share() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "分享一个好玩的app，"+curr_click_appinfo.getName()+"下载地址为http://www.joy.com");
        startActivity(intent);
    }

    /**
     * PackageManager通过包名获取打开该应用的intent,参数在点击item时初始化
     */
    private void start() {
        Log.e(TAG,curr_click_appinfo.getPackagename());

        Intent intent=getPackageManager().getLaunchIntentForPackage(curr_click_appinfo.getPackagename());
        startActivity(intent);
    }

    class MyAsyncTask extends AsyncTask<Void,Void,Void> {

        private static final String TAG = "MyAsyncTask";
        private LinearLayout ll_popup_start;
        private LinearLayout ll_popup_share;
        private LinearLayout ll_popup_uninstall;
        private MyBaseAdapter myBaseAdapter;


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


            //获取被锁定的app的列表
            LockAppDao lockappDao =new LockAppDao(PackageManagerActivity.this);
            allPackagename = lockappDao.getAllPackagename();

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

            //判断一下adapte是否为空，如果不为空，则就不必重新new了，新new出来的listView又会从第一行开始显示
            if (myBaseAdapter==null){
                myBaseAdapter = new MyBaseAdapter();
                lv_pma_content.setAdapter(myBaseAdapter);
            }else
                myBaseAdapter.notifyDataSetChanged();


            //获取每版屏幕显示出的第一个item的position
            lv_pma_content.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    Log.e(TAG, "onScrollStateChanged");
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    Log.e(TAG, "onScroll");

                    if (popupWindow != null) {
                        popupWindow.dismiss();
                        popupWindow = null;
                    }

                    if (firstVisibleItem > installedApps_user.size())
                        tv_pma_reminder.setText("系统的应用：" + installedApps_system.size() + "个");
                    else
                        tv_pma_reminder.setText("用户安装的应用：" + installedApps_user.size() + "个");
                }
            });

            lv_pma_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e(TAG, "onItemClick");

                    //获取被点击的应用info
                    if (0 < position && position < installedApps_user.size() + 1)
                        curr_click_appinfo = installedApps_user.get(position - 1);
                    else if (installedApps_user.size() + 1 < position && position < installedApps.size() + 2)
                        curr_click_appinfo = installedApps_system.get(position - installedApps_user.size() - 2);

                    View v = View.inflate(PackageManagerActivity.this, R.layout.ll_popup_content, null);
                    ll_popup_start = (LinearLayout) v.findViewById(R.id.ll_popup_start);
                    ll_popup_share = (LinearLayout) v.findViewById(R.id.ll_popup_share);
                    ll_popup_uninstall = (LinearLayout) v.findViewById(R.id.ll_popup_uninstall);

                    ll_popup_start.setOnClickListener(PackageManagerActivity.this);
                    ll_popup_share.setOnClickListener(PackageManagerActivity.this);
                    ll_popup_uninstall.setOnClickListener(PackageManagerActivity.this);

                    //第一次点击的时候popupWindow显示，第二次点击消失
                    if (popupWindow == null) {
                        popupWindow = new PopupWindow(v,
                                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                    } else {
                        popupWindow.dismiss();
                        popupWindow = null;
                    }
                    int[] location = new int[2];
                    view.getLocationOnScreen(location);
                    popupWindow.showAtLocation(view, Gravity.TOP | Gravity.LEFT, location[0] + 150, location[1]);
                }
            });

            lv_pma_content.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    //获取被长按的应用info
                    if (0 < position && position < installedApps_user.size() + 1)
                        curr_longclick_appinfo = installedApps_user.get(position - 1);
                    else if (installedApps_user.size() + 1 < position && position < installedApps.size() + 2)
                        curr_longclick_appinfo = installedApps_system.get(position - installedApps_user.size() - 2);

                    ImageView iv_item_pma_lock = (ImageView) view.findViewById(R.id.iv_item_pma_lock);

                    String curr_longclick_packagename = curr_longclick_appinfo.getPackagename();
                    LockAppDao dao = new LockAppDao(PackageManagerActivity.this);
                    boolean locked = dao.isLocked(curr_longclick_packagename);
                    if (locked) {
                        iv_item_pma_lock.setImageResource(R.drawable.unlock);
                        dao.deleteFromDb(curr_longclick_packagename);
                    } else {
                        iv_item_pma_lock.setImageResource(R.drawable.lock);
                        dao.insertIntoDb(curr_longclick_packagename);
                    }
                    return true;//return true 则onItemClick就不执行
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
            //因为在listView中有两种view的对象 所有要做判断
            if (convertView!=null&& convertView instanceof RelativeLayout){
                view=convertView;
                holder = (Holder) view.getTag();
            }else {

                holder=new Holder();
                view= View.inflate(PackageManagerActivity.this, R.layout.listitem_appinfo_pma, null);
                holder.iv_item_pma_icon       = (ImageView) view.findViewById(R.id.iv_item_pma_icon);
                holder.tv_item_pma_label      = (TextView) view.findViewById(R.id.tv_item_pma_label);
                holder.tv_item_pma_isinSdcard = (TextView) view.findViewById(R.id.tv_item_pma_isinSdcard);
                holder.iv_item_pma_lock       = (ImageView)view.findViewById(R.id.iv_item_pma_lock);
                view.setTag(holder);
            }
            holder.iv_item_pma_icon.setBackground(appInfo.getIcon());
            holder.tv_item_pma_label      .setText(appInfo.getName());
            if (appInfo.isSDcard())
                holder.tv_item_pma_isinSdcard .setText("位于sd卡");
            else holder.tv_item_pma_isinSdcard .setText("位于Rom");
            if (allPackagename.contains(appInfo.getPackagename())){
                holder.iv_item_pma_lock.setImageResource(R.drawable.lock);
            } else holder.iv_item_pma_lock.setImageResource(R.drawable.unlock);

            return view;
        }
    }

    class Holder{
        private ImageView iv_item_pma_icon;
        private TextView tv_item_pma_label;
        private TextView tv_item_pma_isinSdcard;
        private ImageView iv_item_pma_lock;

    }
}
