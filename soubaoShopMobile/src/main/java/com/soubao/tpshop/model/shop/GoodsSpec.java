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
 * Date: @date 2015年11月26日 下午10:13:37 
 * Description:{一句话描述该类的作用}
 * @version V1.0
 */
package com.soubao.tpshop.model.shop;

import com.soubao.tpshop.model.SPModel;

/**
 * @author 飞龙
 *
 */
public class GoodsSpec implements SPModel {

	private String specName;	//规格大类
	private String itemID;		//规格ID
	private String item;		//规格名称
	private String src;			//图片URL, 如果该项为空, 则为文本
	
	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	@Override
	public String[] replaceKeyFromPropertyName() {
		return new String[]{
				"specName","spec_name",
				"itemID","item_id"
				};
	}

}
