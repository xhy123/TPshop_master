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
 * Description:订单详情
 * @version V1.0
 */
package com.soubao.tpshop.activity.person.order;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.common.SPOrderBaseActivity;
import com.soubao.tpshop.activity.shop.SPProductShowListActivity_;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.dao.SPPersonDao;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.person.SPPersonRequest;
import com.soubao.tpshop.model.OrderButtonModel;
import com.soubao.tpshop.model.SPProduct;
import com.soubao.tpshop.model.person.SPConsigneeAddress;
import com.soubao.tpshop.model.order.SPOrder;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPConfirmDialog;
import com.soubao.tpshop.utils.SPOrderUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.soubao.tpshop.utils.SPUtils;
import com.soubao.tpshop.view.SPArrowRowView;
import com.ta.utdid2.android.utils.StringUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.order_details)
public class SPOrderDetailActivity extends SPOrderBaseActivity implements SPConfirmDialog.ConfirmDialogListener {

    private String TAG = "SPOrderDetailActivity";

    @ViewById(R.id.order_consignee_txtv)
    TextView consigneeTxtv;

    @ViewById(R.id.order_address_txtv)
    TextView addressTxtv;

    @ViewById(R.id.order_product_scrollv)
    HorizontalScrollView mScroll;

    @ViewById(R.id.product_list_gallery_lyaout)
    LinearLayout mGallery;

    //底部操作按钮View
    @ViewById(R.id.order_button_gallery_lyaout)
    LinearLayout mButtonGallery;


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

    @ViewById(R.id.order_ordersn_txtv)
    TextView orderSnTxtv;

    @ViewById(R.id.order_status_txtv)
    TextView orderStatusTxtv;

    @ViewById(R.id.order_address_arrow_imgv)
    ImageView orderAddressArrowImgv;

    @ViewById(R.id.order_product_count_txtv)
    TextView orderPorductCountTxtv;

    @ViewById(R.id.buy_time_txtv)
    TextView buyTimeTxtv;


    /********* 服务器拉取的数据源集合 *****************/
    SPConsigneeAddress consigneeAddress ;   //当前收货人信息
    List<SPProduct> mProducts;               //商品列表

