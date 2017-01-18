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
 * Description:Activity 公共类, 获取文本
 * @version V1.0
 */
package com.soubao.tpshop.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.utils.SPStringUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by admin on 2016/6/28.
 */
@EActivity(R.layout.text_field)
public class SPTextFieldViewActivity extends SPBaseActivity {

    @ViewById(R.id.value_edtv)
    EditText valueEtv;

    @ViewById(R.id.validate_txtv)
    TextView validateTxtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true , getString(R.string.title_getcontent));
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

        String placeHolder = getIntent().getStringExtra("placeHolder");
        String validate = getIntent().getStringExtra("validate");
        String value = getIntent().getStringExtra("value");

        valueEtv.setText(value);
        valueEtv.setHint(placeHolder);
        validateTxtv.setText(validate);
    }

    @Override
    public void initEvent() {

    }

    @Click({R.id.ok_btn})
    public void onButtonClick(View v){

        if (v.getId() == R.id.ok_btn){

            String value = valueEtv.getText().toString();
            if(SPStringUtils.isEmpty(value)){
                showToast("请输入");
                return;
            }

            Intent intent = new Intent();
            intent.putExtra("value" , value);
            setResult(SPMobileConstants.Result_Code_GetValue, intent);
            this.finish();

        }

    }

}
