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
 * Date: @date 2015年10月20日 下午7:52:58 
 * Description:Activity 订单相关基类
 * @version V1.0
 */
package com.soubao.tpshop.activity.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.person.SPPersonRequest;
import com.soubao.tpshop.model.order.SPOrder;
import com.soubao.tpshop.utils.SPServerUtils;

/**
 * @author 飞龙
 *
 */
public abstract class SPOrderBaseActivity extends SPBaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}
	/**
	 * 去支付
	 * @param order
	 */
	public void gotoPay(SPOrder order){
		Intent payIntent = new Intent(this , SPPayListActivity_.class);
		payIntent.putExtra("order", order);
		startActivity(payIntent);
	}

	/**
	 * 取消订单
	 * @param order
	 */
	public void cancelOrder(String orderId , SPSuccessListener successListener , SPFailuredListener failuredListener){
		SPPersonRequest.cancelOrderWithOrderID(orderId, successListener, failuredListener);
	}

	/**
	 * 确认收货
	 * @param
	 */
	public void confirmOrderWithOrderID(String orderId , SPSuccessListener successListener , SPFailuredListener failuredListener){
		SPPersonRequest.confirmOrderWithOrderID(orderId, successListener, failuredListener);
	}



	/**
	 * 联系客服
	 */
	public void connectCustomer(){
		String qq = SPServerUtils.getCustomerQQ();
		String customerUrl = "mqqwpa://im/chat?chat_type=wpa&uin="+qq+"&version=1";
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(customerUrl)));
	}

	/**
	 * 查看物流
	 */
	public void lookShopping(SPOrder order){
		String shippingUrl = String.format(SPMobileConstants.SHIPPING_URL, order.getOrderID());
		startWebViewActivity(shippingUrl ,  "查看物流");
	}
}
