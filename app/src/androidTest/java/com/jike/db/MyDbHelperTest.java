package com.jike.db;

import android.test.AndroidTestCase;

/**
 * Created by wancc on 2016/4/1.
 */
public class MyDbHelperTest extends AndroidTestCase{


    public void testOnCreate() throws Exception {
        MyDbHelper helper = new MyDbHelper(getContext(),"lockapp",null,1);
    }

    public void testOnUpgrade() throws Exception {

    }
}