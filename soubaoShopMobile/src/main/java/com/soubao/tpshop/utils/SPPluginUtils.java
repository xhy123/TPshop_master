package com.soubao.tpshop.utils;

import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.model.SPPlugin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/6/30.
 */
public class SPPluginUtils {

    final static String PLUGIN_ALIPAY_PARTNER = "alipay_partner";
    final static String PLUGIN_ALIPAY_ACCOUNT = "alipay_account";
    final static String PLUGIN_ALIPAY_PRIVATE_KEY = "alipay_private_key";
    final static String PLUGIN_ALIPAY_CODE = "alipay";    //支付宝code
    final static String PLUGIN_UNIONPAY_CODE = "unionpay";    //银联在线支付
    final static String PLUGIN_WIXINPAY_CODE = "weixin";    //支付宝code
    final static String PLUGIN_CODPAY_CODE  = "cod";    //支付宝code

    /**
     *  支付宝支付 - 获取商户号ID
     *
     *  @return return value description
     */
    public static String getAlipayPartner(){
        return getAlipayConfigValueWithKey(PLUGIN_ALIPAY_PARTNER);
    }


    /**
     *  支付宝支付 - 获取支付账户
     *
     *  @return return value description
     */
    public static String getAlipayAccount(){
        return getAlipayConfigValueWithKey(PLUGIN_ALIPAY_ACCOUNT);

    }

    /**
     *  支付宝支付 - 密钥
     *
     *  @return return value description
     */
    public static String getAlipayPrivateKey(){
        return getAlipayConfigValueWithKey(PLUGIN_ALIPAY_PRIVATE_KEY);
    }

    public static String getAlipayConfigValueWithKey(String key){

        Map<String , SPPlugin> pluginMap = SPMobileApplication.getInstance().getServicePluginMap();
        if (pluginMap == null)return null;
        SPPlugin plugin = pluginMap.get(PLUGIN_ALIPAY_CODE);
        JSONObject configValue = plugin.getConfigValue();
        try {
            if (configValue != null && configValue.has(key)){
                return configValue.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
