package com.jike.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

/**
 * 用该类对数据进行操作，但考虑到要能观察数据库变化，就不直接用该类去操作数据库，而是发给内容提供者去增删改查
 * Created by wancc on 2016/4/1.
 */
public class LockAppDao {
    Context ctx;//用它来拿到contentResolver
    private final ContentResolver resolver;
    String uriString ="content://com.jike.mobilemanager_jk/lockapp";
    Uri uri=Uri.parse(uriString);

    public LockAppDao(Context ctx) {
        this.ctx=ctx;
        resolver = ctx.getContentResolver();

    }

    public Uri insertIntoDb(String pkgname){

        ContentValues values = new ContentValues();
        values.put("packagename",pkgname);
        Uri insert = resolver.insert(uri, values);
        return insert;
    }

    public int deleteFromDb(String pkgname){
        int delete = resolver.delete(uri, " packagename=?", new String[]{pkgname});
        return delete;

    }


    /**
     * 每次判断都得去查一次数据库，费时间
     * @param packagename
     * @return
     */
    public boolean isLocked(String packagename){
        Cursor query = resolver.query(uri, new String[]{"packagename"},
                " packagename=?", new String[]{packagename}, null);
        return query.moveToNext();
    }

    /**
     * 可以一次性把所有的packagename拿到
     * @return
     */
    public ArrayList<String> getAllPackagename(){
        ArrayList<String> pkgnames = new ArrayList<>();
        Cursor query = resolver.query(uri, new String[]{"packagename"}, null, null, null);
        while (query.moveToNext()){
            String pkgname = query.getString(0);
            pkgnames.add(pkgname);
        }
        return pkgnames;
    }
}
