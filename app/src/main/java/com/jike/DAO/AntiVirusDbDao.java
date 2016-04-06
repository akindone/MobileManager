package com.jike.dao;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wancc on 2016/4/6.
 */
public class AntiVirusDbDao {

    private static final String TAG = "AntiVirusDbDao";
    private static String fileName;

    //将数据库从assets目录下 copy到 data/data/packagename/
    public static boolean getdb(Context context,String dbName){
        fileName = "data/data/"+context.getPackageName()+"/"+dbName;
        if (new File(fileName).exists()) return false;

        AssetManager assets = context.getAssets();
        FileOutputStream fos=null;
        InputStream open=null;
        try {
            open = assets.open("antivirus.db");
            fos = new FileOutputStream(fileName);
            byte[] bytes=new byte[1024];
            int len;
            while ((len= open.read(bytes))!=-1){
                fos.write(bytes,0,len);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "getdb IOException");
            return false;
        } finally {
            if (fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (open!=null){
                try {
                    open.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    public static boolean isVirus(String apkMd5) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(fileName, null, 0);
        Cursor cursor = db.rawQuery("select * from datable where  md5 =  ?", new String[]{apkMd5});
        if (cursor.moveToNext()){
            return true;
        }
        return false;
    }
}
