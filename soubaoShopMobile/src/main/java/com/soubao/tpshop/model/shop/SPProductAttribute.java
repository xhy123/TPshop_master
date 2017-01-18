package com.soubao.tpshop.model.shop;

import com.soubao.tpshop.model.SPModel;

import java.io.Serializable;

/**
 * Created by admin on 2016/6/20.
 */
public class SPProductAttribute implements SPModel , Serializable {

    /**
     *  属性ID
     */
    String attrID;
/**
 *  商品ID
 */
    String goodsID;

/**
 *  属性值
 */
    String attrValue;

/**
 *  属性价格
 */
    String attrPrice;

    /**
     *  属性名称
     */
    String attrName;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "attrID","attr_id",
                "goodsID","goods_id",
                "attrValue","attr_value",
                "attrPrice","attr_price",
                "attrName","attr_name"
        };
    }


    public String getAttrID() {
        return attrID;
    }

    public void setAttrID(String attrID) {
        this.attrID = attrID;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getAttrPrice() {
        return attrPrice;
    }

    public void setAttrPrice(String attrPrice) {
        this.attrPrice = attrPrice;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }
}
