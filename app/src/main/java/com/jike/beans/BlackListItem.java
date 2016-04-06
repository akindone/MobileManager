package com.jike.beans;

/**
 * Created by wancc on 2016/4/5.
 */
public class BlackListItem {
    private String num;
    private int mode;

    public BlackListItem(String num, int mode) {
        this.num = num;
        this.mode = mode;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "BlackListItem{" +
                "num='" + num + '\'' +
                ", mode=" + mode +
                '}';
    }
}
