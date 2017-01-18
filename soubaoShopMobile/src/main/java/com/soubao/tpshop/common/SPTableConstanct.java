/******************************************************************************
 * Copyright (C) 2015 ShenZhen Moze Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳市么子信息技术有限公司开发研制，未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/

package com.soubao.tpshop.common;

/** 
 * @author wangqh E-mail:kingastrive22@gmail.com
 * @version 创建时间：2015-5-18 下午1:58:52 
 * @Description 汽车说
 * @category 
 */
public class SPTableConstanct {
	
	public final static String TABLE_NAME_ADDRESS = "sp_address";
	public final static String CREATE_TABLE_ADDRESS = "CREATE TABLE IF NOT EXISTS sp_address(" +
			"id integer, " +
			"name text NOT NULL, " +
			"parent_id integer NOT NULL , " +
			"level integer NOT NULL) ";

	public final static String TABLE_NAME_CATEGORY = "tp_goods_category";
	public final static String CREATE_TABLE_CATEGORY = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME_CATEGORY+"(" +
			"id integer, " +
			"name STRING NOT NULL, " +
			"parent_id INTEGER NOT NULL , " +
			"level INTEGER NOT NULL ," +
			"image STRING ," +
			"is_hot INTEGER , " +
			"sort_order INTEGER) ";
}
 
