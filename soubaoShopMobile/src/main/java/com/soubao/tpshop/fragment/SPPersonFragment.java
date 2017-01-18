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
 * Date: @date 2015年10月20日 下午7:19:26 
 * Description:SPPersonFragment
 * @version V1.0
 */
package com.soubao.tpshop.fragment;

import com.chanven.lib.cptr.loadmore.GridViewWithHeaderAndFooter;
import com.soubao.tpshop.R;
import com.soubao.tpshop.SPMainActivity;
import com.soubao.tpshop.activity.person.SPCouponListActivity_;
import com.soubao.tpshop.activity.person.user.SPLoginActivity_;
import com.soubao.tpshop.activity.person.SPCollectListActivity_;
import com.soubao.tpshop.activity.person.SPSettingListActivity_;
import com.soubao.tpshop.activity.person.SPWalletLogtListActivity_;
import com.soubao.tpshop.activity.person.address.SPConsigneeAddressListActivity_;
import com.soubao.tpshop.activity.person.order.SPOrderListActivity_;
import com.soubao.tpshop.activity.person.user.SPUserDetailsActivity_;
import com.soubao.tpshop.activity.shop.SPProductDetailActivity_;
import com.soubao.tpshop.adapter.SPGuessYouLikeAdapter;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.shop.SPShopRequest;
import com.soubao.tpshop.model.SPProduct;
import com.soubao.tpshop.model.person.SPUser;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPOrderUtils;

import com.soubao.tpshop.utils.SPStringUtils;
import com.soubao.tpshop.view.SPGuessYouLikeView;
import com.soubao.tpshop.view.SPMoreImageView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


/**
 *  首页 -> 我的
 *
 */
public class SPPersonFragment extends SPBaseFragment implements View.OnClickListener  {

	private String TAG = "SPPersonFragment";

	private Context mContext;

	//View addressView;
	View allOrderLayout;
	View waitPayLayout;			//待支付
	View waitReceiveLayout;		//待收货
	View waitCommentLayout;		//待评价
	View waitReturnLayout;		//退换货
	View collectLayout;			//收藏

	View integrateView;			//积分,余额
	View receiveAddressView;	//收货地址
	View couponView;			//优惠券
	//View settingsView;			//设置

	TextView balanceTxtv;			//余额
	TextView pointTxtv;				//积分
	TextView couponCountTxtv;		//优惠券数量
	//SPGuessYouLikeView  recommendProductView;

	TextView nicknameTxtv; 			//昵称

	RelativeLayout header_relayout;
	SPMoreImageView nickImage;
	TextView nickNameTxtv;

	//setting_btn
	Button settingBtn;

	//account_rlayout
	View accountView;

	//level_img
	ImageView levelImgv;

	//level_name_txtv
	TextView levelName;

