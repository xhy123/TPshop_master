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
 * Description: 分类  model
 * @version V1.0
 */
package com.soubao.tpshop.model;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

/**
 * @author 飞龙
 *
 */
public class SPCategory implements SPModel, Serializable{
	
	private int id ;				//分类ID
	private String name ;			//分类名称
	private int parentId ;			//父菜单ID
	private String parentIdPath ;	//一级分类ID到当前ID的路径(保留字段)
	private int level;				//菜单级别
	private String filterAttr;		//筛选属性(保留字段)
	private String image;			//图片URL

	private List<SPCategory> subCategory;	//子分类
	transient JSONArray subCategoryArray;
	 SPCategory parentCategory;		//父级分类

	//是否是空白
	private boolean isBlank;			//是否空白

	
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
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getParentIdPath() {
		return parentIdPath;
	}
	public void setParentIdPath(String parentIdPath) {
		this.parentIdPath = parentIdPath;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getFilterAttr() {
		return filterAttr;
	}
	public void setFilterAttr(String filterAttr) {
		this.filterAttr = filterAttr;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	public List<SPCategory> getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(List<SPCategory> subCategory) {
		this.subCategory = subCategory;
	}

	public SPCategory getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(SPCategory parentCategory) {
		this.parentCategory = parentCategory;
	}

	public boolean isBlank() {
		return isBlank;
	}

	public void setIsBlank(boolean isBlank) {
		this.isBlank = isBlank;
	}

	public JSONArray getSubCategoryArray() {
		return subCategoryArray;
	}

	public void setSubCategoryArray(JSONArray subCategoryArray) {
		this.subCategoryArray = subCategoryArray;
	}

	@Override
	public String[] replaceKeyFromPropertyName() {
		
		return new String[]{
				"parentId","parent_id",
				"parentIdPath","parent_id_path",
				"name" , "mobile_name",
				"filterAttr","filter_attr",
				"subCategoryArray","sub_category"
		};
	}


	
}
