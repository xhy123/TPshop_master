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
 * Description: 退换货  model
 * @version V1.0
 */
package com.soubao.tpshop.model.order;

import com.soubao.tpshop.model.SPModel;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by admin on 2016/7/4.
 */
public class SPExchange implements SPModel{

    String  exchangeId;
    String  orderId;
    String  orderSN;
    String  goodsId;
    String  goodsName;
    /**
     *  0:退货 ,  1:换货
     */
    String  type;

    /**
     *  退换货原因
     */
    String  reason;

    /**
     *  申请时间
     */
    String  addtime;

    /**
     *  0:申请中, 1:处理中, 2:已完成
     */
    String  status;

    /**
     *  客服备注
     */
    String  remark;

    //问题图片
    List<String> images;
    JSONArray imageArray;

    
    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "orderId","order_id",
                "orderSN","order_sn",
                "goodsId","goods_id",
                "exchangeId","id",
                "goodsName","goods_name",
                "imageArray","imgs"
        };
    }


    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderSN() {
        return orderSN;
    }

    public void setOrderSN(String orderSN) {
        this.orderSN = orderSN;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public JSONArray getImageArray() {
        return imageArray;
    }

    public void setImageArray(JSONArray imageArray) {
        this.imageArray = imageArray;
    }
}
