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
 * Description:	数据库操作类
 * @version V1.0
 */
package com.soubao.tpshop.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.soubao.tpshop.common.SPTableConstanct;

public class SPMobileDBHelper extends SQLiteOpenHelper {

	private final String TAG = "SPMobileDBHelper";
	private static final String DBNAME = "tpshop.db" ;   //数据库名称
	private static final int DBVVERSION = 1;       		// 数据库版本

	
	/**
	 * MyDBHelper构造方法
	 * @param context
	 */
	public SPMobileDBHelper(Context context) {
		super(context,DBNAME, null, DBVVERSION);
	}
	
	/**
	 *该方法在 数据库第一次创建时是被调用，方法内应完成数据库表的完成
	 * 其中SQLiteDatabase类中包含对数据库的操作方法
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(SPTableConstanct.CREATE_TABLE_CATEGORY);  	//执行有更改的sql语句
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
}