	GridView mGridView;
	SPGuessYouLikeAdapter mAdapter;
	List<SPProduct> mProducts ;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.person_fragment, null,false);

		initSubView(view);
		initEvent();
		initData();

		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void initSubView(View view) {
		allOrderLayout = view.findViewById(R.id.person_order_all_container);
		waitPayLayout =  view.findViewById(R.id.personal_order_waitpay_layout);
		waitReceiveLayout =  view.findViewById(R.id.personal_order_waitreceive_layout);
		waitCommentLayout =  view.findViewById(R.id.personal_order_waitcomment_layout);
		waitReturnLayout =  view.findViewById(R.id.personal_order_returned);
		collectLayout = view.findViewById(R.id.person_collect_aview);
		integrateView = view.findViewById(R.id.person_integrate_rlayout); 			//积分,余额
		receiveAddressView = view.findViewById(R.id.person_receive_address_aview);	//收货地址
		couponView = view.findViewById(R.id.person_coupon_aview);					//优惠券
		//settingsView = view.findViewById(R.id.person_settings_aview);				//设置

		balanceTxtv = (TextView)view.findViewById(R.id.person_balance_txtv);	//余额
		pointTxtv = (TextView)view.findViewById(R.id.person_point_txtv);		//积分
		couponCountTxtv = (TextView)view.findViewById(R.id.person_coupon_txtv);		//优惠券数量
		nicknameTxtv = (TextView)view.findViewById(R.id.nickname_txtv);		//昵称
		header_relayout= (RelativeLayout)view.findViewById(R.id.header_relayout);
		nickImage = (SPMoreImageView)view.findViewById(R.id.head_mimgv);
		mGridView = (GridView)view.findViewById(R.id.product_gdv);

		////setting_btn
		settingBtn = (Button)view.findViewById(R.id.setting_btn);
		//account_rlayout
		accountView = view.findViewById(R.id.account_rlayout);
		//level_img
		levelImgv = (ImageView)view.findViewById(R.id.level_img);
		//level_name_txtv
		levelName = (TextView)view.findViewById(R.id.level_name_txtv);;

		String path= Environment.getExternalStorageDirectory().getPath();
		//showToast(path);
		Bitmap mBitmap = BitmapFactory.decodeFile(path + "/head.jpg");// 从sdcard中获取本地图片,通过BitmapFactory解码,转成bitmap
		if (mBitmap != null) {
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(mBitmap);
			nickImage.setImageDrawable(drawable);
		} else {
			/** 从服务器取,同时保存在本地 ,后续的工作 */
		}
		nickNameTxtv= (TextView)view.findViewById(R.id.nickname_txtv);
	}

	@Override
	public void initEvent() {

		//addressView.setOnClickListener(this);

		allOrderLayout.setOnClickListener(this);
		waitPayLayout.setOnClickListener(this);
		waitReceiveLayout.setOnClickListener(this);
		waitCommentLayout.setOnClickListener(this);
		waitReturnLayout.setOnClickListener(this);
		collectLayout.setOnClickListener(this);

		integrateView.setOnClickListener(this);
		receiveAddressView.setOnClickListener(this);
		couponView.setOnClickListener(this);
		//settingsView.setOnClickListener(this);

		header_relayout.setOnClickListener(this);
		nickImage.setOnClickListener(this);
		nickNameTxtv.setOnClickListener(this);

		settingBtn.setOnClickListener(this);
		accountView.setOnClickListener(this);


		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Log.i(TAG, "onItemClick...");

				if (mProducts != null && position >= 0 && position < mProducts.size()) {
					SPProduct product = mProducts.get(position);
					startupActivity(product.getGoodsID());
					Log.i(TAG, "onItemClick product.goodsName :"+product.getGoodsName() );
				}
			}
		});
	}

	public void initData(){

		mAdapter = new SPGuessYouLikeAdapter(getActivity());
		mGridView.setAdapter(mAdapter);
		refreshData(1);
	}


	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.person_order_all_container){
			startupOrderList(SPOrderUtils.OrderStatus.all.value());
		}else  if(v.getId() == R.id.personal_order_waitpay_layout){
			startupOrderList(SPOrderUtils.OrderStatus.waitPay.value());
		}else  if(v.getId() == R.id.personal_order_waitreceive_layout){
			startupOrderList(SPOrderUtils.OrderStatus.waitReceive.value());
		}else  if(v.getId() == R.id.personal_order_waitcomment_layout){
			showToast(getString(R.string.copyright_title));
			return;
		}else  if(v.getId() == R.id.personal_order_returned){
			showToast(getString(R.string.copyright_title));
			return;
		}else if(v.getId() == R.id.person_collect_aview){
			//我的收藏
			if (!checkLogin())return;
			Intent collectIntent = new Intent(getActivity() , SPCollectListActivity_.class);
			startActivity(collectIntent);
		}else if(v.getId() == R.id.person_integrate_rlayout){
			//积分,余额
			if (!checkLogin())return;
			getActivity().startActivity(new Intent(getActivity() , SPWalletLogtListActivity_.class));
		}else if(v.getId() == R.id.person_receive_address_aview){
			//收货地址
			if (!checkLogin())return;
			getActivity().startActivity(new Intent(getActivity() , SPConsigneeAddressListActivity_.class));
		}else if(v.getId() == R.id.person_coupon_aview){
			//优惠券
			if (!checkLogin())return;
			getActivity().startActivity(new Intent(getActivity() , SPCouponListActivity_.class));
		}else if(v.getId() == R.id.setting_btn){
			//设置
			getActivity().startActivity(new Intent(getActivity(), SPSettingListActivity_.class));
		} /*else if(v.getId() == R.id.header_relayout){
			loginOrDetail(SPMobileApplication.getInstance().isLogined);
		}*/ else if(v.getId()== R.id.nickname_txtv){
			loginOrDetail(false);
		} else if(v.getId()==R.id.head_mimgv || v.getId()==R.id.account_rlayout){
			loginOrDetail(SPMobileApplication.getInstance().isLogined);
		}
	}

	/**
	 * doLogin or details
	 * @param flag  true go detail, false go login
	 */
	private void loginOrDetail(boolean flag){
		if (flag) {
			getActivity().startActivity(new Intent(getActivity(), SPUserDetailsActivity_.class));

		}else {
			getActivity().startActivity(new Intent(getActivity(), SPLoginActivity_.class));
		}

	}

	boolean checkLogin(){
		if (!SPMobileApplication.getInstance().isLogined) {
			showToastUnLogin();
			toLoginPage();
			return false;
		}
		return true;
	}

	public void startupOrderList(int orderStatus){

		if (!SPMobileApplication.getInstance().isLogined){
			showToastUnLogin();
			toLoginPage();
			return;
		}

		Intent allOrderList = new Intent(getActivity() , SPOrderListActivity_.class);
		allOrderList.putExtra("orderStatus", orderStatus);
		getActivity().startActivity(allOrderList);
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshView();
	}

	/**
	 * 刷新View 数据
	 */
	public void refreshView(){

		if (SPMobileApplication.getInstance().isLogined){
			SPUser user = SPMobileApplication.getInstance().getLoginUser();
			balanceTxtv.setText(String.valueOf(user.getUserMoney()));
			pointTxtv.setText(String.valueOf(user.getPayPoints()));
			if(SPStringUtils.isEmpty(user.getCouponCount())){
				couponCountTxtv.setText("0");
			}else{
				couponCountTxtv.setText(String.valueOf(user.getCouponCount()));
			}
			if (!SPStringUtils.isEmpty(user.getNickname())){
				nicknameTxtv.setText(user.getNickname());
			}

			if (!SPStringUtils.isEmpty(user.getLevelName())){
				levelImgv.setVisibility(View.VISIBLE);
				levelName.setVisibility(View.VISIBLE);
				levelName.setText(user.getLevelName());

				switch (Integer.valueOf(user.getLevel())){
					case 1:
						levelImgv.setImageResource(R.drawable.icon_level_one);
						break;
					case 2:
						levelImgv.setImageResource(R.drawable.icon_level_two);
						break;
					case 3:
						levelImgv.setImageResource(R.drawable.icon_level_three);
						break;
					default:
						levelImgv.setImageResource(R.drawable.icon_level_one);
						break;
				}

			}

			RoundedBitmapDrawable roundedBitmap = getCycleBitmpa(false);
			if (roundedBitmap != null) {
				nickImage.setImageDrawable(roundedBitmap);
			} else {
				/** 从服务器取,同时保存在本地 ,后续的工作 */
			}
		}else{
			balanceTxtv.setText("0");
			pointTxtv.setText("0");
			couponCountTxtv.setText("0");
			nickNameTxtv.setText("点击登录");
			levelImgv.setVisibility(View.INVISIBLE);
			levelName.setVisibility(View.INVISIBLE);

			RoundedBitmapDrawable roundedBitmap = getCycleBitmpa(true);
			if (roundedBitmap != null) {
				nickImage.setImageDrawable(roundedBitmap);
			} else {
				/** 从服务器取,同时保存在本地 ,后续的工作 */
			}
		}
	}

	public RoundedBitmapDrawable getCycleBitmpa(boolean isDefault){

		RoundedBitmapDrawable circularBitmapDrawable = null;
		String path= Environment.getExternalStorageDirectory().getPath();
		Bitmap mBitmap = BitmapFactory.decodeFile(path + "/head.jpg");
		if (isDefault){
			Drawable defaultDrawable = getResources().getDrawable(R.drawable.person_default_head);
			BitmapDrawable bd = (BitmapDrawable) defaultDrawable;
			mBitmap = bd.getBitmap();
		}else{
			mBitmap = BitmapFactory.decodeFile(path + "/head.jpg");
		}

		if (mBitmap != null) {
			circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), mBitmap);
			circularBitmapDrawable.setCornerRadius(getActivity().getResources().getDimension(R.dimen.head_corner_35));
			//nickImage.setImageDrawable(circularBitmapDrawable);
		}
		return circularBitmapDrawable;
	}

	public void refreshData(int page) {
		SPShopRequest.guessYouLike(page, new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {
				if (response != null) {
					mProducts = (List<SPProduct>) response;
					mAdapter.setData(mProducts);
					//recommendProductView.refreshProducts(products, false);
				} else {
					//recommendProductView.refreshProducts(null, true);
				}
				setListViewHeightBasedOnChildren();
			}
		}, new SPFailuredListener() {
			@Override
			public void onRespone(String msg, int errorCode) {
				//recommendProductView.refreshProducts(null, true);
				showToast(msg);
			}
		});
	}



	private void setListViewHeightBasedOnChildren() {
		int count = Double.valueOf(Math.ceil(Double.valueOf(mAdapter.getCount() / 2.0))).intValue();
		/**
		 * 获取屏幕宽度和高度
		 */
		DisplayMetrics metric = new DisplayMetrics();
		SPMainActivity.getmInstance().getWindowManager().getDefaultDisplay().getMetrics(metric);
		float itemheight = getResources().getDimension(R.dimen.product_item_height);
		LinearLayout.LayoutParams  params =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, Float.valueOf(count * itemheight).intValue());
		mGridView.setLayoutParams(params);
	}

	public void startupActivity(String goodsID){
		Intent intent = new Intent(getActivity() , SPProductDetailActivity_.class);
		intent.putExtra("goodsID", goodsID);
		startActivity(intent);
	}
}
