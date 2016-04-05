package com.jike.beans;

import android.graphics.drawable.Drawable;

/**
 * Created by wancc on 2016/4/4.
 */
public class CacheInfo {
    String label;
    Drawable icon;
    String cacheSize;
    String packagename;

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public CacheInfo(String label, Drawable icon, String cacheSize, String packagename) {
        this.label = label;
        this.icon = icon;
        this.cacheSize = cacheSize;
        this.packagename = packagename;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(String cacheSize) {
        this.cacheSize = cacheSize;
    }
}
