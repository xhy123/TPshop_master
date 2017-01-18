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
 * Description:商品详情
 * @version V1.0
 */
package com.soubao.tpshop.activity.shop;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.R;
import com.soubao.tpshop.SPMainActivity;
import com.soubao.tpshop.adapter.SPProductAttrListAdapter;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.activity.common.SPBaseActivity;
import com.soubao.tpshop.global.SPShopCartManager;

import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.condition.SPProductCondition;
import com.soubao.tpshop.http.person.SPPersonRequest;
import com.soubao.tpshop.http.shop.SPShopRequest;
import com.soubao.tpshop.model.SPProduct;
import com.soubao.tpshop.model.shop.SPCollect;
import com.soubao.tpshop.model.shop.SPGoodsComment;
import com.soubao.tpshop.model.shop.SPProductSpec;
import com.soubao.tpshop.utils.SPDialogUtils;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPShopUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.soubao.tpshop.view.SPArrowRowView;
import com.soubao.tpshop.view.SPMobileScrollLayout;
import com.soubao.tpshop.view.SPPageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.next.slidebottompanel.SlideBottomPanel;

/**
 * @author 飞龙
 *
 */
@EActivity(R.layout.product_detail_font)
public class SPProductDetailActivity extends SPBaseActivity implements SPPageView.PageListener {

	private String TAG = "SPProductDetailActivity";

	@ViewById(R.id.banner_slayout)
	SPPageView mScroll; // SPMobileScrollLayout mScroll ;

	@ViewById(R.id.banner_lyaout)
	LinearLayout mGallery;

	@ViewById(R.id.pageindex_txtv)
	TextView pageindexTxtv ;	//图片索引提示

	@ViewById(R.id.details_name_txtv)
	TextView nameTxtv ;			//产品名称

	@ViewById(R.id.details_keywords_txtv)
	TextView keywordsTxtv ;		//关键字描述

	@ViewById(R.id.details_orignal_price_txtv)
	TextView orignalPriceTxtv ;	//原价

	@ViewById(R.id.details_now_price_txtv)
	TextView nowPriceTxtv ;		//现价

	@ViewById(R.id.product_spec_aview)
	SPArrowRowView specAview;	//已选规格

	@ViewById(R.id.product_attr_aview)
	SPArrowRowView attrARView;	//商品属性

	@ViewById(R.id.product_comment_aview)
	SPArrowRowView commentARView;	//商品评论

	@ViewById(R.id.product_detail_aview)
	SPArrowRowView detailARView;//详情

	@ViewById(R.id.product_like_imgv)
	ImageView likeImgv;//收藏图标

	@ViewById(R.id.product_like_txtv)
	TextView likeTxtv;//收藏文本


	@ViewById(R.id.productcart_count)
	TextView cartCountTxtv;//购物车数量

	@ViewById(R.id.product_cart_btn)
	Button cartBtn;//加入购物车

	@ViewById(R.id.sbv)
	SlideBottomPanel sbottomPanel;

	@ViewById(R.id.product_attr_lstv)
	ListView attrLstv;


	private String mGoodsID;

	private int gallerySize;//预览图数量
	private int galleryIndex;//预览图大小
	private int mCommentCount;//商品评论数量

	//全部数据
	private SPProduct mProduct ;
	private JSONArray mGalleryArray ;
	private JSONObject priceJson;	//规格属性对于的价格
	private List<SPGoodsComment> mComments;
	ShopCartChangeReceiver mShopCartChangeReceiver;
	SPProductAttrListAdapter mAttrListAdapter;
	private JSONObject specJson;//Map<String , List<SPProductSpec>>
	private Map<String , String> selectSpecMap;//保存选择的规格ID
	private String currShopPrice;//当前商品价格

	private static SPProductDetailActivity instance;

