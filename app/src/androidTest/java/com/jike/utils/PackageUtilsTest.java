package com.jike.utils;

import android.test.AndroidTestCase;

/**
 * Created by wancc on 2016/4/5.
 */
public class PackageUtilsTest extends AndroidTestCase {

    public void testGetAppCacheSize() throws Exception {
        PackageUtils.getAppCacheSize(getContext(),getContext().getPackageName());
    }
}