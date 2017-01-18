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
 * Date: @date 2015年10月27日 下午9:14:42
 * Description: 地区  model
 * @version V1.0
 */
package com.soubao.tpshop.model.person;

import com.soubao.tpshop.model.SPModel;

/**
 * Created by admin on 2016/6/27.
 */
public class SPRegionModel implements SPModel {
    
    //地区ID
    String regionID;
    //父地区ID
    String parentID;
    String name;
    String level;//1: 省份 , 2: 城市 , 3: 地区

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
            "regionID","id",
            "parentID","parent_id"
        };
    }


    public String getRegionID() {
        return regionID;
    }

    public void setRegionID(String regionID) {
        this.regionID = regionID;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
