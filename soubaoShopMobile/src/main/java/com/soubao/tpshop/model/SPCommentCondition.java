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
 * Date: @date 2015年10月27日 下午9:14:42
 * Description: 商品评价 model
 * @version V1.0
 */
package com.soubao.tpshop.model;

import java.io.File;
import java.util.List;

/**
 * Created by admin on 2016/7/16.
 */
public class SPCommentCondition {

    String goodsID; //商品ID
    String orderID; //订单ID
    String comment; //评论ID
    Integer goodsRank ;
    Integer serviceRank;
    Integer expressRank;
    List<File> images;
    String specKey; //商品规格

    public String getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getGoodsRank() {
        return goodsRank;
    }

    public void setGoodsRank(Integer goodsRank) {
        this.goodsRank = goodsRank;
    }

    public Integer getServiceRank() {
        return serviceRank;
    }

    public void setServiceRank(Integer serviceRank) {
        this.serviceRank = serviceRank;
    }

    public Integer getExpressRank() {
        return expressRank;
    }

    public void setExpressRank(Integer expressRank) {
        this.expressRank = expressRank;
    }

    public List<File> getImages() {
        return images;
    }

    public void setImages(List<File> images) {
        this.images = images;
    }

    public String getSpecKey() {
        return specKey;
    }

    public void setSpecKey(String specKey) {
        this.specKey = specKey;
    }
}
