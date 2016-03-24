package com.jike.mobilemanager_jk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.jike.utils.HttpUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends Activity {

    private static final int MSG_OK = 0;
    private static final String TAG ="SplashActivity" ;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_OK:
                    String[] strings = (String[]) msg.obj;
                    Log.e(TAG,"handleMessage");

                    ifUpdate(strings);
                    break;
            }
        }
    };

    private String appVersionName;
    private  static final String PATH= "http://192.168.28.1/MobileManager/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        Log.e(TAG,"onCreate");
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.currentThread().sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        appVersionName = getAppVersionName();
        getLatestVersion();
    }

    private void ifUpdate(final String[] info) {
       new AlertDialog.Builder(this)
               .setTitle("出新版本啦")
               .setMessage(info[1])
               .setPositiveButton("去更新", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       //下载apk
                       String loadurl=PATH+info[2];
                       download(loadurl);
                   }
               })
               .setNegativeButton("下次吧", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       //TODO 跳转到主页
                   }
               })
               .show();

    }

    private void download(String path) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(path, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                //把bytes数据保存到sd卡
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/lastestMobileManager.apk");
                Log.e(TAG,"download FILEPATH:"+file.getAbsolutePath());
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    fos.write(bytes);
                    Toast.makeText(SplashActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                    install(file);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void install(File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        Log.e(TAG,"install");
        startActivity(intent);
    }


    private void getLatestVersion() {
        Log.e(TAG,"getLatestVersion");
        //联网获取最新版本信息
        new Thread(){
            @Override
            public void run() {
                super.run();
                String path=PATH+"latestVersion.json";
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.connect();
                    int responseCode = conn.getResponseCode();
                    if (responseCode==200){
                        Log.e(TAG,"getLatestVersion SCUCESS");
                        InputStream inputStream = conn.getInputStream();

                        //工具类 把流转为字符串
                        String data= HttpUtils.getStringFromStream(inputStream);

                        //json解析
                        JSONObject jsonObject = new JSONObject(data);
                        String lastestVersionName = jsonObject.getString("versionName");
                        String discription = jsonObject.getString("discription");
                        String urlpath = jsonObject.getString("path");
                        String[] msgs={lastestVersionName,discription,urlpath};
                        Log.e(TAG,"getLatestVersion json:"+lastestVersionName+","+discription+","+urlpath);

                        float version = Float.parseFloat(appVersionName);
                        float latestVer = Float.parseFloat(lastestVersionName);
                        Log.e(TAG,"getLatestVersion:"+(latestVer>version));

                        if (latestVer>version){
                            Message message = handler.obtainMessage(MSG_OK, msgs);
                            handler.sendMessage(message);
                        } else {
                            //TODO 进入主页
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public String getAppVersionName() {
        Log.e(TAG,"getAppVersionName");
        String appVersionName="";
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersionName;
    }
}
