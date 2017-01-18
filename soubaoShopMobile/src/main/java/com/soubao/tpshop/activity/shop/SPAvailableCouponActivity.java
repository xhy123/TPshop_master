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
 * $description: 商城 -> 购物车 -> 确认订单 -> 优惠券/代金券
 */

package com.soubao.tpshop.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.common.SPBaseActivity;
import com.soubao.tpshop.adapter.SPOrderCouponAdapter;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.model.shop.SPCoupon;
import com.soubao.tpshop.utils.SPStringUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.order_available_coupon)
public class SPAvailableCouponActivity extends SPBaseActivity {

    private  String TAG = "SPAvailableCouponActivity";


    @ViewById(R.id.order_coupon_listv)
    ListView couponListv;

    @ViewById(R.id.coupon_use_btn)
    Button useBtn;

    @ViewById(R.id.coupon_check_btn)
    Button checkBtn;

    @ViewById(R.id.coupon_edtv)
    EditText couponEtxtv;

    public static final int MSG_CODE_CHECK_CLICK = 1;
    Handler mHandler ;
    SPOrderCouponAdapter mAdapter;
    List<SPCoupon> coupons;
    SPCoupon selectCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true , true, getString(R.string.person_coupon));
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

        coupons = (List<SPCoupon>)SPMobileApplication.getInstance().list;
        if (getIntent()!=null){
            selectCoupon = (SPCoupon)getIntent().getSerializableExtra("coupon");
        }

        if (selectCoupon!=null && selectCoupon.getCouponType() == 2){
            checkBtn.setBackgroundResource(R.drawable.icon_checked);
        }else{
            checkBtn.setBackgroundResource(R.drawable.icon_checkno);
        }
        mAdapter = new SPOrderCouponAdapter(this , coupons , selectCoupon);
        couponListv.setAdapter(mAdapter);
    }

     @Override
     public void initEvent() {
         couponListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 checkBtn.setBackgroundResource(R.drawable.icon_checkno);
                 selectCoupon = coupons.get(position);
                 selectCoupon.setCouponType(1);
                 if (mAdapter!= null)mAdapter.setSelectCoupon(selectCoupon);
             }
         });
    }

    @Click({R.id.coupon_check_btn , R.id.coupon_use_btn})
    public void onButtonClick(View v) {
        if (v.getId() == R.id.coupon_check_btn){
            //优惠码
            selectCoupon = new SPCoupon();
            selectCoupon.setCouponType(2);
            checkBtn.setBackgroundResource(R.drawable.icon_checked);
            if (mAdapter!= null)mAdapter.setSelectCoupon(selectCoupon);
        }else if(v.getId() == R.id.coupon_use_btn){
            if (selectCoupon == null){
                showToast("请选择优惠券");
                return;
            }
            if (selectCoupon != null && selectCoupon.getCouponType() == 2) {
                //优惠码
                String code = couponEtxtv.getText().toString();
                if(SPStringUtils.isEmpty(code)){
                    showToast("请输入优惠码");
                    return;
                }
                selectCoupon.setCode(code);
            }
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectCoupon" , selectCoupon);
            setResult(2, resultIntent);
            this.finish();
        }
    }

}
