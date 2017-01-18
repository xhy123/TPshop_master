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
 * Date: @date 2015年10月23日 下午9:04:07 
 * Description: 收货地址
 * @version V1.0
 */
package com.soubao.tpshop.model;

/**
 * @author 飞龙
 *
 */
public class SPReceiveAddress {

	public int addressid;		//地址ID
	public String phone;		//电话号码
	public int provinceid;		//省份ID
	public String province;		//省份
	
	public int cityid;			//城市ID
	public String city;			//城市
	
	public int districtid;		//区ID
	public String district;		//区
	
	public String street;		//街道
	public String address;		//详细地址
	
	public int defaultAddress;	//是否默认接收地址. 1: 默认, 0:不是默认
}
