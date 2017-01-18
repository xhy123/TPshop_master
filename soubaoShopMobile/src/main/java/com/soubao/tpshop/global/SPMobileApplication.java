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
 * Date: @date 2015年10月28日 下午8:10:34 
 * Description:Application
 * @version V1.0
 */
package com.soubao.tpshop.global;

import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.model.SPCategory;
import com.soubao.tpshop.model.SPPlugin;
import com.soubao.tpshop.model.SPServiceConfig;
import com.soubao.tpshop.model.person.SPUser;
import com.soubao.tpshop.model.shop.SPCollect;
import com.soubao.tpshop.utils.SPMyFileTool;
import com.soubao.tpshop.utils.SPStringUtils;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @author 飞龙
 *
 */
public class SPMobileApplication extends Application {

	private static SPMobileApplication instance ;

	public List<SPCollect> collects;

	public boolean isLogined ;
	private SPUser loginUser ;
	private String deviceId ;
	DisplayMetrics mDisplayMetrics;
	public JSONObject json;
	public JSONObject json1;
	public Map<String , String> map;
	public List list;
	public JSONArray jsonArray;
	public int productListType = 1; //1: 商品列表, 2: 产品搜索结果列表
	private long cutServiceTime;
	private List<SPCategory> topCategorys;//分类左边菜单一级分类
	private List<SPServiceConfig> serviceConfigs;
	private Map<String , SPPlugin> servicePluginMap;
	private TelephonyManager telephonyManager;

	@Override
	public void onCreate() {
		super.onCreate();

		/** 初始化 Vollery 网络请求 */
		SPMobileHttptRequest.init(getApplicationContext());
		/** 初始化 Facebook SimpleDraweeView 网络请求 */

		loginUser = SPSaveData.loadUser(getApplicationContext());
		if (SPStringUtils.isEmpty(loginUser.getUserID()) || loginUser.getUserID().equals("-1")){
			isLogined = false;
		}else{
			isLogined = true;
		}
		instance = this;
		//初始化购物车管理类
		SPShopCartManager.getInstance(getApplicationContext());


		PackageManager manager = this.getPackageManager();
		mDisplayMetrics = getResources().getDisplayMetrics();

		telephonyManager = (TelephonyManager)getApplicationContext().getSystemService(TELEPHONY_SERVICE);
		if(telephonyManager!=null){
			String deviceId = telephonyManager.getDeviceId();
			SPMyFileTool.cacheValue(this, SPMyFileTool.key1, deviceId);
		}
	}

	public DisplayMetrics getDisplayMetrics(){
		return mDisplayMetrics;
	}

	public static SPMobileApplication getInstance(){
		return instance;
	}

	public List<SPServiceConfig> getServiceConfigs() {
		return serviceConfigs;
	}

	public void setServiceConfigs(List<SPServiceConfig> serviceConfigs) {
		this.serviceConfigs = serviceConfigs;
	}

	public Map<String, SPPlugin> getServicePluginMap() {
		return servicePluginMap;
	}

	public void setServicePluginMap(Map<String, SPPlugin> servicePluginMap) {
		this.servicePluginMap = servicePluginMap;
	}

	public SPUser getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(SPUser loginUser) {
		this.loginUser = loginUser;
		if (this.loginUser !=null){
			SPSaveData.saveUser(getApplicationContext() , "user" ,this.loginUser);
			isLogined = true;
		}else{
			isLogined = false;
		}
	}

	public String getApplicationName() {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	public String getSystemPackage(){
		return this.getPackageName();
	}

	//退出登录
	public void exitLogin(){
		loginUser = null;
		isLogined = false;
		SPSaveData.clearUser(getApplicationContext());

	}

	public List<SPCategory> getTopCategorys() {
		return topCategorys;
	}

	public void setTopCategorys(List<SPCategory> topCategorys) {
		this.topCategorys = topCategorys;
	}

	/**
	 * 获取设备IMEI
	 * @return
	 */
	public String getDeviceId() {
		if (telephonyManager!=null){
			deviceId = telephonyManager.getDeviceId();//String
		}else{
			deviceId = "unionid001";
		}
		return deviceId;
	}

	public long getCutServiceTime() {
		return cutServiceTime;
	}

	public void setCutServiceTime(long cutServiceTime) {
		this.cutServiceTime = cutServiceTime;
	}
}