	@Override
	protected void onCreate(Bundle bundle) {
		setCustomerTitle(true, true, getString(R.string.title_product_detail));
		super.onCreate(bundle);
		Intent intent = getIntent();
		if(intent != null){
		   mGoodsID = intent.getStringExtra("goodsID");
		}
//TabPageIndicator
		
		//监听购物车数据变化广播
		IntentFilter filter = new IntentFilter(SPMobileConstants.ACTION_SHOPCART_CHNAGE);
		this.registerReceiver(mShopCartChangeReceiver = new ShopCartChangeReceiver(), filter);

		instance = this;

	}

	public static SPProductDetailActivity getInstance(){
		return instance;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(mShopCartChangeReceiver);
	}

	@AfterViews
	public void  init(){
		super.init();

		//隐藏 SlideBottomPanel
		if (sbottomPanel.isPanelShowing()) {
			sbottomPanel.hide();
		}
		selectSpecMap = new HashMap<String,String>();
		mAttrListAdapter = new SPProductAttrListAdapter(this);
		attrLstv.setAdapter(mAttrListAdapter);
		loadCartCount();
		refreshCollectDatta();
	}

	@Override
	public void initSubViews() {
		orignalPriceTxtv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
	}

	@Override
	public void initData() {
		getProductDetails();
		refreshCollectButton();
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshCollectButton();
	}

	@Override
	public void initEvent() {
		mScroll.setPageListener(this);
	}
	
