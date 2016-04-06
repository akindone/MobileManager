package com.jike.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jike.beans.BlackListItem;
import com.jike.db.BlackListDbHelper;

import java.util.ArrayList;

/**
 * Created by wancc on 2016/4/5.
 */
public class BlackListDao {
    Context ctx;
    private final SQLiteDatabase db;

    public BlackListDao(Context ctx) {
        BlackListDbHelper dbHelper = new BlackListDbHelper(ctx, null, null, 1);
        db = dbHelper.getReadableDatabase();
        this.ctx = ctx;
    }


    public long insert(String num,int mode){
        ContentValues values = new ContentValues();
        values.put("num",num);//num vachar(20),mode integer)
        values.put("mode",mode);
        long ret = db.insert("blacklist", null, values);
        return ret;
    }

    public int delete(String num){
        int ret = db.delete("blacklist", "num=?", new String[]{num});
        return ret;
    }

    public int update(String num,int mode){
        ContentValues values = new ContentValues();
        values.put("mode", mode);
        int ret = db.update("blacklist", values, "num=?", new String[]{num});
        return ret;
    }

    public ArrayList<BlackListItem> queryAll(){
        ArrayList<BlackListItem> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from blacklist;", null);
        while (cursor.moveToNext()){
            String num = cursor.getString(1);
            int mode = cursor.getInt(2);
            BlackListItem item = new BlackListItem(num, mode);
            list.add(item);
        }
        return list;

    }


    public ArrayList<BlackListItem> queryPart(int limit,int offset){
        ArrayList<BlackListItem> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from blacklist limit ? offset ?;", new String[]{limit+"",offset+""});
        while (cursor.moveToNext()){
            String num = cursor.getString(1);
            int mode = cursor.getInt(2);
            BlackListItem item = new BlackListItem(num, mode);
            list.add(item);
        }
        return list;

    }


    public int queryMode(String incomingNumber) {
        int mode=-1;
        Cursor cursor = db.rawQuery("select mode from blacklist where num=?;", new String[]{incomingNumber});
        if (cursor.moveToNext()){
            mode = cursor.getInt(0);
            return mode;
        }
        return mode;
    }


    public int queryNum() {
        Cursor cursor = db.rawQuery("select count(*) from blacklist ;", null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        return count;
    }
}
