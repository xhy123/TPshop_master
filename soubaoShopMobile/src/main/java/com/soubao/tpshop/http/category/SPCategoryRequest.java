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
 * Date: @date 2015年10月28日 下午9:31:20 
 * Description:分类相关数据接口
 * @version V1.0
 */
package com.soubao.tpshop.http.category;

import android.util.Log;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.common.SPMobileConstants.Response;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;

import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.model.SPCategory;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPJsonUtil;

import cz.msebera.android.httpclient.Header;

/**
 * @author 飞龙
 *
 */
public class SPCategoryRequest {
	
	private static String TAG = "SPCategoryRequest";
	
	/**
	 * 
	* @Description: 获取分类
	* @param parentID 如果parent < 1 则返回时的左边分类, 如果parent > 0 获取的是右边的分类
	* @param failuredListener
	* @throws JSONException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public static void getCategory (int parentID , final SPSuccessListener successListener,  final SPFailuredListener failuredListener){

		assert(successListener!=null);
		assert(failuredListener!=null);

		String url =  SPMobileHttptRequest.getRequestUrl("Goods", "goodsCategoryList");

		try{
			RequestParams params = new RequestParams();
			if(parentID >= 0){
				params.put("parent_id" , parentID);
			}

			SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {
						JSONArray data = (JSONArray) response.getJSONArray(Response.RESULT);
						List<SPCategory> categorys = SPJsonUtil.fromJsonArrayToList(data, SPCategory.class);
						successListener.onRespone("success", categorys);
						printCategory(categorys);
					} catch (Exception e) {
						failuredListener.onRespone(e.getMessage(), -1);
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					failuredListener.onRespone(throwable.getMessage(), statusCode);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
					failuredListener.onRespone(throwable.getMessage(), statusCode);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					failuredListener.onRespone(throwable.getMessage(), statusCode);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @Description: 根据一级分类获取对应的二三级分类
	 * @param parentID 如果parent < 1 则返回时的左边分类, 如果parent > 0 获取的是右边的分类
	 * @param failuredListener
	 * @throws JSONException    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	public static void goodsSecAndThirdCategoryList (int parentID , final SPSuccessListener successListener,  final SPFailuredListener failuredListener){

		assert(successListener!=null);
		assert(failuredListener!=null);
		String url =  SPMobileHttptRequest.getRequestUrl("Goods", "goodsSecAndThirdCategoryList");
		try{
			RequestParams params = new RequestParams();
			if(parentID >= 0){
				params.put("parent_id" , parentID);
			}

			SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {
						JSONArray data = (JSONArray) response.getJSONArray(Response.RESULT);
						List<SPCategory> categorys = SPJsonUtil.fromJsonArrayToList(data, SPCategory.class);
						for (SPCategory category : categorys){
							JSONArray array = category.getSubCategoryArray();
							if (array!=null ) {
								List<SPCategory> subCategorys = null;
								try {
									subCategorys = SPJsonUtil.fromJsonArrayToList(category.getSubCategoryArray(), SPCategory.class);
								}catch(Exception e){
								}
								category.setSubCategory(subCategorys);
							}
						}
						successListener.onRespone("success", categorys);
						printCategory(categorys);
					} catch (Exception e) {
						failuredListener.onRespone(e.getMessage(), -1);
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					failuredListener.onRespone(throwable.getMessage(), statusCode);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
					failuredListener.onRespone(throwable.getMessage(), statusCode);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					failuredListener.onRespone(throwable.getMessage(), statusCode);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @Description: 获取所有分类
	 * @param successListener
	 * @param failuredListener
	 * @throws JSONException    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	public static void getAllCategory(final SPSuccessListener successListener,  final SPFailuredListener failuredListener){

		assert(successListener!=null);
		assert(failuredListener!=null);

		String url =  SPMobileHttptRequest.getRequestUrl("Goods", "goodsAllCategoryList");
		try{

			SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {
						JSONArray data = (JSONArray) response.getJSONArray(Response.RESULT);
						List<SPCategory> categorys = SPJsonUtil.fromJsonArrayToList(data, SPCategory.class);
						successListener.onRespone("success", categorys);
						printCategory(categorys);
					} catch (Exception e) {
						failuredListener.onRespone(e.getMessage(), -1);
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					failuredListener.onRespone(throwable.getMessage(), statusCode);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
					failuredListener.onRespone(throwable.getMessage(), statusCode);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					failuredListener.onRespone(throwable.getMessage(), statusCode);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void printCategory(List<SPCategory> categorys){
		if(categorys!= null){
			for (int i = 0; i < categorys.size(); i++) {
			 SPCategory category = categorys.get(i);
			}
		}
	}

}
