package com.jike.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    public static String getApkMd5(String sourceDir) {
        StringBuffer md5_psw=new StringBuffer();
        File file = new File(sourceDir);
        FileInputStream fis = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            fis = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len=fis.read(bytes))!=-1){
                md5.update(bytes,0,len);
            }
            byte[] digest = md5.digest();

            for (byte b:digest) {
                int result = b&0xff;
                String hexString = Integer.toHexString(result);
                if (hexString.length()==1){
                    md5_psw.append("0");
                }
                md5_psw.append(hexString);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        return md5_psw.toString();
    }
}
