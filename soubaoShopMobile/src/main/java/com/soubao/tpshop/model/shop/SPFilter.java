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
 * Date: @date 2015年11月26日 下午10:13:37
 * Description:商品列表, 过滤器
 * @version V1.0
 */

package com.soubao.tpshop.model.shop;

import com.soubao.tpshop.model.SPModel;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by admin on 2016/6/18.
 */
public class SPFilter implements SPModel {
    private String filterId;
    private String name;
    private List<SPFilterItem> items;
    private JSONArray itemJsonArray;


    public SPFilter(){};

    public SPFilter(int type , String filterId ,String name , List<SPFilterItem> items){
        this.type = type;
        this.name = name;
        this.filterId = filterId;
        this.items = items;

    }


    /**
     * 类型 type ->  1:选中菜单 , 2:规格 ,3:属性 , 4:品牌 , 5:价格
     */
    private Integer type;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{"itemJsonArray","item"};
    }


    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SPFilterItem> getItems() {
        return items;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public JSONArray getItemJsonArray() {
        return itemJsonArray;
    }

    public void setItemJsonArray(JSONArray itemJsonArray) {
        this.itemJsonArray = itemJsonArray;
    }

    public void setItems(List<SPFilterItem> items) {
        this.items = items;
    }
}
