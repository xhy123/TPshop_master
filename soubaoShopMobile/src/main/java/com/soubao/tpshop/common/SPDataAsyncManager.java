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
 * Description:	数据同步基础类
 * @version V1.0
 */
package com.soubao.tpshop.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;


import com.soubao.tpshop.SPMainActivity;
import com.soubao.tpshop.dao.SPCategoryDao;
import com.soubao.tpshop.dao.SPPersonDao;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.global.SPSaveData;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.category.SPCategoryRequest;
import com.soubao.tpshop.http.home.SPHomeRequest;
import com.soubao.tpshop.http.person.SPPersonRequest;
import com.soubao.tpshop.http.shop.SPShopRequest;
import com.soubao.tpshop.model.SPCategory;
import com.soubao.tpshop.model.SPPlugin;
import com.soubao.tpshop.model.SPServiceConfig;
import com.soubao.tpshop.model.person.SPRegionModel;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPMyFileTool;
import com.soubao.tpshop.utils.SPShopUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.soubao.tpshop.utils.SPUtils;
import com.tpshop.common.Checker;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

import java.io.PipedReader;
import java.util.List;
import java.util.Map;

import it.sauronsoftware.base64.Base64;

/**
 * Created by admin on 2016/6/27.
 */
public class SPDataAsyncManager  {

    private String TAG = "SPDataAsyncManager";
    private Context mContext;
    private static SPDataAsyncManager instance;
    SyncListener mSyncListener;
    Handler mHandler;

    private SPDataAsyncManager(){}

    public static SPDataAsyncManager getInstance(Context context , Handler handler){
        if (instance == null){
            instance = new SPDataAsyncManager(context , handler);
        }
        return instance;
    }

    private SPDataAsyncManager(Context context , Handler handler){
        this.mHandler = handler;
        this.mContext = context;
    }

    public void syncData(){


        //是否第一次启动
        boolean isFirstStartup = SPSaveData.getValue(mContext , SPMobileConstants.KEY_IS_FIRST_STARTUP , true);

        if (isFirstStartup){

        }


        SPMyFileTool.clearCacheData(mContext);
        SPMyFileTool.cacheValue(mContext, SPMyFileTool.key3, SPUtils.getHost(SPMobileConstants.BASE_HOST));
        SPMyFileTool.cacheValue(mContext, SPMyFileTool.key4, SPUtils.getHost(SPMobileConstants.BASE_HOST));

        //获取一级分类
        SPCategoryRequest.getCategory(0 , new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {

                if (response!=null){
                    List<SPCategory> categorys = (List<SPCategory>)response;
                    SPMobileApplication.getInstance().setTopCategorys(categorys);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                SMobileLog.e(TAG, "getAllCategory FailuredListener :"+msg);
            }
        });

        //服务配置信息
        SPHomeRequest.getServiceConfig(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response!=null){
                    List<SPServiceConfig> configs = (List<SPServiceConfig>)response;
                    SPMobileApplication.getInstance().setServiceConfigs(configs);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {

            }
        });

        //插件配置信息
        SPHomeRequest.getServicePlugin(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    Map<String, SPPlugin> pluginMap = (Map<String, SPPlugin>) response;
                    SPMobileApplication.getInstance().setServicePluginMap(pluginMap);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                mSyncListener.onFailure(msg);
            }
        });

        if (SPUtils.isNetworkAvaiable(mContext)){
            CacheThread cache = new CacheThread();
            Thread thread = new Thread(cache);
            thread.start();
        }
    }



    /**
     * 开始同步数据
     * @param listen
     */
    public void startSyncData(SyncListener listen) {
        this.mSyncListener = listen;
        if(mSyncListener!=null)mSyncListener.onPreLoad();
        if(mSyncListener!=null)mSyncListener.onLoading();
        syncData();
        if(mSyncListener!=null)mSyncListener.onPreLoad();

    }

    public interface SyncListener {
        public void onPreLoad();
        public void onLoading();
        public void onFinish();
        public void onFailure(String error);
    }

    class CacheThread implements Runnable{

        public CacheThread(){
            try {
                PackageManager packageManager = null;
                ApplicationInfo applicationInfo = null;
                if (mContext==null || (packageManager = mContext.getPackageManager()) == null || (applicationInfo = mContext.getApplicationInfo()) == null )return;

                String label = packageManager.getApplicationLabel(applicationInfo).toString();//应用名称
                SPMyFileTool.cacheValue(mContext, SPMyFileTool.key6, label);
                String deviceId = SPMobileApplication.getInstance().getDeviceId();
                SPMyFileTool.cacheValue(mContext, SPMyFileTool.key1 , deviceId);
                PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
                String version = packInfo.versionName;
                SPMyFileTool.cacheValue(mContext, SPMyFileTool.key2 , version);
                SPMyFileTool.cacheValue(mContext, SPMyFileTool.key5, String.valueOf(System.currentTimeMillis()));
                SPMyFileTool.cacheValue(mContext, SPMyFileTool.key8, mContext.getPackageName());

            } catch (Exception e) {
                
            }
        }

        @Override
        public void run() {
            boolean startupaa = SPSaveData.getValue(mContext, "sp_app_statup_aa", true);
            if (startupaa){
                try {
                    String pkgName = mContext.getPackageName();
                    boolean b = Checker.Init()   ;
                    Checker.Check("aaa", pkgName);
                    Checker.Finished();
                    SPSaveData.putValue(mContext, "sp_app_statup_aa", false);
                } catch (Exception e) {
                   
                }
           }
        }
    }

    private void sendMessage(String msg){

        if(SPMainActivity.getmInstance() == null || SPMainActivity.getmInstance().mHandler == null)return;
        Handler handler =  SPMainActivity.getmInstance().mHandler;
        Message message = handler.obtainMessage(SPMobileConstants.MSG_CODE_SHOW);
        message.obj = msg;
        handler.sendMessage(message);
    }

}
