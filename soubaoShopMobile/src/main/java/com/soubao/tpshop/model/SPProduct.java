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
 * Date: @date 2015年11月4日 下午10:10:15 
 * Description:{一句话描述该类的作用}
 * @version V1.0
 */
package com.soubao.tpshop.model;

import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.model.shop.SPProductAttribute;
import com.soubao.tpshop.model.shop.SPProductSpec;
import com.soubao.tpshop.utils.SPCommonUtils;

import org.json.JSONArray;


import java.io.Serializable;
import java.util.List;

/**
 * @author 飞龙
 *
 */
public class SPProduct implements SPModel , Serializable {


	//商品ID
	private String goodsID;

//所属分类ID
	private String categoryID;

//商品编号
	private String goodsSN;

//商品图片
	private String originalImg;

//商品名称
	private String goodsName;

//品牌ID
	private String brandID;

//库存数量

	private String stockCount;

//评论数量

	private String commentCount;

//浏览数量
	private String  browseCount;

//商品重量
	private String weight;

//市场价
	private String marketPrice;


//本店价(商品价格, 如果商品没有规格属性, 那么该价格就是商品当前售价)
	private String shopPrice;

//会员价 -> member_goods_price
	private String memberGoodsPrice;

//  会员价

//private String memberPrice;

//  商品简单描述

	private String goodsRemark;

//  商品详细描述

	private String goodsContent;

//  点击数量

	private String clickCount;

// 是否上架

	private String isOnSale;

	// 是否包邮:(0:不包邮, 1:包邮)

	private String isFreePostage;


//  是否推荐:(0:不推荐, 1:推荐)

	private String isRecommend;

//  是否新品:(0:否, 1:是)

	private String isNew;

//  是否最热:(0:否, 1:是)

	private String isHot;

//  所属分类

	private String goodsType;

//  规格类型

	private String specType;

	//0未发货，1已发货，2已换货，3已退货 , 为1的时候才显示退换货申请
	private int isSend;
	private int returnBtn;//为1的时候显示 ,isSend ,returnBtn两个字段同时位1的时候显示"申请售后"按钮

	//  商品属性
	private List<SPProductAttribute> attrArr;
	private transient JSONArray attrJsonArray;

	//  商品规格
	private List<SPProductSpec> specArr;
	private transient JSONArray specJsonArray;


/****以下属性是为了适配购物车商品而增加*******/

// 商品所在购物车ID

	private String cartID;

// 用户ID

	private String userID;

//  商品所在购物车数量

	private String goodsNum;

// 选择的规格KEY (规格ID , 多个规格按照"_"分隔)

	private String specKey;

//选择的规格 , 展示属性用

	private String specKeyName;

//商品对应的 sku 或者说 条码

	private String barCode;

//  本店售价

	private String goodsPrice;

//小计金额

	private String goodsFee;

//该商品是否在购物车中选择, 如果选择该商品将会结算 , 1:选中, 0:未选中
	private String selected;

//库存数量

	private String storeCount;


/****额外增加字段, 在对应的数据库表中没有对应的字段*******/

	//商品缩略图URL
	private String imageThumlUrl;


/****额外增加字段(订单列表而增加), 在对应的数据库表中没有对应的字段*******/
//order_id 订单ID
	private String orderID;
	private String orderSN;


//is_comment 是否已经评论(目前只支持一次评论)
	private String isComment;

	private String orderStatusCode;//订单状态code

	private List<String> gallerys;	//图片详情URL
	public List<String> getGallerys() {
		return gallerys;
	}


