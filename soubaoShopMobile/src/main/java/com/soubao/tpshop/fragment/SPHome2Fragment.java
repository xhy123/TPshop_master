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
 * Description:商城Fragment
 * @version V1.0
 */
package com.soubao.tpshop.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.cundong.recyclerview.RecyclerViewUtils;
import com.soubao.tpshop.R;
import com.soubao.tpshop.SPMainActivity;
import com.soubao.tpshop.activity.common.SPSearchCommonActivity_;
import com.soubao.tpshop.activity.person.SPCollectListActivity_;
import com.soubao.tpshop.activity.person.order.SPOrderListActivity_;
import com.soubao.tpshop.activity.shop.SPProductListActivity;
import com.soubao.tpshop.adapter.SPHomeCategoryAdapter;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.home.SPHomeRequest;
import com.soubao.tpshop.model.SPCategory;
import com.soubao.tpshop.model.SPHomeBanners;
import com.soubao.tpshop.model.SPHomeCategory;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPOrderUtils;
import com.soubao.tpshop.view.SPMobileScrollLayout;
import com.soubao.tpshop.zxing.MipcaActivityCapture;

import org.json.JSONObject;

import java.util.List;


/**
 * 首页 -> 商城首页
 * http://blog.csdn.net/jdsjlzx/article/details/49966101
 *
 */
public class SPHome2Fragment extends SPBaseFragment implements View.OnClickListener  {

	private String TAG = "SPHome2Fragment";
	public final static int CATEGORY_FRAGMENT = 1;
	public final static int SHOPCART_FRAGMENT = 2;
	private Context mContext;

	View mHeaderView;
	RecyclerView mRecyclerView ;

	View categoryLayout;
	View shopcartLayout;
	View orderLayout;
	View couponLayout;
	RelativeLayout homeTitleView;
	EditText searchText;
	SPMobileScrollLayout mScrolllayout;	//轮播广告scrollLayout
	ViewGroup mPointerLayout; //指示点Layout
	SPHomeCategoryAdapter mAdapter;

	ImageView upLeftImgv;			//上-> 左
	ImageView upRightTopImgv;		//上-> 右 -> 上
	ImageView upRightBottomImgv;	//上-> 右 -> 下

	ImageView bottomLeftImgv;	//下-> 左
	ImageView bottomRightImgv;	//下-> 右
	SPMainActivity mainActivity;
	
	private final static int SCANNIN_GREQUEST_CODE = 1;
	TextView scanView;

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

	    View view = inflater.inflate(R.layout.home_fragment, null,false);
		mHeaderView = inflater.inflate(R.layout.home_header_view, null);
		categoryLayout = mHeaderView.findViewById(R.id.home_menu_categroy_layout);
		shopcartLayout = mHeaderView.findViewById(R.id.home_menu_shopcart_layout);
		orderLayout = mHeaderView.findViewById(R.id.home_menu_order_layout);
		couponLayout = mHeaderView.findViewById(R.id.home_menu_coupon_layout);

		upLeftImgv = (ImageView)mHeaderView.findViewById(R.id.up_left_imgv);
		upRightTopImgv = (ImageView)mHeaderView.findViewById(R.id.up_right_top_imgv);
		upRightBottomImgv = (ImageView)mHeaderView.findViewById(R.id.up_right_bottom_imgv);
		bottomLeftImgv = (ImageView)mHeaderView.findViewById(R.id.bottom_left_imgv);
		bottomRightImgv = (ImageView)mHeaderView.findViewById(R.id.bottom_right_imgv);


		homeTitleView = (RelativeLayout) view.findViewById(R.id.toprela);
		mRecyclerView = (RecyclerView)view.findViewById(R.id.home_listv);
		homeTitleView.getBackground().setAlpha(0);

		searchText = (EditText) homeTitleView.findViewById(R.id.searchkey_edtv);
		searchText.setFocusable(false);
		searchText.setFocusableInTouchMode(false);
		
