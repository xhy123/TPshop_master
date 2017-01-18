package com.soubao.tpshop.model.shop;

/**
 * Created by admin on 2016/6/18.
 */
public class SPFilterItem  {


    private String key;
    private String name;
    private String href;
    private boolean isHighLight; //是否高亮选中 -> 额外增加字段, 用于页面逻辑判断.

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean isHighLight() {
        return isHighLight;
    }

    public void setIsHighLight(boolean isHighLight) {
        this.isHighLight = isHighLight;
    }


}
