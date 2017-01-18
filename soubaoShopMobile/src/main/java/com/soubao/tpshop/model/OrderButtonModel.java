package com.soubao.tpshop.model;

/**
 * Created by admin on 2016/7/2.
 */
public class OrderButtonModel {

    private String text;
    private int tag;    //按钮action tag
    private boolean isLight;

    public OrderButtonModel(String text , int tag , boolean isLight){
        this.text = text;
        this.tag = tag;
        this.isLight = isLight;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public boolean isLight() {
        return isLight;
    }

    public void setIsLight(boolean isLight) {
        this.isLight = isLight;
    }
}
