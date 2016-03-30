package com.jike.dao;

import junit.framework.TestCase;

/**
 * Created by wancc on 2016/3/30.
 */
public class NumberLocationDaoTest extends TestCase {

    public void testIsValid() throws Exception {
        assertEquals(false,new NumberLocationDao().isValid("111"));
    }
}