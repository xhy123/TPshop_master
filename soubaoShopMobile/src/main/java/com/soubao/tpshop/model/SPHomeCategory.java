/**
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 无风  peach885090@163.com
 * Date: @date 2016年7月11日 下午14:14:42
 * Description: 分类  model
 * @version V1.0
 */
package com.soubao.tpshop.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author 无风
 *
 */
public class SPHomeCategory implements SPModel, Serializable{
	
	private int id ;				//分类ID
	private String name ;			//分类名称
	private List<SPProduct> goodsList ;			//该分类下的商品
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<SPProduct> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<SPProduct> parentId) {
		this.goodsList = parentId;
	}
	
	@Override
	public String[] replaceKeyFromPropertyName() {
		return new String[]{
				"goodsList" , "goods_list",
		};
	}


}
