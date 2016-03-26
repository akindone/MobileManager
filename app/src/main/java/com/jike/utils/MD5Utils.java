package com.jike.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wancc on 2016/3/26.
 */
public class MD5Utils {
    public static String getMd5(String psw){
        //字符串放在常量池，每次变化都会新new一个 占内存。
        StringBuffer md5_psw=new StringBuffer();//StringBuffer 相当于一个可变长度的字符串

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(psw.getBytes());

            for (byte b:digest) {
                int result = b&0xff;
                String hexString = Integer.toHexString(result);
                if (hexString.length()==1){
                    md5_psw.append("0");//一样的md5_psw.append(0);
                    //md5_psw+="0";
                }
                md5_psw.append(hexString);
                //md5_psw+=s;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5_psw.toString();
    }
}