		scanView= (TextView) view.findViewById(R.id.image_left);
		scanView.setOnClickListener(this);

		mAdapter  = new SPHomeCategoryAdapter(mContext);
		HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
		mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerView.addOnScrollListener(mOnScrollListener);

		RecyclerViewUtils.setHeaderView(mRecyclerView, mHeaderView);

		/** 设置listView header view : 广告轮播 */
		mScrolllayout = (SPMobileScrollLayout)mHeaderView.findViewById(R.id.banner_slayout);
		super.init(view);

		return view;
	}



	@Override
	public void onDetach() {
		super.onDetach();
	}


	@Override
	public void initSubView(View view) {

	}

	@Override
	public void initEvent() {
		searchText.setOnClickListener(this);
		upLeftImgv.setOnClickListener(this);
		upRightTopImgv.setOnClickListener(this);
		upRightBottomImgv.setOnClickListener(this);
		bottomLeftImgv.setOnClickListener(this);
		bottomRightImgv.setOnClickListener(this);

		categoryLayout.setOnClickListener(this);
		shopcartLayout.setOnClickListener(this);
		orderLayout.setOnClickListener(this);
		orderLayout.setOnClickListener(this);
		couponLayout.setOnClickListener(this);

	}

	@Override
	public void initData() {
		refreshData();
	}

	public void refreshData() {
		SPHomeRequest.getHomeData(new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {
				hideLoadingToast();
				//homePcf.refreshComplete();
				//homePcf.setLoadMoreEnable(false);
				mDataJson = (JSONObject) response;
				try {
					if (mDataJson.has("homeCategories")) {
						List<SPHomeCategory> homeCategories = (List<SPHomeCategory>) mDataJson.get("homeCategories");
						dealModels(homeCategories);
					}
					if (mDataJson.has("banners")) {
						List<SPHomeBanners> banners = (List<SPHomeBanners>) mDataJson.get("banners");
						mScrolllayout.removeAllViews();
						for (SPHomeBanners banner : banners) {
							//String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL , product.getGoodsID());
							ImageView img = getImageViewByBg(R.drawable.b_m);
							Glide.with(mContext).load(String.format(banner.getAdCode())).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);

							img.setScaleType(ImageView.ScaleType.FIT_XY);
							mScrolllayout.addView(img);
						}
						mScrolllayout.invalidate();
						buildPointer();
					}
				} catch (Exception e) {
					showToast(e.getMessage());
					e.printStackTrace();
				}
			}
		}, new SPFailuredListener() {
			@Override
			public void onRespone(String msg, int errorCode) {
				hideLoadingToast();
				//homePcf.refreshComplete();
				//homePcf.setLoadMoreEnable(false);
				Log.e(TAG, "zzx==>error msg: " + msg);
			}
		});
	}

	private ImageView getImageViewByBg(int imageResId){
		ImageView imageView = new ImageView(mContext);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT ,
				ViewGroup.LayoutParams.MATCH_PARENT);

		imageView.setLayoutParams(lp);
		imageView.setBackgroundResource(imageResId);
		return imageView;
	}

	/**
	 * 构建轮播广告"圆点指示器"
	 */
	public void buildPointer(){

		// 获取子view个数, 用来计算圆点指示器个数
		int pageCount = mScrolllayout.getChildCount();
		mPointerLayout = (ViewGroup)mHeaderView.findViewById(R.id.pointer_layout);

		ImageView[] pointerImgv = new ImageView[pageCount];
		mPointerLayout.removeAllViews();
		for (int i = 0; i < pageCount; i++) {
			ImageView imageView = new ImageView(this.mContext);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20 , 20);//圆点指示器宽高
			lp.setMargins(8, 0, 8, 0);
			imageView.setLayoutParams(lp);
			imageView.setPadding(20, 0, 20, 0);
			pointerImgv[i] = imageView;
			if (i == 0) {
				//默认选中第一张图片
				pointerImgv[i].setBackgroundResource(R.drawable.ic_home_arrows_focus);
			} else {
				pointerImgv[i].setBackgroundResource(R.drawable.ic_home_arrows_normal);
			}
			mPointerLayout.addView(pointerImgv[i]);
		}
	}

	private void dealModels(List<SPHomeCategory> cate){
		mAdapter.setData(cate);
	}


	private int getScrolledDistance() {

		LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
		View firstVisibleItem = mRecyclerView.getChildAt(0);
		int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
		int itemHeight = firstVisibleItem.getHeight();
		int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibleItem);
		return (firstItemPosition + 1) * itemHeight - firstItemBottom;
	}

	private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);

			int scrollY = getScrolledDistance();
			if (scrollY == 0) {
				//int progress = (int) (new Float(dy) / new Float(lHeight) * 200);//255
				homeTitleView.getBackground().setAlpha(0);
				//homeTitleView.setAlpha(0);

			} else {
				homeTitleView.getBackground().setAlpha(255 - 55);
				//homeTitleView.setAlpha(0.9f);
			}
		}
	};

	public void setMainActivity(SPMainActivity mainActivity){
		this.mainActivity = mainActivity;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()){
			case R.id.searchkey_edtv:
				Intent intent = new Intent(getActivity() , SPSearchCommonActivity_.class);
				getActivity().startActivity(intent);
				break;
			case R.id.up_left_imgv:
			case R.id.up_right_top_imgv:
			case R.id.up_right_bottom_imgv:
			case R.id.bottom_left_imgv:
			case R.id.bottom_right_imgv:
				List<SPCategory> categories = SPMobileApplication.getInstance().getTopCategorys();
				SPCategory category = null;
				if(SPCommonUtils.verifyArray(categories)){
					category = categories.get(0);
				}
				startupProductListActivity(category);
				break;
			case R.id.home_menu_categroy_layout :
				mainActivity.setShowFragment(CATEGORY_FRAGMENT);
				break;
			case R.id.home_menu_shopcart_layout :
				mainActivity.setShowFragment(SHOPCART_FRAGMENT);
				break;
			case R.id.home_menu_order_layout :
				startupOrderList(SPOrderUtils.OrderStatus.all.value());
				break;
			case R.id.home_menu_coupon_layout :
				startupCollection();
				break;
			case R.id.image_left:
				Intent intentC = new Intent();
				intentC.setClass(getActivity(), MipcaActivityCapture.class);
				intentC.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intentC, SCANNIN_GREQUEST_CODE);
				break;
		}
	}


	boolean checkLogin(){
		if (!SPMobileApplication.getInstance().isLogined){
			showToastUnLogin();
			toLoginPage();
			return false;
		}
		return true;
	}

	public void startupCollection(){
		if (!checkLogin())return;
		Intent collectIntent = new Intent(getActivity() , SPCollectListActivity_.class);
		getActivity().startActivity(collectIntent);
	}

	public void startupOrderList(int orderStatus){
		if (!SPMobileApplication.getInstance().isLogined){
			showToastUnLogin();
			toLoginPage();
			return;
		}
		Intent allOrderList = new Intent(getActivity() , SPOrderListActivity_.class);
		allOrderList.putExtra("orderStatus" , orderStatus);
		getActivity().startActivity(allOrderList);
	}

	public void startupProductListActivity(SPCategory category){
		Intent intent = new Intent(getActivity() , SPProductListActivity.class);
		if (category!=null){
			intent.putExtra("category", category);
		}
		getActivity().startActivity(intent);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode){
				case SCANNIN_GREQUEST_CODE:
					Bundle bundle = data.getExtras();
					//显示扫描到的内容
					//mTextView.setText(bundle.getString("result"));
					//显示
					//mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
					showToast(bundle.getString("result"));
					break;
			}
		}
	}
}
