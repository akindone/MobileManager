package com.jike.dao;

import android.test.AndroidTestCase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by wancc on 2016/4/1.
 */
public class LockAppDaoTest extends AndroidTestCase {

    public void testInsertIntoDb() throws Exception {
        LockAppDao lockAppDao = new LockAppDao(getContext());
        lockAppDao.insertIntoDb("com.test.pkgname");
    }

    public void testDeleteFromDb() throws Exception {
        LockAppDao lockAppDao = new LockAppDao(getContext());
        int i = lockAppDao.deleteFromDb("com.test.pkgname");
        assertEquals(1,i);

    }

    public void testIsLocked() throws Exception {
        LockAppDao lockAppDao = new LockAppDao(getContext());
        boolean locked = lockAppDao.isLocked("com.test.pkgname");
        assertEquals(true,locked);

    }

    public void testGetAllPackagename() throws Exception {
        LockAppDao lockAppDao = new LockAppDao(getContext());
        ArrayList<String> allPackagename = lockAppDao.getAllPackagename();
        Log.e("TAG TEST",allPackagename.get(0));

    }
}