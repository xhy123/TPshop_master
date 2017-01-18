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
 * Date: @date 2015年10月29日 下午10:01:49
 * Description: 服务器插件配置信息
 * @version V1.0
 */
package com.soubao.tpshop.model;

import org.json.JSONObject;

/**
 * Created by admin on 2016/6/30.
 */
public class SPPlugin implements SPModel{

    String  code;
    String  name;
    String  icon;
    JSONObject configValue;
    String  status;
    String  type;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{"configValue","config_value"};
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public JSONObject getConfigValue() {
        return configValue;
    }

    public void setConfigValue(JSONObject configValue) {
        this.configValue = configValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
