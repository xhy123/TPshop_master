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
 * Description: 订单  model
 * @version V1.0
 */
package com.soubao.tpshop.model.order;

import com.soubao.tpshop.model.SPModel;
import com.soubao.tpshop.model.SPProduct;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2016/6/29.
 */
public class SPOrder implements SPModel , Serializable{

    //订单ID(order_id)
    String orderID;

//订单编号(order_sn)
    String orderSN;

//订单状态号(order_status)
    String orderStatus;

//发货状态(shipping_status)
    String shippingStatus;

    //运费(shipping_price)
    String shippingPrice;


//支付状态 pay_status
    String payStatus;

//consignee 收货人
    String consignee;

//省份(province)
    String province;

//发货状态(city)
    String city;

//发货状态(district)
    String district;

    //发货状态(district)
    String town;//街道/镇

//详细地址(address)
    String address;

    String mobile;

    String email;

    //物流code(shipping_code)
    String shippingCode;

    //物流名称(shipping_name)
    String shippingName;

//支付code(pay_code)
    String payCode;

//支付方式名称(pay_name)
    String payName;

//发票抬头(invoice_title)
    String invoiceTitle;

//快递单号(invoice_no)
    String invoiceNO;


//应付款金额 order_amount
    String orderAmount;

//发货时间
    String shippingTime;

    //商品总额
    String goodsPrice;

//是否货到付款 is_cod
    String isCod;

    //优惠券金额
    String couponPrice;

    //积分抵扣金额
    String integralMoney;

    //使用积分
    String integral;

    //使用余额
    String userMoney;

    //创建时间
    String addTime;

    /****** 额外增加字段 ****************/
    List<SPProduct> products;//商品列表
    private transient JSONArray productsArray;

    /****** 额外增加字段(控制订单列表按钮状态) ****************/
    //pay_btn (支付按钮)
    int payBtn;
    //cancel_btn (取消订单按钮) 0: 隐藏, 1:显示
    int cancelBtn;
    //receive_btn (确认收货按钮) 0: 隐藏, 1:显示
    int receiveBtn;
    //comment_btn (评价按钮) 0: 隐藏, 1:显示
    int commentBtn;
    //shipping_btn (查看物流) 0: 隐藏, 1:显示
    int shippingBtn;
    //return_btn (联系客服/退换货) 0: 隐藏, 1:显示
    int returnBtn;

//order_status_code (订单状态:英文)
    String orderStatusCode;
//order_status_code (订单状态:中文)
    String orderStatusDesc;
    
    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "orderID", "order_id",
                "orderSN", "order_sn",
                "orderStatus", "order_status",
                "shippingStatus", "shipping_status",
                "shippingPrice", "shipping_price",
                "payStatus", "pay_status",
                "shippingCode", "shipping_code",
                "shippingName", "shipping_name",
                "payCode", "pay_code",
                "payName", "pay_name",
                "invoiceTitle", "invoice_title",
                "orderAmount", "order_amount",
                "shippingTime", "shipping_time",
                "isCod", "is_cod",
                "invoiceNO", "invoice_no",
                "goodsPrice", "goods_price",
                "couponPrice", "coupon_price",
                "integralMoney", "integral_money",
                "userMoney", "user_money",
                "town","twon",
                "addTime","add_time",


                /**********额外增加字段*****************/
                "productsArray", "goods_list",

                /**********额外增加字段(订单列表按钮)*****************/
                "payBtn", "pay_btn",
                "cancelBtn", "cancel_btn",
                "receiveBtn", "receive_btn",
                "commentBtn", "comment_btn",
                "shippingBtn", "shipping_btn",
                "returnBtn", "return_btn",
                "orderStatusCode", "order_status_code",
                "orderStatusDesc", "order_status_desc",
        };
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderSN() {
        return orderSN;
    }

    public void setOrderSN(String orderSN) {
        this.orderSN = orderSN;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(String shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getInvoiceNO() {
        return invoiceNO;
    }

    public void setInvoiceNO(String invoiceNO) {
        this.invoiceNO = invoiceNO;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(String shippingTime) {
        this.shippingTime = shippingTime;
    }

    public String getIsCod() {
        return isCod;
    }

    public void setIsCod(String isCod) {
        this.isCod = isCod;
    }

    public List<SPProduct> getProducts() {
        return products;
    }

    public void setProducts(List<SPProduct> products) {
        this.products = products;
    }

    public JSONArray getProductsArray() {
        return productsArray;
    }

    public void setProductsArray(JSONArray productsArray) {
        this.productsArray = productsArray;
    }

    public int getPayBtn() {
        return payBtn;
    }

    public void setPayBtn(int payBtn) {
        this.payBtn = payBtn;
    }

    public int getCancelBtn() {
        return cancelBtn;
    }

    public void setCancelBtn(int cancelBtn) {
        this.cancelBtn = cancelBtn;
    }

    public int getReceiveBtn() {
        return receiveBtn;
    }

    public void setReceiveBtn(int receiveBtn) {
        this.receiveBtn = receiveBtn;
    }

    public int getCommentBtn() {
        return commentBtn;
    }

    public void setCommentBtn(int commentBtn) {
        this.commentBtn = commentBtn;
    }

    public int getShippingBtn() {
        return shippingBtn;
    }

    public void setShippingBtn(int shippingBtn) {
        this.shippingBtn = shippingBtn;
    }

    public int getReturnBtn() {
        return returnBtn;
    }

    public void setReturnBtn(int returnBtn) {
        this.returnBtn = returnBtn;
    }

    public String getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatusCode(String orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public String getOrderStatusDesc() {
        return orderStatusDesc;
    }

    public void setOrderStatusDesc(String orderStatusDesc) {
        this.orderStatusDesc = orderStatusDesc;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getIntegralMoney() {
        return integralMoney;
    }

    public void setIntegralMoney(String integralMoney) {
        this.integralMoney = integralMoney;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(String userMoney) {
        this.userMoney = userMoney;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
