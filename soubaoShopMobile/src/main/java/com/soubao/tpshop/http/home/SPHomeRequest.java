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
 * Description: 首页相关数据接口
 * @version V1.0
 */
package com.soubao.tpshop.http.home;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.common.SPMobileConstants.Response;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.model.SPHomeBanners;
import com.soubao.tpshop.model.SPHomeCategory;
import com.soubao.tpshop.model.SPPlugin;
import com.soubao.tpshop.model.SPProduct;
import com.soubao.tpshop.model.SPServiceConfig;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.soubao.tpshop.utils.SPMyFileTool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * @author 飞龙
 *
 */
public class SPHomeRequest {

	private static String TAG = "SPHomeRequest";

	/**
	 *  查询系统配置信息
	 *  使用万能SQL: index.php?m=Api&c=Index&a=getConfig
	 *  @param successListener success description
	 *  @param failuredListener failure description
	 */
	public static void getServiceConfig(final SPSuccessListener successListener,  final SPFailuredListener failuredListener){
		assert(successListener!=null);
		assert(failuredListener!=null);

		String url =  SPMobileHttptRequest.getRequestUrl("Index", "getConfig");

		try{
			SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {

						String msg = (String) response.getString(Response.MSG);
						int status = response.getInt(Response.STATUS);
						if (status > 0){
							JSONArray resultArray = (JSONArray) response.getJSONArray(Response.RESULT);
							List<SPServiceConfig> serviceConfigs = SPJsonUtil.fromJsonArrayToList(resultArray, SPServiceConfig.class);
							successListener.onRespone("success", serviceConfigs);
						}else {
							failuredListener.onRespone(msg , -1);
						}
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
	 *  查询插件配置信息
	 *  使用万能SQL: index.php?m=Api&c=Index&a=getPluginConfig
	 *  @param successListener success description
	 *  @param failuredListener failure description
	 */
	public static void getServicePlugin(final SPSuccessListener successListener,  final SPFailuredListener failuredListener){
		assert(successListener!=null);
		assert(failuredListener!=null);

		String url =  SPMobileHttptRequest.getRequestUrl("Index", "getPluginConfig");

		try{
			SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {

						String msg = (String) response.getString(Response.MSG);

						int status = response.getInt(Response.STATUS);
						if (status > 0){
							JSONObject resultJson = (JSONObject)response.getJSONObject(Response.RESULT);
							List<SPPlugin> servicePlugins = SPJsonUtil.fromJsonArrayToList(resultJson.getJSONArray("payment"), SPPlugin.class);
							Map<String , SPPlugin> pluginMap = new HashMap<String, SPPlugin>();
							if (servicePlugins!=null) {
								for (SPPlugin plugin : servicePlugins) {
									//插件安装后才可使用
									if (plugin.getStatus().equals("1")){
										String key = plugin.getCode();
										pluginMap.put(key , plugin);
									}
								}
							}

							successListener.onRespone("success", pluginMap);
						}else {
							failuredListener.onRespone(msg , -1);
						}
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
	public static void getHomeData(final SPSuccessListener successListener,  final SPFailuredListener failuredListener) {
		assert (successListener != null);
		assert (failuredListener != null);
		String url =  SPMobileHttptRequest.getRequestUrl("Index", "home");
		try{
			SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {

						String msg = (String) response.getString(Response.MSG);
						JSONObject resultJson = (JSONObject) response.getJSONObject(Response.RESULT);
						JSONObject dataJson = new JSONObject();

						if (resultJson != null){
							//商品列表
							if (!resultJson.isNull("goods")) {
								JSONArray goods = resultJson.getJSONArray("goods");

								if (goods != null) {
									List<SPHomeCategory> homeCategories = SPJsonUtil.fromJsonArrayToList(goods, SPHomeCategory.class);
									for(int i = 0;i<goods.length();i++){
										JSONObject entityObj = goods.getJSONObject(i);
										JSONArray products = entityObj.getJSONArray("goods_list");
										List<SPProduct> pros = SPJsonUtil.fromJsonArrayToList(products, SPProduct.class);
										homeCategories.get(i).setGoodsList(pros);
									}
									dataJson.put("homeCategories", homeCategories);
								}
							}
							//ad
							if (!resultJson.isNull("ad")) {
								JSONArray ads = resultJson.getJSONArray("ad");
								if (ads != null) {
									List<SPHomeBanners> banners = SPJsonUtil.fromJsonArrayToList(ads, SPHomeBanners.class);
									dataJson.put("banners", banners);
								}
							}
							successListener.onRespone("success", dataJson);
						}else {
							failuredListener.onRespone(msg , -1);
						}
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


}
