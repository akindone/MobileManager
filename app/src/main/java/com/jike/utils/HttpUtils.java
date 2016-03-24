package com.jike.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wancc on 2016/3/24.
 */
public class HttpUtils  {
    private static final String TAG = "HttpUtils";

    public static String getStringFromStream(InputStream is){
        String data="";

        byte[] bytes=new byte[1024];
        int len ;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//写入内存
        try {
            while((len=is.read(bytes))!=-1){
                baos.write(bytes,0,len);
            }
            data= baos.toString("GBK");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG,"getStringFromStream:"+data);
        return data;
    }
}
