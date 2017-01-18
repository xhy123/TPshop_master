/**
 * tpshop
 * ============================================================================
 * * 版权所有 2015-2027 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ----------------------------------------------------------------------------
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * $Author: 飞龙  16/01/15 $
 * $description: 服务操数据配置获取
 */
package com.soubao.tpshop.utils;

import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.model.SPServiceConfig;

import java.util.List;

/**
 * Created by admin on 2016/6/29.
 */
public class SPServerUtils {

    final static String  CONFIG_TPSHOP_HTTP = "tpshop_http";
    final static String  CONFIG_QQ = "qq";
    final static String  CONFIG_QQ1 = "qq1";
    final static String  CONFIG_STORE_NAME = "store_name";
    final static String  CONFIG_POINT_RATE = "point_rate";//积分抵扣金额
    
    final static String  CONFIG_PHONE = "phone";
    final static String  CONFIG_ADDRESS = "address";
    final static String  CONFIG_WORK_TIME = "worktime";
    final static String  CONFIG_HOT_KEYWORDS = "hot_keywords";
    final static String  CONFIG_KEY_SMS_TIME_OUT = "sms_time_out";
    final static String  CONFIG_KEY_REG_SMS_ENABLE = "sms_enable";
    /**
     *  获取联系客服QQ
     *
     *  @return return value description
     */
    public static String getCustomerQQ(){
        return getConfigValue(CONFIG_QQ);
    }


/**
 *  获取商城名称
 *
 *  @return return value description
 */
    public static String getStoreName(){
        return getConfigValue(CONFIG_STORE_NAME);
    }


/**
 *  获取积分抵扣金额
 *
 *  @return return value description
 */
    public static String getPointRate(){
        return getConfigValue(CONFIG_POINT_RATE);
    }

/**
 *  根据名称获取配置的值
 *
 *  @param name name description
 *
 *  @return return value description
 */
  public static String getConfigValue(String name){

      List<SPServiceConfig> serviceConfigs = SPMobileApplication.getInstance().getServiceConfigs();

        if (serviceConfigs!= null && serviceConfigs.size() > 0) {
            for (SPServiceConfig  config : serviceConfigs) {
                if (name.equals(config.getName())){
                    return config.getValue();
                }
            }
        }
        return name;
    }

/**
 *  上班时间
 *
 *  @return return value description
 */
    public static String getWorkTime(){
        String worktime = getConfigValue(CONFIG_WORK_TIME);
        if(SPStringUtils.isEmpty(worktime)){
            worktime = "(周一致周五) 08:00-19:00 (周六日) 休息";
        }
        return worktime;
    }


/**
 *  售后收货地址
 *
 *  @return return value description
 */
    public static String getAddress(){
        return getConfigValue(CONFIG_ADDRESS);
    }

/**
 *  售后客服电话
 *
 *  @return return value description
 */
    public static String getServicePhone(){
        return getConfigValue(CONFIG_PHONE);
    }


/**
 *  搜索关键词
 *
 *  @return return value description
 */
   public static List<String> getHotKeyword(){
       List<String> hotwords = null;
       String hotword = getConfigValue(CONFIG_HOT_KEYWORDS);
        if (!SPStringUtils.isEmpty(hotword)) {
            hotwords = SPStringUtils.stringToList(hotword , "|");
        }
        return hotwords;
    }


/**
 *  注册短信, 超时时间(单位:s)
 *
 *  @return
 */
   public static int getSmsTimeOut(){

        String timeout = getConfigValue(CONFIG_KEY_SMS_TIME_OUT);
        if (!SPStringUtils.isEmpty(timeout)) {
            return Integer.valueOf(timeout);
        }
        return 0;
    }

/**
 *  是否启用短信验证码
 *
 *  @return
 */
    public static boolean enableSmsCheckCode() {
        String smsEnable = getConfigValue(CONFIG_KEY_REG_SMS_ENABLE);
        if (!SPStringUtils.isEmpty(smsEnable)) {
            return Boolean.valueOf(smsEnable);
        }
        return false;
    }
}