	@Override
	public String[] replaceKeyFromPropertyName() {

		return new String[]{
				"goodsID","goods_id",
				"goodsName","goods_name",
				"categoryID","cat_id",
				"goodsSN","goods_sn",

				"browseCount","click_count",
				"brandID","brand_id",
				"stockCount","kc_count",
				"commentCount","comment_count",
				"marketPrice","market_price",
				"shopPrice","shop_price",
				"memberGoodsPrice","member_goods_price",
				//"memberPrice","member_price",
				"originalImg","original_img",
				"goodsRemark","goods_remark",
				"goodsContent","goods_content",
				"isOnSale","is_on_sale",
				"isFreePostage","is_baoyou",
				"isRecommend","is_tuijian",
				"isNew","isnew",
				"isHot","is_hot",
				"goodsType","goods_type",
				"specType","spec_type",
				"attrJsonArray","goods_attr_list",
				"specJsonArray","goods_spec_list",
				"isSend","is_send",

				/****以下属性是为了适配购物车商品类别而增加*******/
				"cartID","id",
				"userID","user_id",
				"goodsNum","goods_num",
				"specKey","spec_key",
				"specKeyName","spec_key_name",
				"barCode","bar_code",
				"goodsPrice","goods_price",
				"goodsFee","goods_fee",
				"storeCount","store_count",
				"isComment","is_comment"
		};
	}


	public void setGallerys(List<String> gallerys) {
		this.gallerys = gallerys;
	}

	public int getIsSend() {
		return isSend;
	}

	public void setIsSend(int isSend) {
		this.isSend = isSend;
	}

	public int getReturnBtn() {
		return returnBtn;
	}

	public void setReturnBtn(int returnBtn) {
		this.returnBtn = returnBtn;
	}

	public String getGoodsID() {
		return goodsID;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public String getGoodsSN() {
		return goodsSN;
	}

	public String getOriginalImg() {
		return originalImg;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public String getBrandID() {
		return brandID;
	}

	public String getStockCount() {
		return stockCount;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public String getBrowseCount() {
		return browseCount;
	}

	public String getWeight() {
		return weight;
	}

	public String getMarketPrice() {
		return marketPrice;
	}

	public String getShopPrice() {
		return shopPrice;
	}

	public String getMemberGoodsPrice() {
		return memberGoodsPrice;
	}

	public String getGoodsRemark() {
		return goodsRemark;
	}

	public String getGoodsContent() {
		return goodsContent;
	}

	public String getClickCount() {
		return clickCount;
	}

	public String getIsOnSale() {
		return isOnSale;
	}

	public String getIsFreePostage() {
		return isFreePostage;
	}

	public String getIsRecommend() {
		return isRecommend;
	}

	public String getIsNew() {
		return isNew;
	}

	public String getIsHot() {
		return isHot;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public String getSpecType() {
		return specType;
	}

	public List<SPProductAttribute> getAttrArr() {
		return attrArr;
	}

	public void setAttrArr(List<SPProductAttribute> attrArr) {
		this.attrArr = attrArr;
	}

	public JSONArray getAttrJsonArray() {
		return attrJsonArray;
	}

	public void setAttrJsonArray(JSONArray attrJsonArray) {
		this.attrJsonArray = attrJsonArray;
	}

	public List<SPProductSpec> getSpecArr() {
		return specArr;
	}

	public void setSpecArr(List<SPProductSpec> specArr) {
		this.specArr = specArr;
	}

	public JSONArray getSpecJsonArray() {
		return specJsonArray;
	}

	public void setSpecJsonArray(JSONArray specJsonArray) {
		this.specJsonArray = specJsonArray;
	}

	public String getCartID() {
		return cartID;
	}

	public String getUserID() {
		return userID;
	}

	public String getGoodsNum() {
		return goodsNum;
	}

	public String getSpecKey() {
		return specKey;
	}

	public String getSpecKeyName() {
		return specKeyName;
	}

	public String getBarCode() {
		return barCode;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public String getGoodsFee() {
		return goodsFee;
	}

	public String getSelected() {
		return selected;
	}

	public String getStoreCount() {
		return storeCount;
	}

	public String getImageThumlUrl() {
		return  SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, this.goodsID);

	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getIsComment() {
		return isComment;
	}

	public void setIsComment(String isComment) {
		this.isComment = isComment;
	}

	public String getOrderStatusCode() {
		return orderStatusCode;
	}

	public void setOrderStatusCode(String orderStatusCode) {
		this.orderStatusCode = orderStatusCode;
	}

	public String getOrderSN() {
		return orderSN;
	}

	public void setOrderSN(String orderSN) {
		this.orderSN = orderSN;
	}
}
