package com.jike.beans;

import android.graphics.drawable.Drawable;

/**
 * Created by wancc on 2016/3/31.
 */
public class AppInfo {
    private String name;
    private Drawable icon;
    private boolean isSDcard;
    private boolean isSystem;
    private String packagename;

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public AppInfo(String name, Drawable icon, boolean isSDcard, boolean isSystem, String packagename) {
        this.name = name;
        this.icon = icon;
        this.isSDcard = isSDcard;
        this.isSystem = isSystem;
        this.packagename = packagename;
    }

    public AppInfo() {
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

    public boolean isSDcard() {
        return isSDcard;
    }

    public void setIsSDcard(boolean isSDcard) {
        this.isSDcard = isSDcard;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", isSDcard=" + isSDcard +
                ", isSystem=" + isSystem +
                ", packagename='" + packagename + '\'' +
                '}';
    }
}