    /********* 选中的数据 *****************/
    int points;                             //使用的积分
    float balance;                          //使用的余额
    String invoice;                         //发票
    String mOrderId;                        //订单编号
    SPOrder mOrder;                         //订单
    String detailAddress;                   //收货地址详情

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true , true , getString(R.string.title_detail));
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init(){
        super.init();
        if (getIntent()==null || getIntent().getStringExtra("orderId")==null){
            showToast(getString(R.string.toast_no_ordersn_data));
            this.finish();
            return;
        }
        mOrderId = getIntent().getStringExtra("orderId");
        refreshData();
    }

    public void refreshData(){

         showLoadingToast();
         SPPersonRequest.getOrderDetailByID(mOrderId, new SPSuccessListener() {
             @Override
             public void onRespone(String msg, Object response) {
                 mOrder = (SPOrder) response;
                 try {
                     if (mOrder != null) {
                         dealModel();
                         refreshView();
                         //load 商品金额信息
                     }
                 } catch (Exception e) {
                     showToast(e.getMessage());
                 }
                 hideLoadingToast();
             }
         }, new SPFailuredListener(SPOrderDetailActivity.this) {
             @Override
             public void onRespone(String msg, int errorCode) {
                 hideLoadingToast();
                 showToast(msg);
             }
         });
    }

    /**
     *  处理服务器获取的数据
     */
    public void dealModel(){
        try {
             List<SPProduct> products = mOrder.getProducts();
             if (products!=null &&products.size() > 0){
                 for (SPProduct product : products){
                     product.setReturnBtn(mOrder.getReturnBtn());
                     product.setOrderStatusCode(mOrder.getOrderStatusCode());
                     product.setOrderID(mOrder.getOrderID());
                     product.setOrderSN(mOrder.getOrderSN());
                 }
             }
            mProducts = mOrder.getProducts();
            String firstAddress = SPPersonDao.getInstance(this).queryFirstRegion(mOrder.getProvince() , mOrder.getCity() , mOrder.getDistrict(), mOrder.getTown());
            detailAddress = firstAddress + mOrder.getAddress();

            //设置商品图片
            buildProductGallery();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新页面数据
     */
    public void refreshView(){
        try {

            consigneeTxtv.setText(mOrder.getConsignee() +"  "+ mOrder.getMobile());
            addressTxtv.setText(detailAddress);
            orderSnTxtv.setText("订单编号:"+mOrder.getOrderSN());
            mProducts = mOrder.getProducts();
            if (mProducts != null && mProducts.size() > 0 ){
                productCountTxtv.setText("共" + mProducts.size() + "件商品");
            }

            if(!SPStringUtils.isEmpty(mOrder.getOrderStatusDesc())){
                orderStatusTxtv.setText(mOrder.getOrderStatusDesc());
            }

            deliverAview.setSubText(mOrder.getShippingName());

            //订单支付信息
            feeGoodsFeeTxtv.setText("¥" +mOrder.getGoodsPrice());
            feeShoppingTxtv.setText("¥"+mOrder.getShippingPrice());
            feeCouponTxtv.setText("¥" + mOrder.getCouponPrice());
            feePointTxtv.setText("¥" + mOrder.getIntegralMoney());
            feeBalanceTxtv.setText("¥"+mOrder.getUserMoney());

            if (!SPStringUtils.isEmpty(mOrder.getOrderAmount())){
                String payablesFmt = "实付款:¥"+mOrder.getOrderAmount();
                int startIndex = 4;
                int endIndex = payablesFmt.length();
                SpannableString payablesSpanStr = new SpannableString(payablesFmt);
                payablesSpanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
                feeAmountTxtv.setText(payablesSpanStr);
            }

            if (!SPStringUtils.isEmpty(mOrder.getAddTime())){
                //下单时间
                String buyTime = SPUtils.convertFullTimeFromPhpTime(Long.valueOf(mOrder.getAddTime()));
                buyTimeTxtv.setText("下单时间:"+buyTime);
            }

            List<OrderButtonModel> buttonModels = getOrderButtonModelByOrder(mOrder);
            buildProductButtonGallery(mButtonGallery, buttonModels);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initSubViews() {
        orderAddressArrowImgv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void initData() {
        GestureDetector.OnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return super.onSingleTapConfirmed(e);
            }
        };

        /*final GestureDetector gestureDetector = new GestureDetector(this, listener);
        mScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return gestureDetector.onTouchEvent(event);
            }
        });*/
    }

    @Override
    public void initEvent() {

        orderPorductCountTxtv.setOnClickListener(orderButtonClickListener);
    }

    @Click({R.id.order_product_scrollv})
    public void onViewClick(View v){
        switch (v.getId()){
            case R.id.order_button_scrollv:
                break;
        }

    }

    private void buildProductGallery(){

        if (mProducts!=null){
            for (int i = 0; i < mProducts.size(); i++){
                String url = mProducts.get(i).getImageThumlUrl();
                View view = LayoutInflater.from(this).inflate(R.layout.activity_index_gallery_item, mGallery, false);
                ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
                view.setOnClickListener(orderButtonClickListener);
                Glide.with(this).load(url).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);
                mGallery.addView(view);
            }
        }
    }

    private void buildProductButtonGallery(LinearLayout gallery , List<OrderButtonModel> buttonModels){
        gallery.removeAllViews();
        if (buttonModels !=null && buttonModels.size() > 0){
            for (int i = 0; i < buttonModels.size(); i++){
                OrderButtonModel buttonModel = buttonModels.get(i);
                View view = LayoutInflater.from(this).inflate(R.layout.order_button_gallery_item, gallery, false);
                Button button = (Button) view.findViewById(R.id.id_index_gallery_item_button);
                button.setText(buttonModel.getText());
                button.setTag(buttonModel.getTag());

                if (buttonModel.isLight()){
                    button.setBackgroundResource(R.drawable.tag_button_bg_checked);
                }else{
                    button.setBackgroundResource(R.drawable.tag_button_bg_unchecked);
                }
                button.setOnClickListener(orderButtonClickListener);

                gallery.addView(view);
            }
        }else{
            View view = LayoutInflater.from(this).inflate(R.layout.order_button_gallery_item, gallery, false);
            Button button = (Button) view.findViewById(R.id.id_index_gallery_item_button);
            button.setText("再次购买");
            button.setTag(SPMobileConstants.tagBuyAgain);
            button.setBackgroundResource(R.drawable.tag_button_bg_unchecked);
            button.setOnClickListener(orderButtonClickListener);
            gallery.addView(view);
        }
    }

    public List<OrderButtonModel> getOrderButtonModelByOrder(SPOrder order) {

        List<OrderButtonModel> buttons = new ArrayList<OrderButtonModel>();
        if (order.getPayBtn() == 1) {//显示支付按钮
            OrderButtonModel payModel = new OrderButtonModel("支付", SPMobileConstants.tagPay, true);
            buttons.add(payModel);
        }

        if (order.getCancelBtn() == 1) {//取消订单按钮
            OrderButtonModel cancelModel = new OrderButtonModel("取消订单", SPMobileConstants.tagCancel, false);
            buttons.add(cancelModel);
        }

        if (order.getReceiveBtn() == 1) {//确认收货按钮
            OrderButtonModel cancelModel = new OrderButtonModel("确认收货", SPMobileConstants.tagReceive, true);
            buttons.add(cancelModel);
        }

        if (order.getShippingBtn() == 1) {//查看物流按钮
            OrderButtonModel cancelModel = new OrderButtonModel("查看物流", SPMobileConstants.tagShopping, true);
            buttons.add(cancelModel);
        }
        return buttons;
    }

    public View.OnClickListener orderButtonClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.id_index_gallery_item_rlayout || v.getId() == R.id.order_product_count_txtv){
                Intent showIntent = new Intent(SPOrderDetailActivity.this , SPProductShowListActivity_.class);
                SPMobileApplication.getInstance().list = mOrder.getProducts();
                startActivity(showIntent);
            }else if(v.getId() == R.id.id_index_gallery_item_button){
                int tag = Integer.valueOf(v.getTag().toString());
                switch (tag){
                    case SPMobileConstants.tagPay:
                        gotoPay(mOrder);
                        break;
                    case SPMobileConstants.tagCancel:
                        cancelOrder(mOrder);
                        break;
                    case SPMobileConstants.tagCustomer:
                        connectCustomer();
                        break;
                    case SPMobileConstants.tagShopping:
                        lookShopping(mOrder);
                        break;
                    case SPMobileConstants.tagReceive:
                        confirmReceive(mOrder);
                        break;
                    case SPMobileConstants.tagReturn:
                        break;
                    case SPMobileConstants.tagBuyAgain:
                        gotoProductDetail(mOrder.getOrderID());
                        break;
                    case SPMobileConstants.MSG_CODE_ORDER_LIST_ITEM_ACTION:
                        Intent detailIntent = new Intent(SPOrderDetailActivity.this  , SPOrderDetailActivity_.class);
                        detailIntent.putExtra("orderId", mOrder.getOrderID());
                        startActivity(detailIntent);
                        break;
                }
            }

        }
    };

    /**
     * 取消订单
     * @param order
     */
    public void cancelOrder(SPOrder order){
        showConfirmDialog("确定取消订单?", "订单提醒", this, SPMobileConstants.tagCancel);

    }

    /**
     * 确认收货
     * @param order
     */
    public void confirmReceive(SPOrder order){

        showLoadingToast("正在操作");
        confirmOrderWithOrderID(order.getOrderID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                showToast(msg);
                refreshData();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }

    @Override
    public void clickOk(int actionType) {
        //用户点击了确定
        if (actionType == SPMobileConstants.tagCancel){

            showLoadingToast("正在操作");
            cancelOrder(mOrder.getOrderID(), new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingToast();
                    showToast(msg);
                    refreshData();
                }
            }, new SPFailuredListener() {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingToast();
                    showToast(msg);
                }
            });
        }
    }
}
