package com.jike.mobilemanager_jk;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jike.beans.AppInfo;
import com.jike.beans.CacheInfo;
import com.jike.utils.PackageUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ScanCacheActivity extends Activity {

    private ProgressBar pb_scancache_progress;
    private TextView tv_scancache_appname;
    private TextView tv_scancache_state;
    private ListView lv_scancache_content;
    private Button bt_scancache_clear;


    private ArrayList<CacheInfo> cacheInfos;
    private PackageManager mPm;
    private MyCacheAdapter myCacheAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_cache);

       /* long appCacheInfo = PackageUtils.getAppCacheInfo(this, getPackageName());
        Toast.makeText(ScanCacheActivity.this, "appCacheInfo:"+appCacheInfo, Toast.LENGTH_SHORT).show();*/

        pb_scancache_progress = (ProgressBar) findViewById(R.id.pb_scancache_progress);
        tv_scancache_appname = (TextView) findViewById(R.id.tv_scancache_appname);
        tv_scancache_state = (TextView) findViewById(R.id.tv_scancache_state);
        lv_scancache_content = (ListView) findViewById(R.id.lv_scancache_content);
        bt_scancache_clear = (Button) findViewById(R.id.bt_scancache_clear);


        MyScanCacheAsyncTask mAsyncTask = new MyScanCacheAsyncTask();
        mAsyncTask.execute();

    }

    class MyScanCacheAsyncTask extends AsyncTask<Void,Integer,Void>{
        private static final String TAG = "MyScanCacheAsyncTask";
        int count=0;


        @Override
        protected void onPreExecute() {
            cacheInfos = new ArrayList<>();
            int appNum = PackageUtils.getInstallAppNum(ScanCacheActivity.this);
            pb_scancache_progress.setMax(appNum);


            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<AppInfo> installedApps = PackageUtils.getInstalledApps(ScanCacheActivity.this);

            mPm = ScanCacheActivity.this.getPackageManager();



            for (AppInfo appInfo:
            installedApps) {
                String packagename = appInfo.getPackagename();

                try {
                    final Class<?> pmClass = ScanCacheActivity.this.getClassLoader().loadClass("android.content.pm.PackageManager");
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


                publishProgress(++count);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pb_scancache_progress.setProgress(values[0]);
        }

        @Override
         protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (cacheInfos.size()>0){
                myCacheAdapter = new MyCacheAdapter();
                lv_scancache_content.setAdapter(myCacheAdapter);
                bt_scancache_clear.setVisibility(View.VISIBLE);
                tv_scancache_state.setText("扫描完成！");

            } else {
                Toast.makeText(ScanCacheActivity.this, "没有缓存", Toast.LENGTH_SHORT).show();
            }
        }
    }

    IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub(){

        /**
         * 在调用mPm.getPackageSizeInfo()的时候，系统会回调该方法，该方法运行在子线程，要注意线程安全。
         * 此处用一个cacheInfos的列表（位于堆上，线程共享）去接收封装好的对象
         * @param pStats
         * @param succeeded
         * @throws RemoteException
         */
        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            final String packageName = pStats.packageName;
            long cacheSize = pStats.cacheSize;
            final String cacheSizeString = Formatter.formatFileSize(ScanCacheActivity.this, cacheSize);
            if (cacheSize>12*1024){
                try {
                    ApplicationInfo applicationInfo = mPm.getApplicationInfo(packageName, 0);
                    CharSequence label = applicationInfo.loadLabel(mPm);
                    Drawable icon = applicationInfo.loadIcon(mPm);

                    CacheInfo cacheInfo = new CacheInfo(label.toString(), icon,cacheSizeString, packageName);

                    cacheInfos.add(cacheInfo);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_scancache_appname.setText(packageName+":"+cacheSizeString);
                        }
                    });
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    };


    /**
     * 清理所有缓存
     * 向系统申请一个很大的内存（Long.MAX_VALUE），系统为了腾出空间，就会把所有的缓存文件删除
     * @param view
     */
    public void clearCache(View view) {
        try {
//            Class<?> aClass = Class.forName("android.content.pm.PackageManager");
            Class<?> aClass = ScanCacheActivity.this.getClassLoader().loadClass("android.content.pm.PackageManager");
            Method method = aClass.getDeclaredMethod("freeStorageAndNotify", Long.TYPE, IPackageDataObserver.class);
            method.invoke(mPm,Long.MAX_VALUE,new MyIPackageDataObserver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    class MyIPackageDataObserver extends IPackageDataObserver.Stub{

        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
            Log.e("onRemoveCompleted",succeeded+"");//删除成功后返回的居然是false！！！
            cacheInfos.clear();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myCacheAdapter.notifyDataSetChanged();
                }
            });
        }
    }


    class MyCacheAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return cacheInfos.size();
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
            View view = View.inflate(ScanCacheActivity.this, R.layout.listitem_scancache, null);
            ImageView iv_item_scancache_icon = (ImageView) view.findViewById(R.id.iv_item_scancache_icon);
            TextView tv_item_scancache_label = (TextView) view.findViewById(R.id.tv_item_scancache_label);
            TextView tv_item_cachesize = (TextView) view.findViewById(R.id.tv_item_cachesize);

            CacheInfo cacheInfo = cacheInfos.get(position);

            iv_item_scancache_icon.setImageDrawable(cacheInfo.getIcon());
            tv_item_scancache_label.setText(cacheInfo.getLabel());
            tv_item_cachesize.setText(cacheInfo.getCacheSize());

            return view;
        }
    }
}
