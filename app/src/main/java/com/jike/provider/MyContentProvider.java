package com.jike.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.jike.db.MyDbHelper;

public class MyContentProvider extends ContentProvider {

    private SQLiteDatabase db;

    public MyContentProvider() {
    }

    /**
     * 应用一启动，该方法就被调用，在主线程里，所以尽量不做耗时的操作，甚至是getReadableDatabase，
     * 建议改写dbhelper的onOpen（）方法，但是怎么改写api没说
     * @return
     */
    @Override
    public boolean onCreate() {
        MyDbHelper myDbHelper = new MyDbHelper(getContext(), "lockapp.db", null, 1);
        db = myDbHelper.getReadableDatabase();

        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int delete = db.delete("lockapp", selection, selectionArgs);
        getContext()
                .getContentResolver()//ContentResolver 提供了access to context model
                .notifyChange(uri, null);
        return delete;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db.insert("lockapp", null, values);
        getContext()
                .getContentResolver()//ContentResolver 提供了access to context model
                .notifyChange(uri, null);
        return null;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor lockapp = db.query("lockapp", projection, selection, selectionArgs, sortOrder, null, null);
        return lockapp;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int row = db.update("lockapp", values, selection, selectionArgs);
        return row;
    }
}
