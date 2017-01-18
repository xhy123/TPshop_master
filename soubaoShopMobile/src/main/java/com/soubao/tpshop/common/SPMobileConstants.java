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
 * Date: @date 2015年10月28日 下午9:10:48 
 * Description:	系统使用的常量
 * @version V1.0
 */
package com.soubao.tpshop.common;

/**
 * @author 飞龙
 *
 */
public class SPMobileConstants {

	//开发的调试的时候开启, 发布的时候改成false
	public static final boolean DevTest = true;
	//URL
	//www服务器临时屏蔽方法:

	public static final String BASE_HOST = "http://demo2.tp-shop.cn";//"http://demo2.tp-shop.cn";//"http://demo.tp-shop.cn";//
	public static final String BASE_URL = BASE_HOST + "/index.php?m=api";
	public static final String FLEXIBLE_THUMBNAIL = BASE_HOST + "/index.php?m=Api&c=Goods&a=goodsThumImages&width=%d&height=%d&goods_id=%s";
	public static final String SHIPPING_URL = BASE_HOST +"/Mobile/User/express/order_id/%s/display/1.html";

	public static final String WAP_URL = BASE_HOST +"/Mobile/Index/index.html";

	//支付宝支付通知URL
	public static final String PAY_NOTIFYURL = "/index.php/Api/Payment/alipayNotify";
	//菜单分类: 一级分类
	public static final String URL_CATEGORY = BASE_URL + "/goodsCategoryList";	
	public static final String URL_PRODUCT_LIST = BASE_URL + "/goodsList?1=1";
	public static final String URL_PRODUCT_DETAILS = BASE_URL + "/goodsInfo?1=1";

	public static final String APP_NAME = "tpshop";
	public static final String DB_NAME = "tpshop.db";
	public static final String DB2_NAME = "tpshop2.db";//内置数据库名称, 不可更改, 该名称对于asset目录下的tpshop2.db文件名
	public static final String KEY_IS_FIRST_STARTUP = "isFirstStartup";//是否第一次启动
	public static final String KEY_SEARCH_KEY = "search_key";//搜索关键词key


	/****系统所使用到的广播***/
	public static final String ACTION_SHOPCART_CHNAGE = "com.soubao.tpshop.shoprcart_change";
	public static final String ACTION_LOGIN_CHNAGE = "com.soubao.tpshop.login_change";	//登录状态改变

	/****系统常量*********/
	public static final String SP_SIGN_PRIVATGE_KEY = "tpshop2";	//签名KEY, 同服务器一致
	public static final String SP_AUTH_CODE = "TPSHOP";			//授权code,同服务器一致, 否则登录失败

	//客服电话
	public static final String MOBILE_CUSTOMER = "0755-86140485";

	//每页显示数据条数
	public static final int SizeOfPage = 15;

	public static final int MSG_CODE_ORDER_BUTTON_ACTION = 10;
	public static final int MSG_CODE_ORDER_LIST_ITEM_ACTION = 11;
	public static final int MSG_CODE_FILTER_CHANGE_ACTION = 12;

	public static final int MSG_CODE_LOAD_DATA_START = 13;	//开始加载数据
	public static final int MSG_CODE_LOAD_DATAE_CHANGE = 14;//数据发生改变
	public static final int MSG_CODE_LOAD_DATAE_COMPLETED = 15;//数据加载完成


	public static final int MSG_CODE_REGION_PROVINCE = 16;	//省份
	public static final int MSG_CODE_REGION_CITY = 17;		//城市
	public static final int MSG_CODE_REGION_DISTRICT = 18;	//区县
	public static final int MSG_CODE_REGION_TOWN = 19;		//镇/街道

	public static final int MSG_CODE_SEARCHKEY = 20;		//镇/街道
	public static final int MSG_CODE_AFTERSALE = 21;		//申请售后
	public static final int MSG_CODE_COMMENT = 22;			//去评价

	public static final int MSG_CODE_SHOW = 23;


	/******** Activity启动相关 ***************/
	public static final int Result_Code_GetValue = 100;
	public static final int Result_Code_Refresh = 101;
	public static final int Result_Code_GetAddress = 102;
	public static final int Result_Code_GetPicture = 103;//获取图片

	public static final int tagCancel 	= 666; 	//取消订单
	public static final int tagPay 		= 667; 	//支付
	public static final int tagReceive 	= 668; 	//确认收货
	public static final int tagShopping = 669; 	//查看物流
	public static final int tagComment 	= 700; 	//评论
	public static final int tagCustomer = 701; 	//联系客服
	public static final int tagReturn 	= 702; 	//退换货
	public static final int tagBuyAgain = 703; 	//再次购买


	//数据返回常量key
	public class Response{
		public static final String RESULT	= "result";
		public static final String MSG		= "msg";
		public static final String STATUS	= "status";
		public static final int RESPONSE_CODE_TOEKN_EMPTY	= -100;	//缺少token
		public static final int RESPONSE_CODE_TOEKN_EXPIRE	= -101;	//token过期, 需要重新登录重新刷新token
		public static final int RESPONSE_CODE_TOEKN_INVALID	= -102;	//token无效, 需要重新登录获取新token
	}

	/**
	 * 分类级别
	 */
	public enum CategoryLevel{

		topLevel("一级分类" , 0),
		secondLevel("二级分类" , 1),
		thirdLevel("三级分类" ,2);

		private String name;
		private int value;

		// 构造方法
		private CategoryLevel(String name, int value) {
			this.name = name;
			this.value = value;
		}
	}

	/**
	 * 支付类型
	 */
	public enum PayType{

		typeUnknow("未知支付类型" , -1), typeAlipay("支付宝支付" , 4), typeWechatPay("微信支付" , 5);
		private String name;
		private int value;

		// 构造方法
		private PayType(String name, int value) {
			this.name = name;
			this.value = value;
		}
	}

	/**
	 * 支付状态
	 */
	public enum PayStatus{

		statusSccess("支付完成" , 1), statusFailed("支付失败" , 2),statusCancel("取消支付" , 3);

		private String name;
		private int value;

		// 构造方法
		private PayStatus(String name, int value) {
			this.name = name;
			this.value = value;
		}
	}




}
