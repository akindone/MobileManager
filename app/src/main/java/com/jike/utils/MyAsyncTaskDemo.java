package com.jike.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.net.URL;

/**
 * Created by wancc on 2016/3/31.
 */
public abstract class MyAsyncTaskDemo  {
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    postExecute();
                    break;
            }
        }
    };
    public abstract void doinbackground();//执行耗时操作

    public abstract void preExecute();//一些准备

    public abstract void postExecute();//在耗时操作完成后 在主线程

    /**
     * 执行流程
     */
    public void execute(){
        preExecute();
        new Thread(){
            @Override
            public void run() {
                super.run();
                doinbackground();
                handler.sendEmptyMessage(1);
            }
        }.start();
    }

}

class MyAsyncTask extends AsyncTask<URL, Integer, Float> {
    @Override
    protected void onPreExecute() {super.onPreExecute();}
    @Override
    protected Float doInBackground(URL...  params) {
        return null;
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
    @Override
    protected void onPostExecute(Float aFloat) {super.onPostExecute(aFloat);}
}

