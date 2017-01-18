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
 * Description:确认订单
 * @version V1.0
 */
package com.soubao.tpshop.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.common.SPBaseActivity;
import com.soubao.tpshop.activity.common.SPPayListActivity_;
import com.soubao.tpshop.activity.common.SPTextFieldViewActivity_;
import com.soubao.tpshop.activity.person.address.SPConsigneeAddressListActivity_;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.global.SPShopCartManager;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.person.SPPersonRequest;
import com.soubao.tpshop.http.shop.SPShopRequest;
import com.soubao.tpshop.model.SPProduct;
import com.soubao.tpshop.model.person.SPConsigneeAddress;
import com.soubao.tpshop.model.order.SPOrder;
import com.soubao.tpshop.model.shop.SPCoupon;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPServerUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.soubao.tpshop.utils.SPUtils;
import com.soubao.tpshop.view.SPArrowRowView;
import com.soubao.tpshop.view.SwitchButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

@EActivity(R.layout.order_confirm_order)
public class SPConfirmOrderActivity extends SPBaseActivity {

    private String TAG = "SPConfirmOrderActivity";

    @ViewById(R.id.order_consignee_txtv)
    TextView consigneeTxtv;

    @ViewById(R.id.order_address_txtv)
    TextView addressTxtv;


    @ViewById(R.id.product_list_gallery_lyaout)
    LinearLayout mGallery;

    @ViewById(R.id.order_product_count_txtv)
    TextView productCountTxtv;

    @ViewById(R.id.order_deliver_aview)
    SPArrowRowView deliverAview;

    @ViewById(R.id.order_coupon_aview)
    SPArrowRowView couponAview;

    @ViewById(R.id.order_invoce_aview)
    SPArrowRowView invoceAview;

    @ViewById(R.id.fee_goodsfee_txtv)
    TextView feeGoodsFeeTxtv;

    @ViewById(R.id.fee_shopping_txtv)
    TextView feeShoppingTxtv;

    @ViewById(R.id.fee_coupon_txtv)
    TextView feeCouponTxtv;

    @ViewById(R.id.fee_point_txtv)
    TextView feePointTxtv;

    @ViewById(R.id.fee_balance_txtv)
    TextView feeBalanceTxtv;

    @ViewById(R.id.fee_amount_txtv)
    TextView feeAmountTxtv;

    @ViewById(R.id.payfee_txtv)
    TextView payfeeTxtv;

    @ViewById(R.id.order_balance_sth)
    SwitchButton bananceSth;

    @ViewById(R.id.order_balance_txtv)
    TextView balanceTxtv;

    @ViewById(R.id.order_point_sth)
    SwitchButton pointSth;

    @ViewById(R.id.order_point_txtv)
    TextView pointTxtv;

    @ViewById(R.id.buy_time_txtv)
    TextView buyTimeTxtv;



    /********* 服务器拉取的数据源集合 *****************/
    SPConsigneeAddress consigneeAddress ;   //当前收货人信息
    List<SPProduct> products;               //商品列表
    List<SPCoupon> coupons;                  //优惠券列表
    JSONObject userinfoJson;                //用户信息(积分,余额)
    JSONObject amountDict;                   //结算金额汇总
    JSONArray deliverArray;                 //配送方式: 数据源

