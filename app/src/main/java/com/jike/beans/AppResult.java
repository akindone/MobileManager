package com.jike.beans;

/**
 * Created by wancc on 2016/4/6.
 */
public class AppResult {
    private String label;
    private boolean isVirus;

    public AppResult(String label, boolean isVirus) {
        this.label = label;
        this.isVirus = isVirus;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isVirus() {
        return isVirus;
    }

    public void setIsVirus(boolean isVirus) {
        this.isVirus = isVirus;
    }
}
