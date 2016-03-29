package com.jike.beans;

/**
 * Created by wancc on 2016/3/28.
 */
public class Contact {
    private String name;
    private String tele;

    public Contact() {
    }

    public Contact(String name, String tele) {
        this.name = name;
        this.tele = tele;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTele() {
        return tele;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", tele='" + tele + '\'' +
                '}';
    }
}