    /********* 选中的数据 *****************/
    JSONObject selectedDeliverJson;         //当前选中的物流
    SPCoupon selectedCoupon;                //当前选中的优惠券
    int points;                             //使用的积分
    float balance;                          //使用的余额
    String invoice;                         //发票


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.title_confirm_order));
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init(){

        super.init();

    }

    public void refreshData(){
        showLoadingToast();
        SPShopRequest.getConfirmOrderData(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                mDataJson = (JSONObject) response;
                try {
                    if (mDataJson != null) {
                        if (mDataJson.has("consigneeAddress")) {
                            consigneeAddress = (SPConsigneeAddress) mDataJson.get("consigneeAddress");
                        }
                        if (mDataJson.has("delivers")) {
                            deliverArray = (JSONArray) mDataJson.getJSONArray("delivers");
                        }
                        if (mDataJson.has("products")) {
                            products = (List<SPProduct>) mDataJson.get("products");
                        }
                        if (mDataJson.has("coupons")) {
                            coupons = (List<SPCoupon>) mDataJson.get("coupons");
                        }
                        if (mDataJson.has("userInfo")) {
                            userinfoJson = mDataJson.getJSONObject("userInfo");
                        }

                        dealModel();
                        refreshView();
                        //load 商品金额信息
                        loadTotalFee();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }

            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
        initEvent();
    }

    /**
     *  处理服务器获取的数据
     */
    public void dealModel(){

        if (products == null)return;

        try {
            //默认支付
            if (consigneeAddress != null ) {
                //NSString* firstAddress = [[SPDataBaseManager shareInstance] queryFirstAddress:self.consigneeAddress.province cityID:self.consigneeAddress.city districtID:self.consigneeAddress.district];
               consigneeAddress.setFullAddress("广东省深圳市"+consigneeAddress.getAddress());
            }

            //默认第一个物流
            if (deliverArray!=null && deliverArray.length() > 0) {
                selectedDeliverJson = deliverArray.getJSONObject(0);
            }

            //设置商品图片
            buildProductGallery();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildProductGallery(){
        for (int i = 0; i < products.size(); i++){
            String url = products.get(i).getImageThumlUrl();
            View view = LayoutInflater.from(this).inflate(R.layout.activity_index_gallery_item, mGallery, false);
            ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
            Glide.with(this).load(url).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);
            mGallery.addView(view);
        }
    }

    /**
     * 刷新页面数据
     */
    public void refreshView(){
        try {
            consigneeTxtv.setText(consigneeAddress.getConsignee() +"  "+consigneeAddress.getMobile());
            addressTxtv.setText(consigneeAddress.getFullAddress());

            if (products != null || products.size() > 0 ){
                productCountTxtv.setText("共" + products.size() + "件商品");
            }

            if (selectedDeliverJson!=null && selectedDeliverJson.has("name")){
                deliverAview.setSubText(selectedDeliverJson.getString("name"));
            }

            if(selectedCoupon != null ){
                if (selectedCoupon.getCouponType() == 1){
                    couponAview.setSubText(selectedCoupon.getName());
                }else{
                    couponAview.setSubText(selectedCoupon.getCode());
                }
            }


            if (amountDict==null)return;

            //订单支付信息
            if (amountDict.has("goodsFee")){
                feeGoodsFeeTxtv.setText("¥"+amountDict.getString("goodsFee"));
            }

            if (amountDict.has("postFee")){
                feeShoppingTxtv.setText("¥"+amountDict.getString("postFee"));
            }

            if (amountDict.has("couponFee")){
                feeCouponTxtv.setText("¥"+amountDict.getString("couponFee"));
            }

            if (amountDict.has("pointsFee")){
                feePointTxtv.setText("¥" + amountDict.getString("pointsFee"));
            }

            if (amountDict.has("balance")){
                feeBalanceTxtv.setText("¥"+amountDict.getString("balance"));
            }
            if (amountDict.has("payables")){

                String payablesFmt = "实付款:¥"+amountDict.getString("payables");
                int startIndex = 4;
                int endIndex = payablesFmt.length();
                SpannableString payablesSpanStr = new SpannableString(payablesFmt);
                payablesSpanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
                feeAmountTxtv.setText(payablesSpanStr);
            }

            if(userinfoJson == null)return;
            //积分
            if( userinfoJson.has("pay_points")){
                int p = userinfoJson.getInt("pay_points");
                String pointRate = SPServerUtils.getPointRate();
                pointTxtv.setText("当前可用积分"+p+"("+pointRate+"积分抵扣1元)");
            }

            //金额
            if(userinfoJson.has("user_money")){
                double b = userinfoJson.getDouble("user_money");
                balanceTxtv.setText("当前可用余额¥"+b);
            }

            //下单时间
            String buyTime = SPUtils.convertFullTimeFromPhpTime(System.currentTimeMillis());
            buyTimeTxtv.setText("下单时间:"+buyTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/**
 *  更加当前设置信息, 获取商品数据(商品总价, 应付金额)
 */
   public void loadTotalFee(){

       RequestParams params = getRequestParameter(1);
       SPShopRequest.getOrderTotalFee(params, new SPSuccessListener() {
           @Override
           public void onRespone(String msg, Object response) {

               if (response!=null){
                   amountDict = (JSONObject)response;
                   refreshView();
                   refreshTotalFee();
               }
           }
       }, new SPFailuredListener() {
           @Override
           public void onRespone(String msg, int errorCode) {
               showToast(msg);
           }
       });
    }


    /**
     *  获取请求参数
     *  @param type 请求类型, 1: 价格变动, 2: 生成订单
     *
     *  @return
     */
    public RequestParams getRequestParameter(int type){

        RequestParams params = new RequestParams();

        try {
            if (type == 1) {
                //价格变动
                params.put("act" , "order_price");
            }else{
                //提交订单
                params.put("act", "submit_order");
                if (!SPStringUtils.isEmpty(this.invoice)){
                    params.put("invoice_title", this.invoice);
                }
            }

            if (consigneeAddress != null) {
                params.put("address_id" , consigneeAddress.getAddressID());
            }

            //物流码
            if (selectedDeliverJson != null) {
                String deliverCode = selectedDeliverJson.getString("code");
                params.put("shipping_code" , deliverCode);
            }

            //使用积分
            params.put("pay_points" , points);

            //使用余额
            params.put("user_money" , balance);

            //优惠码
            if (selectedCoupon == null){
                return params;
            }
            if (selectedCoupon.getCouponType() == 1) {
                params.put("couponTypeSelect","1");//代金券(1:选择框, 2:输入框)
                params.put("coupon_id",selectedCoupon.getCouponID());
            }else{
                params.put("couponTypeSelect","2");//代金券(1:选择框, 2:输入框)
                params.put("couponCode", selectedCoupon.getCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     *  更新底部商品总金额数据
     */
    public void refreshTotalFee(){

        try {

            String payables  = "0.00";
            if(amountDict!=null && amountDict.has("payables")) {
                payables = amountDict.getString("payables");
            }else{
                payables = "0";
            }

            String totalFeeFmt = "应付金额¥"+payables ;
            int startIndex = 4;
            int endIndex = totalFeeFmt.length() ;
            SpannableString totalFeeSpanStr = new SpannableString(totalFeeFmt);
            totalFeeSpanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
            payfeeTxtv.setText(totalFeeSpanStr);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {
        refreshData();
    }

    @Override
    public void initEvent() {
        bananceSth.setOnChangeListener(new SwitchButton.OnChangeListener() {
            @Override
            public void onChange(SwitchButton sb, boolean state) {
                try {
                    //余额
                    if (userinfoJson != null && state && userinfoJson.has("user_money")) {
                        balance = Double.valueOf(userinfoJson.getDouble("user_money")).floatValue();
                    } else {
                        balance = 0;
                    }
                    //重新计算支付金额
                    loadTotalFee();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        pointSth.setOnChangeListener(new SwitchButton.OnChangeListener() {

            @Override
            public void onChange(SwitchButton sb, boolean state) {
                try {
                    //积分
                    if (userinfoJson != null && state && userinfoJson.has("pay_points")) {
                        points = userinfoJson.getInt("pay_points");
                    } else {
                        points = 0;
                    }
                    //重新计算支付金额
                    loadTotalFee();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Click({R.id.order_deliver_aview , R.id.order_coupon_aview , R.id.order_invoce_aview , R.id.pay_btn , R.id.order_confirm_consignee_layout , R.id.order_product_flayout})
    public void onButtonClick(View v){

        switch (v.getId()){
            case R.id.order_confirm_consignee_layout:
                Intent resultIntent = new Intent(SPConfirmOrderActivity.this , SPConsigneeAddressListActivity_.class);
                resultIntent.putExtra("getAddress" , "1");
                startActivityForResult(resultIntent , SPMobileConstants.Result_Code_GetAddress);
                break;
            case R.id.order_deliver_aview:

                SPMobileApplication.getInstance().jsonArray = deliverArray;
                SPMobileApplication.getInstance().json = selectedDeliverJson;
                Intent logisticIntent = new Intent(this , SPChooseLogisticActivity_.class);
                startActivityForResult(logisticIntent , 1);

                break;
            case R.id.order_coupon_aview:
                SPMobileApplication.getInstance().list = coupons;
                Intent couponIntent = new Intent(this , SPAvailableCouponActivity_.class);
                couponIntent.putExtra("coupon",selectedCoupon);
                startActivityForResult(couponIntent, 2);
                break;
            case R.id.order_invoce_aview:
                Intent invokeIntent = new Intent(this , SPTextFieldViewActivity_.class);
                invokeIntent.putExtra("placeHolder" , "请输入发票抬头");
                invokeIntent.putExtra("validate" , "例如:办公用品");
                startActivityForResult(invokeIntent, SPMobileConstants.Result_Code_GetValue);
                break;
            case R.id.pay_btn:
                /*Intent completedIntent = new Intent(this, SPPayCompletedActivity_.class);
                completedIntent.putExtra("tradeFee" , "9809");
                completedIntent.putExtra("tradeNo" , "2016630142355");
                startActivity(completedIntent);*/
                orderCommint();
                break;
            case R.id.order_product_flayout:
                Intent showIntent = new Intent(SPConfirmOrderActivity.this , SPProductShowListActivity_.class);
                SPMobileApplication.getInstance().list = products;
                startActivity(showIntent);
                break;
        }
    }


/**
 *  提交订单
 */
  public void orderCommint(){

        if (consigneeAddress == null) {
            showToast("请选择收货地址!");
            return;
        }

        //提交订单
        RequestParams params = getRequestParameter(2);
        //提交订单
        showLoadingToast("正在提交订单");
        SPShopRequest.submitOrder(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                SPShopCartManager.getInstance(SPConfirmOrderActivity.this).reloadCart();
                hideLoadingToast();

                String orderID = (String) response;
                startUpPay(orderID);
            }
        }, new SPFailuredListener(SPConfirmOrderActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {

                hideLoadingToast();
                showToast(msg);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode){
            case 1:
                selectedDeliverJson = SPMobileApplication.getInstance().json;
                break;
            case 2:
                //优惠券
               selectedCoupon = (SPCoupon)data.getSerializableExtra("selectCoupon");
                break;
            case SPMobileConstants.Result_Code_GetValue:
                invoice = data.getStringExtra("value");
                invoceAview.setSubText(invoice);
                break;
            case SPMobileConstants.Result_Code_GetAddress:
                consigneeAddress  = (SPConsigneeAddress)data.getSerializableExtra("consignee");
                refreshView();
                break;
        }
        loadTotalFee();
    }

    /**
     *  启动支付页面支付
     *
     *  @param orderID order description
     */
    public void startUpPay(String orderID){

        //进入支付页面支付
        SPPersonRequest.getOrderDetail(orderID, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if(response!=null){
                    SPOrder order  = (SPOrder)response;
                    gotoPay(order);
                }
            }
        }, new SPFailuredListener(SPConfirmOrderActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }

    /**
     *  进入支付页面支付
     *
     *  @param order order description
     */
    public void gotoPay(SPOrder order){

        //余额或积分支付完成, 进入订单页, 否则进入支付页
        if (Integer.valueOf(order.getPayStatus())== 1) {
            //NSLog(@" gotoPayWithOrder 支付完成, 进入订单页... ");
        }else{
            Intent payIntent = new Intent(this , SPPayListActivity_.class);
            payIntent.putExtra("order" , order);
            startActivity(payIntent);
        }
    }

}
