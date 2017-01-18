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
 * Description:	商品分类数据操作类
 * @version V1.0
 */
package com.soubao.tpshop.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soubao.tpshop.common.SPTableConstanct;
import com.soubao.tpshop.model.SPCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/27.
 */
public class SPCategoryDao {

    private final String TAG = "SPCategoryDao";


    private SPMobileDBHelper dbHelper = null;
    private static SPCategoryDao instance = null;
    private SPCategoryDao(Context context) {
        this.dbHelper = new SPMobileDBHelper(context);
    }

    public static synchronized SPCategoryDao getInstance(Context context){
        if(null==instance){
            instance = new SPCategoryDao(context);
        }
        return instance;
    }

    public void insertCategoryList(List<SPCategory> categorys){

        if(categorys == null) return;
        SQLiteDatabase dbwrite = null;
        try {
            dbwrite = dbHelper.getWritableDatabase();
            //1. 先删除旧数据
            String deleteSQL = "delete from "+ SPTableConstanct.TABLE_NAME_CATEGORY;
            dbwrite.execSQL(deleteSQL);

            for(int i=0; i<categorys.size(); i++){
                SPCategory category = categorys.get(i);
                String name = category.getName();
                int id = category.getId();
                int parentId = category.getParentId();
                int level = category.getLevel();
                String image = category.getImage();

                ContentValues cv = new ContentValues();
                cv.put("id", id);
                cv.put("name", name);
                cv.put("parent_id", parentId);
                cv.put("level", level);
                cv.put("image", image);

                dbwrite.insert( SPTableConstanct.TABLE_NAME_CATEGORY, null, cv );
            }
        } catch (Exception e) {
            Log.w(TAG, "insertBatch occur error : " + e.getMessage());
        }finally{
            dbwrite.close();
        }
    }

    /**
     *  获取下级地址信息
     *  @return return value description
     */
    public List<SPCategory> queryCategoryByParentID(int parentID){

        List<SPCategory> list = new ArrayList<SPCategory>();
        SQLiteDatabase dbwrite = dbHelper.getWritableDatabase();

        String [] columns = new String[]{"id" , "name" , "parent_id" , "level" , "image" , "is_hot"};
        String selection = " parent_id = ? ";
        String [] selectionArgs = new String[]{String.valueOf(parentID)};
        try {
            Cursor cursor = dbwrite.query(SPTableConstanct.TABLE_NAME_CATEGORY, columns, selection, selectionArgs, null, null, null);
            try {
                if(cursor != null){
                    while(cursor.moveToNext()) {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        int level = cursor.getInt(cursor.getColumnIndex("level"));
                        String image = cursor.getString(cursor.getColumnIndex("image"));
                        int parentId = cursor.getInt(cursor.getColumnIndex("parent_id"));

                        SPCategory category = new SPCategory();
                        category.setId(id);
                        category.setName(name);
                        category.setLevel(level);
                        category.setParentId(parentId);
                        category.setImage(image);

                        list.add(category);
                    }
                }
            }catch (Exception e) {
                Log.w(TAG , " queryCategoryByParentID occur error : "+e.getMessage());
            }finally{
                cursor.close();
            }
        } catch (Exception e) {
            Log.w(TAG , " queryCategoryByParentID occur error : "+e.getMessage());
        }finally{
            dbwrite.close();
        }
        return list;
    }

}
