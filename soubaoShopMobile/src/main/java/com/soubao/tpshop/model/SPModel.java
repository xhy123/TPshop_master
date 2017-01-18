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
 * Date: @date 2015年10月29日 下午10:01:49 
 * Description:{一句话描述该类的作用}
 * @version V1.0
 */
package com.soubao.tpshop.model;

/**
 * @author 飞龙
 *
 */
public interface SPModel {

	/**
	 * 
	* @Description: 返回用实体属性与之对于的json属性 
	* @return    设定文件 
	* @return String[] 属性i为实体属性, i+i 为json对应的属性 
	* @throws
	 */
	public String[] replaceKeyFromPropertyName() ;
}
