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
 * Date: @date 2015年10月20日 下午7:52:58
 * Description:Activity 支付列表
 * @version V1.0
 */
package com.soubao.tpshop.activity.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.soubao.tpshop.R;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.model.order.SPOrder;
import com.soubao.tpshop.utils.PayResult;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPPluginUtils;
import com.soubao.tpshop.utils.SPServerUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.soubao.tpshop.utils.SignUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by admin on 2016/6/29.
 */
@EActivity(R.layout.pay_list)
public class SPPayListActivity extends  SPBaseActivity{

    private String TAG = "SPPayListActivity";

    @ViewById(R.id.pay_money_txtv)
    TextView payMoneyText;

    private final int SDK_PAY_FLAG = 1;
    private SPOrder order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.title_pay_list));
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public  void init(){
        super.init();
    }

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {

        if (getIntent()==null || getIntent().getSerializableExtra("order") == null){
            showToast("数据错误, 无法完成支付, 请检查! ");
            this.finish();
            return;
        }
       order = (SPOrder)getIntent().getSerializableExtra("order");
        payMoneyText.setText("¥"+order.getOrderAmount());
    }

    @Override
    public void initEvent() {

    }

    @Click({R.id.pay_alipay_aview , R.id.pay_cod_aview , R.id.pay_wechat_aview})
    public void onButtonClick(View v){
        String tip = getString(R.string.copyright_title);
        switch (v.getId()){
            case R.id.pay_alipay_aview:
                showToast(tip);
                break;
            case R.id.pay_wechat_aview:
                showToast(tip);
                break;
            case R.id.pay_cod_aview:
                showToast(tip);
                break;
        }
    }

}
