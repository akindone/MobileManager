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
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jike.application.MyApplication;
import com.jike.dao.NumberLocationDao;
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
    private static final String TAG ="SplashActivity" ;
    private  static final String PATH= "http://192.168.3.8/MobileManager/";

    private static final int MSG_OK = 1;//TO TEST 不能写0,
    private static final int MSG_ERROR_NOTFOUND = -1;
    private static final int MSG_ERROR_SERVER = -2;
    private static final int MSG_ERROR_IO =-3 ;
    private static final int MSG_ERROR_JSON=-4;
    private static final int MSG_ERROR_FNFE =-5 ;
    private static final int MSG_ERROR_NNFE =-6 ;
    private static final int MSG_ERROR_HTTPUTILS = -7;
    private static final int MSG_WAIT_TIMEOUT = -8;

    private String appVersionName;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_OK:
                    String[] strings = (String[]) msg.obj;
                    Log.e(TAG, "handleMessage");
                    ifUpdate(strings);
                    break;
                case MSG_ERROR_NOTFOUND:
                    Toast.makeText(SplashActivity.this, MSG_ERROR_NOTFOUND+"", Toast.LENGTH_SHORT).show();
                    gotoHome();
                    break;
                case MSG_ERROR_SERVER:
                    Toast.makeText(SplashActivity.this, MSG_ERROR_SERVER+"", Toast.LENGTH_SHORT).show();
                    gotoHome();
                    break;
                case MSG_ERROR_IO:
                    Toast.makeText(SplashActivity.this, MSG_ERROR_IO+"", Toast.LENGTH_SHORT).show();
                    gotoHome();
                    break;
                case MSG_ERROR_JSON:
                    Toast.makeText(SplashActivity.this, MSG_ERROR_JSON+"", Toast.LENGTH_SHORT).show();
                    gotoHome();
                    break;
                case MSG_ERROR_FNFE:
                    Toast.makeText(SplashActivity.this, MSG_ERROR_FNFE+"", Toast.LENGTH_SHORT).show();
                    gotoHome();
                    break;
                case MSG_ERROR_NNFE:
                    Toast.makeText(SplashActivity.this, MSG_ERROR_NNFE+"", Toast.LENGTH_SHORT).show();
                    gotoHome();
                    break;
                case MSG_ERROR_HTTPUTILS:
                    Toast.makeText(SplashActivity.this, MSG_ERROR_HTTPUTILS+"", Toast.LENGTH_SHORT).show();
                    gotoHome();
                    break;
                case MSG_WAIT_TIMEOUT:
                    //Toast.makeText(SplashActivity.this, MSG_WAIT_TIMEOUT+"", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashActivity.this,Home.class));
                    finish();
                    break;
            }
        }
    };
    private ProgressBar pb_splash_download;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        appVersionName = getAppVersionName();
        TextView tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        pb_splash_download = (ProgressBar) findViewById(R.id.pb_splash_download);

        tv_splash_version.setText("手机卫士 版本号：" + appVersionName);

        boolean getdb = NumberLocationDao.getdb(this, "phoneLocation.db");
        if (!getdb){
            Log.e(TAG,"数据库导入失败,或该目录下已经存在数据库文件");
        }
        boolean autoUpdate = MyApplication.config.getBoolean("autoUpdate", true);
        if (autoUpdate) {getLatestVersion();}
        else gotoHome();
    }

    private void ifUpdate(final String[] info) {
        String sname = SplashActivity.this.getClass().getName();
        String name = this.getClass().getName();
        Log.e(TAG,"ifUpdate  THIS'S CLASSNAME："+sname+","+name);
        Log.e(TAG, "ifUpdate");
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
                       gotoHome();
                   }
               })
               .show();

    }

    private void waitaWhile(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Message message = handler.obtainMessage();
                try {
                    Thread.currentThread().sleep(100);
                    message.what=MSG_WAIT_TIMEOUT;
                } catch (InterruptedException e) {
                    e.printStackTrace();

                } finally {
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private void gotoHome() {
        waitaWhile();
    }

    private void download(String path) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(path, new AsyncHttpResponseHandler() {
            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                pb_splash_download.setVisibility(View.VISIBLE);
                pb_splash_download.setMax((int) totalSize);
                pb_splash_download.setProgress((int)bytesWritten);
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Message message = handler.obtainMessage();
                //把bytes数据保存到sd卡
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/lastestMobileManager.apk");
                Log.e(TAG, "download FILEPATH:" + file.getAbsolutePath());
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    fos.write(bytes);
                    Toast.makeText(SplashActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(this, "下载成功", Toast.LENGTH_SHORT).show();
                    install(file);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    message.what=MSG_ERROR_FNFE;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what=MSG_ERROR_IO;
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            message.what=MSG_ERROR_IO;
                        }
                    }
                    handler.sendMessage(message);
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
        startActivityForResult(intent, 100);//如果用户下载后不安装，要让界面可以跳转到主页，则要回调onActivityResult（）
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100)
            gotoHome();
    }

    private void getLatestVersion() {
        Log.e(TAG,"getLatestVersion");
        //联网获取最新版本信息
        new Thread(){
            @Override
            public void run() {
                super.run();
                String path=PATH+"latestVersion.json";
                Message message = handler.obtainMessage();
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(2000);
                    conn.setReadTimeout(2000);
                    conn.connect();
                    int responseCode = conn.getResponseCode();
                    Log.e(TAG,"responseCode"+responseCode);

                    if (responseCode/100==4){
                        message.what=MSG_ERROR_NOTFOUND;
                    } else if (responseCode/100==5){
                        message.what=MSG_ERROR_SERVER;
                    } else if (responseCode/100==2){
                        Log.e(TAG,"getLatestVersion SCUCESS");
                        InputStream inputStream = conn.getInputStream();

                        //工具类 把流转为字符串
                        String data= HttpUtils.getStringFromStream(inputStream);
                        if (data.equals("")){
                            message.what=MSG_ERROR_HTTPUTILS;
                        }else{
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
                                message.what=MSG_OK;
                                message.obj=msgs;
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        gotoHome();
                                    }
                                });
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what=MSG_ERROR_IO;
                    Log.e(TAG,"getLatestVersion IOException");
                } catch (JSONException e) {
                    e.printStackTrace();
                    message.what=MSG_ERROR_JSON;
                    Log.e(TAG,"getLatestVersion JSONException");
                } finally {
                    handler.sendMessage(message);
                }
            }
        }.start();
    }


    /**
     * 获取应用的版本名称
     * @return
     */
    public String getAppVersionName() {
        Log.e(TAG,"getAppVersionName");
        String appVersionName="";
        PackageManager packageManager = getPackageManager();
        Message message = handler.obtainMessage();//有可能发生异常
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            message.what=MSG_ERROR_NNFE;
        } finally {
            handler.sendMessage(message);//有可能发生异常，为了防止崩溃，让handler去处理异常
        }
        return appVersionName;
    }
}
