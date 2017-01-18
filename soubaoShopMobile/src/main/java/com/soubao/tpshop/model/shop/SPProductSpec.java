package com.soubao.tpshop.model.shop;

import com.soubao.tpshop.model.SPModel;

import java.io.Serializable;

/**
 * Created by admin on 2016/6/20.
 */
public class SPProductSpec implements SPModel ,Serializable ,Comparable<SPProductSpec>  {

   /**
    *  规格ID
    */
    String itemID;

    /**
     *  规格值
     */
    String item;

    /**
     *  规格名称
     */
    String specName;

    /**
     *  URL
     */
    String src;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
            "itemID","item_id",
            "specName","spec_name"
        };
    }

    @Override
    public int compareTo(SPProductSpec another) {

        return this.specName.compareTo(another.specName);
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
