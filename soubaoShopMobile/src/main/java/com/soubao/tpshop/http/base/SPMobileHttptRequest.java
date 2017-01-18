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
 * Date: @date 2015年10月28日 下午8:13:39 
 * Description:所有URL请求的基类
 * @version V1.0
 */
package com.soubao.tpshop.http.base;

import android.content.Context;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.activity.common.SPIViewController;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.model.person.SPUser;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPCommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 *  网络请求基类, 实现GET , POS方法
 *  网络请求已经是异步实现, 调用时不需要额外开辟新线程调用
 * @author 飞龙
 *
 */
public class SPMobileHttptRequest {

	private static String TAG = "SouLeopardHttptRequest";
	public static void init(Context context){

	}
	

	private SPMobileHttptRequest(){
		
	}
	
	/**
	* @Description: 回调 
	* @param url	GET 请求URL
	* @param params		请求参数
	* @return void    返回类型
	* @throws
	 */
	public static void get(String url , RequestParams params ,JsonHttpResponseHandler responseHandler) {

		if (params == null){
			params = new RequestParams();
		}

		if (SPMobileApplication.getInstance().isLogined){
			SPUser user = SPMobileApplication.getInstance().getLoginUser();
			params.put("user_id" , user.getUserID());
			params.put("token" , user.getToken());
		}

		if (SPMobileApplication.getInstance().getDeviceId() !=null){
			String imei = SPMobileApplication.getInstance().getDeviceId();
			params.put("unique_id" , imei);
		}

		try {
			configSign(params , url);
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(url, params, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
			responseHandler.onFailure(-1 , new Header[]{},new Throwable(e.getMessage()) , new JSONObject());
		}

	}
	
	/**
	* @Description: POST回调 
	* @param url	请求URL
	* @param params		请求参数
	* @return void    返回类型
	* @throws
	 */
	public static void post(String url , RequestParams params , JsonHttpResponseHandler responseHandler){
		AsyncHttpClient client = new AsyncHttpClient();

		if (params == null){
			params = new RequestParams();
		}

		if (SPMobileApplication.getInstance().isLogined){
			SPUser user = SPMobileApplication.getInstance().getLoginUser();
			params.put("user_id" , user.getUserID());
			params.put("token" , user.getToken());//
		}else{
			if (SPMobileConstants.DevTest){
				params.put("user_id" , 1);
			}
		}

		if (SPMobileApplication.getInstance().getDeviceId() !=null){
			String imei = SPMobileApplication.getInstance().getDeviceId();
			params.put("unique_id" , imei);
		}

		try {
			configSign(params, url);
			client.post(url, params, responseHandler);

		} catch (Exception e) {
			e.printStackTrace();
			responseHandler.onFailure(-1 , new Header[]{},new Throwable(e.getMessage()) , new JSONObject());
		}
	}

	/**
	 * 根据控制器和action组装请求URL
	 * @param c
	 * @param action
	 * @return
	 */
	public static String getRequestUrl(String c , String action){
		return SPMobileConstants.BASE_URL +"&c="+c+"&a="+action;
	}

	/*public interface SuccessListener{
		public void onRespone(String msg , Object response);
	}

	public interface FailuredListener{
		public void onRespone(String msg , int errorCode);
	}*/




	/**
	 *  每一个访问接口都要调用改函数进行签名
	 *  具体签名方法, 参考: tpshop 开发手册 -> 手机api接口 -> 公共接口
	 *
	 *  @param params
	 */
	public static void configSign(RequestParams params , String url){

		long locaTime = SPCommonUtils.getCurrentTime();//本地时间
		long cutTime = SPMobileApplication.getInstance().getCutServiceTime();
		String time = String.valueOf(locaTime + cutTime);
		Map<String ,String>  paramsMap = convertRequestParamsToMap(params);
		String signFmt = SPCommonUtils.signParameter(paramsMap , time , SPMobileConstants.SP_SIGN_PRIVATGE_KEY , url);
		params.put("sign" , signFmt);
		params.put("time" , time);
	}

	public static Map<String ,String> convertRequestParamsToMap(RequestParams params){

		Map<String ,String> paramsMap = new HashMap<String ,String>();
		if(params!=null){
			try {
				String[] items = params.toString().split("&");
				if (items!=null){
					for(String keyValue : items ){
						String[] keyValues = keyValue.split("=");
						if (keyValues!=null && keyValues.length == 2){
							paramsMap.put(keyValues[0] , keyValues[1]);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return paramsMap;
	}

	public static List<String> convertJsonArrayToList(JSONArray jsonArray) throws JSONException {
		List<String> itemList = new ArrayList<String>();
		for(int i=0; i<jsonArray.length() ; i++){
			String item = jsonArray.getString(i);
			itemList.add(item);
		}
		return itemList;
	}

}
