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
 * Date: @date 2015年11月12日 下午8:02:24 
 * Description:{一句话描述该类的作用}
 * @version V1.0
 */
package com.soubao.tpshop.model;

/**
 * @author 飞龙
 *
 */
public class SPComment implements SPModel {

	private String commentID;	//商品评论
	private String goodsID;		//商品ID
	private String username;
	private String email;
	private int rank;			//星级
	private int parentID;		//父评论ID
	private int userID;			//用户ID
	private String addTime;		//评论时间
	
	@Override
	public String[] replaceKeyFromPropertyName() {
		return new String[]{
				"commentID" , "comment_id",
				"goodsID" , "goods_id",
				"addTime" , "add_time"
		};
	}

}
