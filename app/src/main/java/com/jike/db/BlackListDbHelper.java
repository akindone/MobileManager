package com.jike.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wancc on 2016/4/5.
 */
public class BlackListDbHelper extends SQLiteOpenHelper {
    public BlackListDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "blacklist.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table blacklist (id integer primary key, num vachar(20),mode integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
