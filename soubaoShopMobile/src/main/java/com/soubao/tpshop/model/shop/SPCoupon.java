package com.soubao.tpshop.model.shop;

import com.soubao.tpshop.model.SPModel;

import java.io.Serializable;

/**
 * Created by admin on 2016/6/27.
 */
public class SPCoupon implements SPModel ,Serializable {

    // id - 优惠券ID
    String couponID;

    // cid - 优惠券类型ID
    String typeID;

    //发放类型 1 按订单发放 2 注册 3 邀请 4 按用户发放
    String type;

    //uid - 用户ID
    String userID;

    //order_id - 订单ID
    String orderID;

    //use_time 使用时间
    String useTime;

    //code 优惠券兑换码
    String code;

    //send_time 发放时间
    String sendTime;

//name 名称
    String name;

//money 金额(面值金额)
    String money;

//use_end_time 过期时间
    String useEndTime;

//condition 使用条件
    String condition;



/******** 额外增加属性 **********************/

    /**
     *  优惠券类型, 1:代金券(列表选择), 2:优惠码(文本框输入)
     */
    int couponType;
/**
 *  优惠码
 */
//String couponCode;
/**
 *  是否选择使用, 为了适配页面而增加的属性, 对应数据库不存在该字段
 */
   boolean isCheck;//只有couponType为2时才设置该值
    
    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "couponID","id",
                "typeID","cid",
                "typeID","uid",
                "orderID","order_id",
                "useTime","use_time",
                "sendTime","send_time",
                "sendTime","send_time",
                "useEndTime","use_end_time"
            };
    }

    public String getCouponID() {
        return couponID;
    }

    public void setCouponID(String couponID) {
        this.couponID = couponID;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUseEndTime() {
        return useEndTime;
    }

    public void setUseEndTime(String useEndTime) {
        this.useEndTime = useEndTime;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
}
