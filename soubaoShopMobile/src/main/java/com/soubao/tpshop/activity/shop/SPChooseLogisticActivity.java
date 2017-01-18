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
 * Date: @date 2015年11月12日 下午8:08:13
 * Description:购物车 -> 确认订单 -> 选择物流
 * @version V1.0
 */
package com.soubao.tpshop.activity.shop;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.common.SPBaseActivity;
import com.soubao.tpshop.adapter.SPLogisticAdapter;
import com.soubao.tpshop.global.SPMobileApplication;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/6/28.
 */
@EActivity(R.layout.order_choose_logistic)
public class SPChooseLogisticActivity extends SPBaseActivity  {


    @ViewById(R.id.order_choose_logistic_listv)
    ListView listView;

    SPLogisticAdapter mAdapter;
    JSONArray logisticArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.title_logistic));
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init(){
        super.init();
    }

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {
        logisticArray = SPMobileApplication.getInstance().jsonArray;
        JSONObject deliverJson = SPMobileApplication.getInstance().json;
        String checkCode = null;
        if (deliverJson != null){
            try {
                checkCode = deliverJson.getString("code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mAdapter = new SPLogisticAdapter(this , checkCode);
        listView.setAdapter(mAdapter);
        mAdapter.setData(logisticArray);
    }

    @Override
    public void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject jsonObject = logisticArray.getJSONObject(position);
                    SPMobileApplication.getInstance().json = jsonObject;

                    SPChooseLogisticActivity.this.setResult(1);
                    SPChooseLogisticActivity.this.finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }
            }
        });
    }
}
