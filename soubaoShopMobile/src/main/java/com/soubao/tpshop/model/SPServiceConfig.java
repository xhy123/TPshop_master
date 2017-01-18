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
 * Date: @date 2015年10月23日 下午9:04:07
 * Description: 服务器配置Model
 * @version V1.0
 */
package com.soubao.tpshop.model;

/**
 * Created by admin on 2016/6/29.
 */
public class SPServiceConfig implements SPModel {

    private String  configID;

    private String  name;

    private String  value;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
            "configID","id"
        };
    }

    public String getConfigID() {
        return configID;
    }

    public void setConfigID(String configID) {
        this.configID = configID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