	public void getProductDetails(){

		/** 此处参数-1 : 意味着返回的是左边分类  */
		SPProductCondition condition = new SPProductCondition();
		if (mGoodsID==null){
			condition.goodsID = -1;
		}else {
			condition.goodsID = Integer.valueOf(mGoodsID);
		}

		showLoadingToast();
		SPShopRequest.getProductByID(condition, new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {
				hideLoadingToast();
				try {
					mDataJson = (JSONObject) response;
					if (mDataJson != null && mDataJson.has("product")) {
						mProduct = (SPProduct) mDataJson.get("product");
					}

					if (mDataJson != null && mDataJson.has("gallery")) {
						mGalleryArray = mDataJson.getJSONArray("gallery");
					}

					if (mDataJson != null && mDataJson.has("price")) {
						priceJson = mDataJson.getJSONObject("price");
					}

					if (mDataJson != null && mDataJson.has("comments")) {
						mComments = (List<SPGoodsComment>) mDataJson.get("comments");
						mCommentCount = mComments.size();
					}
					dealModel();
					if (mProduct.getAttrArr() != null) {
						mAttrListAdapter.setData(mProduct.getAttrArr());
						mAttrListAdapter.notifyDataSetChanged();
					}
				} catch (Exception e) {
					showToast(e.getMessage());
				}
				onDataLoadFinish();
			}
		}, new SPFailuredListener() {
			@Override
			public void onRespone(String msg, int errorCode) {
				hideLoadingToast();
				SMobileLog.e(TAG, "onRespone ,msg : " + msg);
				SPDialogUtils.showToast(SPProductDetailActivity.this, msg);
			}
		});
	}

	@Click({R.id.like_lyaout , R.id.product_cart_rlayout , R.id.product_cart_btn , R.id.product_spec_aview, R.id.product_attr_aview, R.id.product_detail_aview, R.id.product_comment_aview})
	public void onButtonClick(View v) {
		switch (v.getId()){
			case R.id.like_lyaout:
				if (!SPMobileApplication.getInstance().isLogined){
					showToastUnLogin();
					toLoginPage();
					return;
				}

				String type =  "1" ;
				if (SPShopUtils.isCollected(mGoodsID)){//收藏 -> 取消收藏
					showLoadingToast("正在取消收藏");
					type =  "1" ;
				}else{
					showLoadingToast("正在添加收藏");
					type =  "0" ;
				}

				SPPersonRequest.collectOrCancelGoodsWithID(mGoodsID, type, new SPSuccessListener() {
					@Override
					public void onRespone(String msg, Object response) {
						refreshCollectDatta();
						hideLoadingToast();
						showToast(msg);
					}
				}, new SPFailuredListener(SPProductDetailActivity.this) {
					@Override
					public void onRespone(String msg, int errorCode) {
						hideLoadingToast();
						showToast(msg);
					}
				});
				break;
			case R.id.product_cart_rlayout:
				Intent shopcartIntetn = new Intent(this , SPMainActivity.class);
				shopcartIntetn.putExtra(SPMainActivity.SELECT_INDEX , SPMainActivity.INDEX_SHOPCART);
				startActivity(shopcartIntetn);
				break;
			case R.id.product_cart_btn:
			case R.id.product_spec_aview:
				if (mProduct==null){
					showToast(getString(R.string.data_error));
					return;
				}
				Intent specIntent = new Intent(this , SPProductDetaiSpeclActivity_.class);
				specIntent.putExtra("currShopPrice", currShopPrice);//商品价格
				specIntent.putExtra("product", mProduct);
				SPMobileApplication.getInstance().json = priceJson;
				SPMobileApplication.getInstance().json1 = specJson;
				SPMobileApplication.getInstance().map = selectSpecMap;
				startActivity(specIntent);
				break;
			case R.id.product_attr_aview:
				//显示 SlideBottomPanel
				sbottomPanel.displayPanel();
				break;
			case R.id.product_detail_aview:
				startupDetailActivity(0);
				break;
			case R.id.product_comment_aview:
				startupDetailActivity(1);
				break;

		}
	}


	public void startupDetailActivity(int position){
		Intent detailIntent = new Intent(this, SPProductDetailInnerActivity_.class);
		detailIntent.putExtra("goodsId" , mProduct.getGoodsID());
		detailIntent.putExtra("content", mProduct.getGoodsContent());
		detailIntent.putExtra("position", position);
		startActivity(detailIntent);
	}

	public void onDataLoadFinish(){

		refreshGalleryViewData();

		String commentTxt = getString(R.string.product_details_comment) +"("+mCommentCount+")";
		commentARView.setText(commentTxt);

		if(mProduct != null){
			this.nameTxtv.setText(mProduct.getGoodsName());
			this.orignalPriceTxtv.setText("价格：￥"+mProduct.getMarketPrice());
			this.nowPriceTxtv.setText("¥"+mProduct.getShopPrice());
			this.commentARView.setText("累计评价("+mProduct.getCommentCount()+")");
		}
	}

	@Override
	public void page(int page) {
		galleryIndex = page;
		refreshGalleryViewData();
	}

	@Override
	public void dealModel() {
		super.dealModel();
		List<String> gallerys = new ArrayList<String>();
		if (mGalleryArray!= null){
			try {
				for (int i=0; i<mGalleryArray.length(); i++){
					JSONObject jsonObject = (JSONObject)mGalleryArray.getJSONObject(i);
					String url = jsonObject.getString("image_url");
					gallerys.add(url);;
					if (mScroll!=null){
						//mScroll.setDataSource(this , gallerys);
						buildProductGallery(gallerys);
						gallerySize = gallerys.size();
						galleryIndex = 0;
						refreshGalleryViewData();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		dealProductSpec();

	}


	private void refreshGalleryViewData(){

		String tIndex = (galleryIndex+1)+"/"+gallerySize;
		this.pageindexTxtv.setText(tIndex);

	}

	/**
	 * 刷新收藏按钮
	 */
	public void refreshCollectButton(){

		if (SPShopUtils.isCollected(mGoodsID)) {
			//收藏
			likeImgv.setImageResource(R.drawable.product_like);
			likeTxtv.setText(getString(R.string.product_details_like));
		}else{
			//未收藏
			likeImgv.setImageResource(R.drawable.product_unlike);
			likeTxtv.setText(getString(R.string.product_details_unlike));
		}
	}

	/**
	 * 刷新收藏数据
	 */
	public void refreshCollectDatta(){

		if (SPMobileApplication.getInstance().isLogined) {
			SPPersonRequest.getGoodsCollectWithSuccess(new SPSuccessListener () {
				@Override
				public void onRespone(String msg, Object response) {
					SPMobileApplication.getInstance().collects = (List<SPCollect>) response;
					refreshCollectButton();
				}
			}, new SPFailuredListener() {
				@Override
				public void onRespone(String msg, int errorCode) {
					//showToast(msg);
				}
			});
		}else{
			SPMobileApplication.getInstance().collects = null;
		}
	}

	/**
	 * 刷新购物车数据
	 */
	public void loadCartCount(){
		SPShopCartManager shopCartManager = SPShopCartManager.getInstance(this);
		int shopCount = shopCartManager.getShopCount();
		if (shopCount <= 0 ){
			SPShopCartManager.getInstance(this).reloadCart();
			cartCountTxtv.setVisibility(View.INVISIBLE);
		}else{
			cartCountTxtv.setVisibility(View.INVISIBLE);
			cartCountTxtv.setText(String.valueOf(shopCount));
		}

	}

	class  ShopCartChangeReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(SPMobileConstants.ACTION_SHOPCART_CHNAGE)){
				loadCartCount();
			}
		}
	}

	/**
	 *  处理商品规格
	 *  1. 将每个商品根据 specName 为key 分组
	 *  2. 把每组规格中的第一个规格设置为默认规格
	 *  3. 根据默规格, 查询商品当前的价格
	 */
	public void dealProductSpec(){
		specJson = new JSONObject();//清理之前的缓存数据
		try {
			if (mProduct!=null) {
				if (mProduct.getSpecArr() !=null && mProduct.getSpecArr().size() > 0) {
					//排序
					Collections.sort(mProduct.getSpecArr());
					//循环获取商品规格
					//并将商品规格以specName为key进行分类
					//specName相同为一组
					for (SPProductSpec productSpec : mProduct.getSpecArr()) {

						List<SPProductSpec> specList = null;
						if (specJson.has(productSpec.getSpecName())){
							specList = (List<SPProductSpec>)specJson.get(productSpec.getSpecName());
							if(!(specList.contains(productSpec))){
								specList.add(productSpec);
							}
						}else{
							specList = new ArrayList<SPProductSpec>();
							specList.add(productSpec);
						}

						specJson.put(productSpec.getSpecName() ,specList);
					}
				}
			}

			//获取每组规格中的第一个规格
			selectSpecMap.clear();

			Iterator<String> iterator = specJson.keys();
			while (iterator.hasNext()){
				String key = iterator.next();
				List<SPProductSpec> specs = (List<SPProductSpec>)specJson.get(key);
				if (specs!=null && specs.size()> 0) {
					SPProductSpec productSpec = specs.get(0);
					selectSpecMap.put(key,productSpec.getItemID());
				}
			}
			refreshPriceView();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void refreshPriceView(){
		if (selectSpecMap != null || selectSpecMap.size() > 0) {
			currShopPrice = SPShopUtils.getShopprice(priceJson, selectSpecMap.values());
			if (SPStringUtils.isEmpty(currShopPrice)){
				currShopPrice = mProduct.getShopPrice();
			}
			this.nowPriceTxtv.setText("现价: ¥"+currShopPrice);
		}
	}

	public void updateSelectSpec(Map<String , String> selectSpecMap){
		this.selectSpecMap = selectSpecMap;
		refreshPriceView();
	}

	private void buildProductGallery(List<String> gallerys){
		if(gallerys == null || gallerys.size() < 1)return;
		mGallery.removeAllViews();
		int productImageWidth =  Float.valueOf(getResources().getDimension(R.dimen.dp_300)).intValue();
		DisplayMetrics displayMetrics = SPMobileApplication.getInstance().getDisplayMetrics();

		for (int i = 0; i < gallerys.size(); i++){
			String url = gallerys.get(i);
			ImageView imageView = new ImageView(this);
			ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(displayMetrics.widthPixels , productImageWidth);
			imageView.setLayoutParams(lp);
			Glide.with(this).load(url).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
			//mGallery.addView(imageView);
			mScroll.addPage(imageView);
		}
	}
}
