/**
 * tpshop
 * ============================================================================
 * * 版权所有 2015-2027 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ----------------------------------------------------------------------------
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * $Author: Ben  16/07/08
 * $description: 登录
 */
package com.soubao.tpshop.activity.person.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.SPMainActivity;
import com.soubao.tpshop.activity.common.SPBaseActivity;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.person.SPUserRequest;
import com.soubao.tpshop.model.person.SPUser;
import com.soubao.tpshop.utils.SPDialogUtils;
import com.soubao.tpshop.utils.SPStringUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_splogin)
public class SPLoginActivity extends SPBaseActivity {

    private String TAG = "SPLoginActivity";

    @ViewById(R.id.edit_phone_num)
    EditText txtPhoneNum;
    @ViewById(R.id.edit_password)
    EditText txtPassword;
    @ViewById(R.id.btn_login)
    Button btnLogin;
    @ViewById(R.id.txt_register)
    TextView txtRegister;
    @ViewById(R.id.txt_forget_pwd)
    TextView txtForgetPwd;

    @ViewById(R.id.qq_icon_txt)
    TextView qqIconTxt;

    @ViewById(R.id.wx_icon_txt)
    TextView wxIconTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true,true,getString(R.string.login_title));
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

    }

    @Override
    public void initEvent() {

    }

    public void onLoginClick(View view){
        if (SPStringUtils.isEditEmpty(txtPhoneNum)) {
            txtPhoneNum.setError(Html.fromHtml("<font color='red'>"+getString(R.string.login_phone_number_null)+"</font>"));
            return;
        }
        if (SPStringUtils.isEditEmpty(txtPassword)) {
            txtPassword.setError(Html.fromHtml("<font color='red'>"+getString(R.string.login_password_null)+"</font>"));
            return;
        }
        showLoadingToast("正在登录...");
        SPUserRequest.doLogin(txtPhoneNum.getText().toString(),txtPassword.getText().toString(),
                new SPSuccessListener(){
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            SPUser user = (SPUser)response;
                            SPMobileApplication.getInstance().setLoginUser(user);
							SPLoginActivity.this.sendBroadcast(new Intent(SPMobileConstants.ACTION_LOGIN_CHNAGE));
                            showToast("登录成功");
                            loginSuccess();
                        }
                    }
                },new SPFailuredListener(){
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingToast();
                        showToast(msg);
                    }
                });

    }

    private void loginSuccess(){
        Intent intent = new Intent();
        intent.setClass(this,SPMainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onRegisterClick(View view){
        startActivity(new Intent(this, SPRegisterActivity_.class));
    }

    public void onForgetPwdClick(View view){
        startActivity(new Intent(this, SPForgetPwdActivity_.class));
    }

    @Click({R.id.qq_icon_txt ,R.id.wx_icon_txt})
    public void onClickListener(View v) {
        SPDialogUtils.showToast(this , getString(R.string.copyright_title));
    }
}
