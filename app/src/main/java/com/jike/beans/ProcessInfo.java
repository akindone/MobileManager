package com.jike.beans;

import android.graphics.drawable.Drawable;

/**
 * Created by wancc on 2016/4/2.
 */
public class ProcessInfo {
    private String name;
    private Drawable icon;
    private String ramsize;
    private boolean isSystem;
    private String packagename;
    private boolean isChecked;

    public ProcessInfo(String name, Drawable icon, String ramsize, boolean isSystem, String packagename, boolean isChecked) {
        this.name = name;
        this.icon = icon;
        this.ramsize = ramsize;
        this.isSystem = isSystem;
        this.packagename = packagename;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getRamsize() {
        return ramsize;
    }

    public void setRamsize(String ramsize) {
        this.ramsize = ramsize;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return "ProcessInfo{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", ramsize='" + ramsize + '\'' +
                ", isSystem=" + isSystem +
                ", packagename='" + packagename + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
