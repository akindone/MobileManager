package com.jike.dao;

import android.test.AndroidTestCase;
import android.util.Log;

import com.jike.beans.BlackListItem;

import java.util.ArrayList;

/**
 * Created by wancc on 2016/4/6.
 */
public class BlackListDaoTest extends AndroidTestCase {

    public void testInsert() throws Exception {
        BlackListDao dao = new BlackListDao(getContext());
        for (int i=0;i<100;i++){
            dao.insert("num"+i,3);
        }

    }

    public void testQueryPart() throws Exception {
        BlackListDao dao = new BlackListDao(getContext());
        ArrayList<BlackListItem> blackListItems = dao.queryPart(5, 0);
        for (BlackListItem item :
                blackListItems) {
            Log.e("TEST", item.toString());
        }

    }

    public void testQueryNum() throws Exception {
        BlackListDao dao = new BlackListDao(getContext());
        int i = dao.queryNum();
        Log.e("TEST",i+"");

    }
}