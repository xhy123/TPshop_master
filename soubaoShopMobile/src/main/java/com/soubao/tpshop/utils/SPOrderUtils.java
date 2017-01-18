/**
 * tpshop
 * ============================================================================
 * * 版权所有 2015-2027 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ----------------------------------------------------------------------------
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * $Author: 飞龙  15/12/1 $
 * $description: 订单公共操作类
 */
package com.soubao.tpshop.utils;

import com.soubao.tpshop.model.order.SPOrder;

/**
 * Created by admin on 2016/7/1.
 */
public class SPOrderUtils {

    //订单类型
    public static final String typeWaitPay = "WAITPAY";    // 待支付
    public static final String typeWaitSend = "WAITSEND";     // 待发货
    public static final String typeWaitReceive = "WAITRECEIVE";  // 待收货
    public static final String typeWaitComment = "WAITCCOMMENT"; // 待评价
    public static final String typeCommented = "COMMENTED";    // 已评价
    public static final String typeReturned = "RETURNED";     // 已退货
    public static final String typeAll = "";             // 全部订单

    public final String typeCanceled = "CANCEL";       // 已取消
    public final String typeFinished ="FINISH";       // 已完成

    public static OrderStatus getOrderStatusByValue(int value){

        switch (value){
            case 1:
                return OrderStatus.waitPay;
            case 2:
                return OrderStatus.waitSend;
            case 3:
                return OrderStatus.waitReceive;
            case 4:
                return OrderStatus.waitComment;
            case 5:
                return OrderStatus.commented;
            case 6:
                return OrderStatus.returned;
            case 7:
                return OrderStatus.all;
        }
        return OrderStatus.all;
    }

    /**
     * 订单状态
     */
    static public enum  OrderStatus{
        waitPay(1 , "待付款"),
        waitSend(2,"待发货"),
        waitReceive(3,"待收货"),
        waitComment(4,"待评价"),
        commented(5,"已评价"),
        returned(6 ,"已退货"),
        all(7 , "全部订单");

        private String name;
        private int value;

        private OrderStatus(int value , String name){
            this.name = name;
            this.value = value;
        }

        public int value(){
            return value;
        }
    }

    /**
     *  获取订单状态的 中文描述名称
     *
     *  @param order order description
     *
     *  @return return value description
     */
    public static String getOrderStatusText(SPOrder order){

        if (order.getPayStatus().equals("0") && order.getOrderStatus().equals("0")){
            return "待支付";
        }
        if(order.getPayStatus().equals("1") || order.getPayCode().equals("cod") &&
                order.getOrderStatus().equals("0") && order.getShippingStatus().equals("0")){
            //待发货: (已支付 || 货到付款) && 未发货 && 已下单
            return "待发货";
        }
        if(order.getShippingStatus().equals("1") && order.getOrderStatus().equals("0")){
            //待收货: 已发货 && 已下单
            return "待收货";
        }
        if(order.getOrderStatus().equals("1")){
            //待评价: 待评价
            return "待评价";
        }
        if(order.getOrderStatus().equals("4")){
            //已评价
            return "已评价";
        }
        return "其它";
    }

    /**
     *  获取订单状态的
     *  订单状态详情可查看TPSHOP开发手册 : (Android/IOS)开发者手册 -> 常见文件 -> TPSHOP的订单状态
     *
     *  @param order order description
     *
     *  @return 订单枚举
     *
     */
    public static OrderStatus getOrderStatusWithOrder(SPOrder order){

        if (order.getPayStatus().equals("0") && order.getOrderStatus().equals("0")){
            //待支付:  未支付 && 已下单
            return OrderStatus.waitPay;
        }

        if(order.getPayStatus().equals("1") || order.getPayCode().equals("cod") && order.getOrderStatus().equals("0") && order.getShippingStatus().equals("0")){
            //待发货: (已支付 || 货到付款) && 未发货 && 已下单
            return OrderStatus.waitSend;
        }

        if(order.getShippingStatus().equals("1") && order.getOrderStatus().equals("0")){
            //待收货: 已发货 && 已下单
            return OrderStatus.waitReceive;
        }

        if(order.getOrderStatus().equals("1")){
            //待评价: 待评价
            return OrderStatus.waitComment;
        }

        if(order.getOrderStatus().equals("4")){
            //已评价
            return OrderStatus.commented;
        }

        return OrderStatus.all;
    }


    /**
     *  根据订单状态获取标题
     *
     *  @return 标题
     */
    public static String getOrderTitlteWithOrderStatus(OrderStatus orderStatus){

        switch (orderStatus) {
            case waitPay:
                return "待付款";
            case waitSend:
                return "待发货";
            case waitReceive:
                return "待收货";
            case waitComment:
                return "待评价";
            case commented:
                return "已评价";
            case returned:
                return "已退货";
            case all:
                return "全部订单";
        }
        return "全部订单";
    }


/**
 *  根据订单状态返回订单类型
 *
 *  @return 标题
 */
    public static String getOrderTypeWithOrderStatus(OrderStatus orderStatus){

        switch (orderStatus) {
            case waitPay:
                return typeWaitPay;
            case waitSend:
                return typeWaitSend;
            case waitReceive:
                return typeWaitReceive;
            case waitComment:
                return typeWaitComment;
            case commented:
                return typeCommented;
            case returned:
                return typeReturned;
            case all:
                return typeAll;
        }
        return typeAll;
    }

    /**
     *  根据订单状态返回订单类型
     *
     *  @return 标题
     */
    public static String getExchangeStatusNameWithStatus(String status){
        switch (Integer.valueOf(status)) {
            case 0:
                return "申请中";
            case 1:
                return "处理中";
            case 2:
                return "已完成";
        }
        return "未知";
    }

    /**
     *  根据订单状态返回订单类型
     *
     *  @return 标题
     */
    public static String getExchangeTypeNameWithType(String type){
        switch (Integer.valueOf(type)) {
            case 0:
                return "退货";
            case 1:
                return "换货";
        }
        return "未知";
    }



}
