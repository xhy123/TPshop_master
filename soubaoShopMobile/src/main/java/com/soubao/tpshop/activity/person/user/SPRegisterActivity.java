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
 * $description: 注册
 */
package com.soubao.tpshop.activity.person.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.SPMainActivity;
import com.soubao.tpshop.activity.common.SPBaseActivity;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.person.SPUserRequest;
import com.soubao.tpshop.model.person.SPUser;
import com.soubao.tpshop.utils.SPStringUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_spregister)
public class SPRegisterActivity extends SPBaseActivity {

    @ViewById(R.id.layout_first)
    RelativeLayout layoutFirst;
    @ViewById(R.id.layout_second)
    RelativeLayout layoutSecond;
    @ViewById(R.id.layout_third)
    RelativeLayout layoutThird;


    @ViewById(R.id.editPhoneNum)
    EditText editPhoneNumber;
    @ViewById(R.id.btn_next_1)
    Button btnNext1;
    @ViewById(R.id.txt_register_phone)
    TextView txtInfo;
    @ViewById(R.id.edit_code)
    EditText editCode;
    @ViewById(R.id.edit_password)
    EditText editPwd;
    @ViewById(R.id.edit_re_password)
    EditText editRePwd;

    @ViewById(R.id.btn_next_2)
    Button btnNext2;
    @ViewById(R.id.btn_send_sms)

    Button btnSendSMS;
    @ViewById(R.id.txt_error_info)
    TextView txtErrorInfo;

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true,true,getString(R.string.register_title));
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

    public void onBtnReCodeClick(View view){
        //send SMS
        SPUserRequest.sendSMSRegCode(phoneNumber, new SPSuccessListener(){
            @Override
            public void onRespone(String msg, Object response) {
                //if (response != null) {
                showToast(msg);
                //}
            }
        },new SPFailuredListener(){
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
        btnSendSMS.setEnabled(false);
        countDownTimer.start();
    }

    /**
     * First Next click
     * @param view
     */
    public void onBtnNextOneClick(View view){
        //check input
        if (isEditEmpty(editPhoneNumber)) {
            editPhoneNumber.setError(Html.fromHtml("<font color='red'>"+getString(R.string.register_phone_number_null)+"</font>"));
            return;
        }
        phoneNumber= editPhoneNumber.getText().toString();
        //send SMS
        SPUserRequest.sendSMSRegCode(phoneNumber, new SPSuccessListener(){
            @Override
            public void onRespone(String msg, Object response) {
                //if (response != null) {
                    showToast(msg);
                //}
            }
        },new SPFailuredListener(){
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });

        layoutFirst.setVisibility(View.GONE);
        layoutSecond.setVisibility(View.VISIBLE);
        layoutThird.setVisibility(View.GONE);
        txtInfo.setText(getResources().getString(R.string.register_sub_title,phoneNumber));
        btnSendSMS.setEnabled(false);
        countDownTimer.start();
    }

    public void onBtnNextTowClick(View view){
        if (SPStringUtils.isEditEmpty(editCode)) {
            editCode.setError(Html.fromHtml("<font color='red'>"+getString(R.string.edit_code_null)+"</font>"));
            return;
        }
        layoutFirst.setVisibility(View.GONE);
        layoutSecond.setVisibility(View.GONE);
        layoutThird.setVisibility(View.VISIBLE);
        countDownTimer.cancel();
    }

    public void onBtnNextThreeClick(View view){
        String pwd = editPwd.getText().toString();
        String repwd = editRePwd.getText().toString();
        if (isEditEmpty(editPwd)) {
            editPwd.setError(Html.fromHtml("<font color='red'>"+getString(R.string.register_password_null)+"</font>"));
            return;
        }
        if ( isEditEmpty(editRePwd)){
            editRePwd.setError(Html.fromHtml("<font color='red'>"+getString(R.string.register_password_null)+"</font>"));
            return;
        }
        if (!pwd.equals(repwd)){
            txtErrorInfo.setVisibility(View.VISIBLE);
            txtErrorInfo.setText(getString(R.string.register_error_info_re));
            return;
        }
        if (pwd.length()< 6 || pwd.length() > 20) {
            txtErrorInfo.setText(getString(R.string.register_error_info));
            txtErrorInfo.setVisibility(View.VISIBLE);
            return;
        }

        //Register
        SPUserRequest.doRegister(phoneNumber,pwd,editCode.getText().toString(),
                new SPSuccessListener(){
                    @Override
                    public void onRespone(String msg, Object response) {
                        if (response != null) {
                            SPUser user = (SPUser)response;
                            //showToast(user.toString());
                            SPMobileApplication.getInstance().setLoginUser(user);
                            SPMobileApplication.getInstance().isLogined = true;
                            startActivity(new Intent(SPRegisterActivity.this, SPMainActivity.class));
                        }
                    }
                },new SPFailuredListener(){
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        showToast(msg);
                        //txtErrorInfo.setText("注册成功");
                        txtErrorInfo.setText(msg);
                        txtErrorInfo.setVisibility(View.VISIBLE);
                    }
                });

    }

    private  boolean isEditEmpty(EditText text){
        return  text == null || "".equals(text.getText().toString());
    }

    public CountDownTimer countDownTimer = new CountDownTimer(60*1000,1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            btnSendSMS.setText(getString(R.string.register_btn_re_code,millisUntilFinished / 1000));
        };

        @Override
        public void onFinish() {
            btnSendSMS.setText(getString(R.string.register_btn_re_code_done));
            btnSendSMS.setEnabled(true);
        };
    };
}
