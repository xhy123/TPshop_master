package com.soubao.tpshop.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.R;
import com.soubao.tpshop.SPMainActivity;
import com.soubao.tpshop.activity.common.SPBaseActivity;
import com.soubao.tpshop.adapter.SPProductSpecListAdapter;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.global.SPShopCartManager;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.model.SPProduct;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPShopUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.soubao.tpshop.view.tagview.Tag;
import com.soubao.tpshop.view.tagview.TagListView;
import com.soubao.tpshop.view.tagview.TagView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by admin on 2016/6/23.
 */
@EActivity(R.layout.product_details_spec)
public class SPProductDetaiSpeclActivity extends SPBaseActivity implements TagListView.OnTagClickListener {

    private String TAG = "SPProductDetaiSpeclActivity";

    @ViewById(R.id.product_pic_imgv)
    ImageView picImgv;

    @ViewById(R.id.product_name_txtv)
    TextView nameTxtv;

    @ViewById(R.id.product_price_txtv)
    TextView priceTxtv;

    @ViewById(R.id.cart_minus_btn)
    Button minusBtn;

    @ViewById(R.id.cart_count_dtxtv)
    EditText cartCountEtxtv;

    @ViewById(R.id.cart_plus_btn)
    Button plusBtn;

    @ViewById(R.id.product_spec_lstv)
    ListView specListv;


    @ViewById(R.id.product_spec_cart_layout)
    View cartView;

    @ViewById(R.id.product_spec_store_count_txtv)
    TextView storeCountTxtv;


    int mCartCount;
    SPProduct mProduct;
    SPProductSpecListAdapter specAdapter;

    private String currShopPrice ;
    private Map<String , String> selectSpecMap;//保存选择的规格ID
    JSONObject priceJson;
    JSONObject specJson;

    @Override
    protected void onCreate(Bundle bundle) {
        setCustomerTitle(true, true , getString(R.string.title_product_spec));
        super.onCreate(bundle);

        if(getIntent() == null){
            showToast(getString(R.string.data_error));
            this.finish();
            return;
        }

        Bundle extras =  getIntent().getExtras();
        mProduct = (SPProduct)extras.getSerializable("product");
        currShopPrice = extras.getString("currShopPrice");
        priceJson = SPMobileApplication.getInstance().json;
        specJson = SPMobileApplication.getInstance().json1;
        selectSpecMap = SPMobileApplication.getInstance().map;

    }

    @AfterViews
    public void  init(){
        super.init();
    }

    @Click({R.id.cart_minus_btn , R.id.cart_plus_btn , R.id.add_cart_btn , R.id.buy_btn })
    public void onButtonClick(final View v){
        mCartCount = Integer.valueOf(cartCountEtxtv.getText().toString().trim());
        if(v.getId() == R.id.cart_minus_btn || v.getId() == R.id.cart_plus_btn){
            if(v.getId() == R.id.cart_minus_btn){
                if (mCartCount<=1){
                    showToast(getString(R.string.toast_count_not_small_zero));
                    return;
                }
                mCartCount--;
            }else{
                int storeCount = SPShopUtils.getShopStoreCount(priceJson, selectSpecMap.values());
                if (mCartCount > storeCount){
                    showToast(getString(R.string.toast_low_stocks));
                    return ;
                }
                mCartCount++;
            }
            cartCountEtxtv.setText(String.valueOf(mCartCount));
        }else if(v.getId() == R.id.add_cart_btn || v.getId() == R.id.buy_btn){
            //加入购物车
            Integer count = Integer.valueOf(cartCountEtxtv.getText().toString().trim());
            if (count < 1) {
                showToast(getString(R.string.toast_not_datal));
                return;
            }

            String specs = null;
            if(selectSpecMap.values().size()>0){
                specs = SPStringUtils.collectToString(selectSpecMap.values(), ",");
            }
            SPShopCartManager.getInstance(this).shopCartGoodsOperation(mProduct.getGoodsID(), specs, count, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {

                    showToast(getString(R.string.toast_shopcart_action_success));

                    if(v.getId() ==  R.id.buy_btn){
                       gotoShopcart();
                    }

                }
            }, new SPFailuredListener(SPProductDetaiSpeclActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    showToast(msg);
                }
            });
        }
    }

    public void gotoShopcart(){

        //进入购物车
        Intent shopcartIntetn = new Intent(this , SPMainActivity.class);
        shopcartIntetn.putExtra(SPMainActivity.SELECT_INDEX , SPMainActivity.INDEX_SHOPCART);
        startActivity(shopcartIntetn);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

    //获取listview的适配器
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        float cartHeight = SPCommonUtils.dip2px(this, 50);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)listView.getLayoutParams();
        int count = listAdapter.getCount()-1;
        params.height = totalHeight + (listView.getDividerHeight() * count) + Float.valueOf(cartHeight).intValue() ;
        ((FrameLayout.LayoutParams)params).setMargins(10, 10, 10, 10);
        listView.setLayoutParams(params);

    }

    @Override
    public void initSubViews() {
        specAdapter = new SPProductSpecListAdapter(this , this , selectSpecMap.values());
        specListv.setAdapter(specAdapter);

        if (!SPStringUtils.isEmpty(mProduct.getGoodsID())){
            String imgUrl2 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, mProduct.getGoodsID());

            Glide.with(this).load(imgUrl2).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(picImgv);
        }
        if (!SPStringUtils.isEmpty(mProduct.getGoodsName())){
            nameTxtv.setText(mProduct.getGoodsName());
        }
        specAdapter.setData(specJson);
        specAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(specListv);
    }

    @Override
    public void initData() {
        mCartCount = 0;
        refreshPrice();
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onTagClick(TagView tagView, Tag tag) {

        selectSpecMap.put(tag.getKey(), tag.getValue());

        String title = tag.getTitle();
        refreshPrice();

        SPProductDetailActivity detailActivity = SPProductDetailActivity.getInstance();
        if (detailActivity != null){
            detailActivity.updateSelectSpec(selectSpecMap);
        }
    }

    private void refreshPrice(){
        if(priceJson==null || selectSpecMap == null || selectSpecMap.size() < 1){
            priceTxtv.setText("¥" + mProduct.getShopPrice());
        }else{

            String price = SPShopUtils.getShopprice(priceJson, selectSpecMap.values());
            if (!SPStringUtils.isEmpty(price)){
                priceTxtv.setText("¥"+price);
            }

            //刷新库存数据
            int storeCount = SPShopUtils.getShopStoreCount(priceJson, selectSpecMap.values());
            String residue = "数量(剩余"+storeCount+"件)";
            int startIndex = 2;
            int endIndex = residue.length();
            SpannableString residueSpanStr = new SpannableString(residue);
            residueSpanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
            residueSpanStr.setSpan(new RelativeSizeSpan(0.8f), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            storeCountTxtv.setText(residueSpanStr);
        }

    }
}
