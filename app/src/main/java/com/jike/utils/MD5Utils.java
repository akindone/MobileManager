package com.jike.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wancc on 2016/3/26.
 */
public class MD5Utils {
    public static String getMd5(String psw){
        String md5_psw="";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(psw.getBytes());

            for (byte b:digest) {
                int result = b&0xff;
                String s = Integer.toHexString(result);
                if (s.length()==1){
                    md5_psw+="0";
                }
                md5_psw+=s;
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5_psw;
    }
}