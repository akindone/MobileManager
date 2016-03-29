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
 * Created by wancc on 2016/3/29.
 */
public class NumberLocationDao {

    private static final String TAG = "NumberLocationDao";
    private static String fileName;



    //将数据库从assets目录下 copy到 data/data/packagename/
    public static boolean getdb(Context context,String dbName){
        fileName = "data/data/"+context.getPackageName()+"/"+dbName;
        if (new File(fileName).exists()) return false;

        AssetManager assets = context.getAssets();
        FileOutputStream fos=null;
        InputStream open=null;
        try {
            open = assets.open("naddress.db");
            fos = new FileOutputStream(fileName);
            byte[] bytes=new byte[1024];
            int len;
            while ((len= open.read(bytes))!=-1){
                fos.write(bytes,0,len);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"getdb IOException");
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

    public static String getPhoneLocation(Context context,String num){
        String location;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(fileName, null, Context.MODE_PRIVATE);
        String numprefix=num.substring(0, 7);
        Cursor cursor = db.rawQuery("select city,cardtype from address_tb where _id =" +
                "(select outkey from numinfo where mobileprefix =?);", new String[]{numprefix});
        cursor.moveToNext();
        int city = cursor.getColumnIndex("city");
        int cardtype = cursor.getColumnIndex("cardtype");
        String string_city = cursor.getString(city);
        String string_cardtype = cursor.getString(cardtype);

        location=string_city+string_cardtype;
        Log.e(TAG,"getPhoneLocation :"+location);
        return location;
    }
}
