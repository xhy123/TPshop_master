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
 * Date: @date 2015年11月2日 下午8:30:29 
 * Description:{一句话描述该类的作用}
 * @version V1.0
 */
package com.soubao.tpshop.utils;

import android.util.Log;

/**
 * @author 飞龙
 *
 */
public class SMobileLog {

	//发布的时候改成false
	public static boolean DEBUG = true;
	
	public static void i(String TAG , String  msg) {
		if(DEBUG)Log.i(TAG  , msg);
	}

	public static void e(String TAG , String  msg) {
		Log.e(TAG  , msg);
	}
}
