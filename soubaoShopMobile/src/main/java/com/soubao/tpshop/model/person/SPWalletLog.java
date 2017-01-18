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
 * Description: 积分金额交易log Model
 * @version V1.0
 */
package com.soubao.tpshop.model.person;

import com.soubao.tpshop.model.SPModel;

/**
 * Created by admin on 2016/7/5.
 */
public class SPWalletLog implements SPModel{

    //交易余额
    String userMoney;

    //交易积分
    String payPoints;

    //交易描述
    String desc;

    //交易时间
    String changeTime;

    //订单SN
    String orderSN;
    
    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "userMoney","user_money",
                "payPoints","pay_points",
                "changeTime","change_time",
                "orderSN","order_sn",
        };
    }

    public String getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(String userMoney) {
        this.userMoney = userMoney;
    }

    public String getPayPoints() {
        return payPoints;
    }

    public void setPayPoints(String payPoints) {
        this.payPoints = payPoints;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }

    public String getOrderSN() {
        return orderSN;
    }

    public void setOrderSN(String orderSN) {
        this.orderSN = orderSN;
    }
}
