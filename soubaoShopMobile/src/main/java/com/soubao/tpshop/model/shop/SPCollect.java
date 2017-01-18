/**
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015年11月26日 下午10:13:37
 * Description:商品收藏Model
 * @version V1.0
 */
package com.soubao.tpshop.model.shop;

import com.soubao.tpshop.model.SPModel;

/**
 * Created by admin on 2016/6/21.
 */
public class SPCollect implements SPModel {

    // collect_id - 收藏ID
   String collectID;

    // goods_id - 商品ID
   String goodsID;

    // goods_name - 商品名称
   String goodsName;

    // shop_price - 商品价格
   String shopPrice;

/****额外增加字段, 在对应的数据库表中没有对应的字段*******/
    //商品缩略图
   String imageThumlUrl;


    
    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
            "collectID","collect_id",
            "goodsID","goods_id",
            "goodsName","goods_name",
            "shopPrice","shop_price"
        };
    }

    public String getCollectID() {
        return collectID;
    }

    public void setCollectID(String collectID) {
        this.collectID = collectID;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getImageThumlUrl() {
        return imageThumlUrl;
    }

    public void setImageThumlUrl(String imageThumlUrl) {
        this.imageThumlUrl = imageThumlUrl;
    }
}
