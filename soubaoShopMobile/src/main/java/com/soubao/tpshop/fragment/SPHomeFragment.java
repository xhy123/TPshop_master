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
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.RecyclerViewUtils;
import com.soubao.tpshop.R;
import com.soubao.tpshop.SPMainActivity;
import com.soubao.tpshop.activity.person.SPCollectListActivity_;
import com.soubao.tpshop.activity.person.order.SPOrderListActivity_;
import com.soubao.tpshop.adapter.SPHomeCategoryAdapter;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.home.SPHomeRequest;
import com.soubao.tpshop.model.SPHomeBanners;
import com.soubao.tpshop.model.SPHomeCategory;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPOrderUtils;
import com.soubao.tpshop.view.SPMobileScrollLayout;
import com.soubao.tpshop.view.SPMobileScrollLayout.PageListener;

import org.json.JSONObject;

import java.util.List;


/**
 * 首页 -> 商城首页
 * http://blog.csdn.net/jdsjlzx/article/details/49966101
 *
 */
public class SPHomeFragment extends SPBaseFragment implements PageListener,View.OnClickListener {

	private String TAG = "SPHomeFragment";
	public final static int CATEGORY_FRAGMENT = 1;
	public final static int SHOPCART_FRAGMENT = 2;
	private Context mContext;
	//private PtrClassicFrameLayout homePcf;
	RecyclerView mRecyclerView ;
	SPHomeCategoryAdapter mAdapter;
	View mHeaderView ;				//列表头部: 广告轮播
	ViewGroup mPointerLayout;		//指示点Layout
	SPMobileScrollLayout mScrolllayout;	//轮播广告scrollLayout
	LayoutInflater mInflater;

	View categoryLayout;
	View shopcartLayout;
	View orderLayout;
	View couponLayout;
	RelativeLayout homeTitleView;
	SPMainActivity mainActivity;

	public void setMainActivity(SPMainActivity mainActivity){
		this.mainActivity = mainActivity;
	}

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
		mInflater = inflater;
	    View view = inflater.inflate(R.layout.home_fragment, null,false);
		mHeaderView = inflater.inflate(R.layout.home_header_view, null);

		categoryLayout = mHeaderView.findViewById(R.id.home_menu_categroy_layout);
		shopcartLayout = mHeaderView.findViewById(R.id.home_menu_shopcart_layout);
		orderLayout = mHeaderView.findViewById(R.id.home_menu_order_layout);
		couponLayout = mHeaderView.findViewById(R.id.home_menu_coupon_layout);

		homeTitleView = (RelativeLayout) view.findViewById(R.id.toprela);

		homeTitleView.getBackground().setAlpha(0);

		categoryLayout.setOnClickListener(this);
		shopcartLayout.setOnClickListener(this);
		orderLayout.setOnClickListener(this);
		orderLayout.setOnClickListener(this);
		couponLayout.setOnClickListener(this);
		//homePcf = (PtrClassicFrameLayout)view.findViewById(R.id.home_pcf);
		mRecyclerView = (RecyclerView)view.findViewById(R.id.home_listv);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		mAdapter  = new SPHomeCategoryAdapter(mContext);

		mRecyclerView.setAdapter(mAdapter);

		RecyclerViewUtils.setHeaderView(mRecyclerView, mHeaderView);

	    /** 设置listView header view : 广告轮播 */
		mScrolllayout = (SPMobileScrollLayout)mHeaderView.findViewById(R.id.banner_slayout);
		mRecyclerView.addOnScrollListener(mOnScrollListener);

		super.init(view);
 		
	    //初始化"圆点"指示器
	    //注意: 必须是先构建"圆点指示器", 才能设置滑屏到第一页, 否则会抛出异常
 		buildPointer();
 		
 		
 		 /** 滑动监听事件 */
 		mScrolllayout.setPageListener(this);
 		
 		/** 默认滑动到第一屏 */
 		mScrolllayout.setToScreen(0);	
 		
		return view;
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

	@Override
	public void onDetach() {
		super.onDetach();
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

	
	/**
	 * 
	* @Description: 轮播页改变回调.
	* @param page	当前页索引
	 */
	@Override
	public void page(int page) {
		int count = mPointerLayout.getChildCount();
		for(int i=0; i<count; i++){
			View v = mPointerLayout.getChildAt(i);
			//重新计算"圆点指示器"的位置
			if(v instanceof ImageView){
				if(i == page){
					v.setBackgroundResource(R.drawable.ic_home_arrows_focus);  
				}else{
					v.setBackgroundResource(R.drawable.ic_home_arrows_normal);  
				}
			}
		}
	}

	@Override
	public void initSubView(View view) {

	}

	@Override
	public void initEvent() {
		refreshData();
	}

	@Override
	public void initData() {

	}

	public void refreshData() {


		SPHomeRequest.getHomeData(new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {

				hideLoadingToast();
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

	private void dealModels(List<SPHomeCategory> cate){

		//mAdapter.setData(cate);

	}

	@Override
	public void onClick(View v) {
		if(v == categoryLayout){
			mainActivity.setShowFragment(CATEGORY_FRAGMENT);
		} else if(v == shopcartLayout){
			mainActivity.setShowFragment(SHOPCART_FRAGMENT);
		} else if(v == orderLayout){
			startupOrderList(SPOrderUtils.OrderStatus.all.value());
		} else if(v == couponLayout){
			startupCollection();
		}
	}

	public void startupCollection(){
		if (!checkLogin())return;
		Intent collectIntent = new Intent(getActivity() , SPCollectListActivity_.class);
		getActivity().startActivity(collectIntent);
	}

	boolean checkLogin(){
		if (!SPMobileApplication.getInstance().isLogined){
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
		allOrderList.putExtra("orderStatus" , orderStatus);
		getActivity().startActivity(allOrderList);
	}


	boolean pauseOnScroll = false, pauseOnFling=true;
	private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);

			if (mScrolllayout.getHeight() > 0 ) {
				//define it for scroll height
				int lHeight = mScrolllayout.getHeight();
				if(dy < 0){
					homeTitleView.getBackground().setAlpha(0);
				}else {
					if (dy < lHeight) {
						int progress = (int) (new Float(dy) / new Float(lHeight) * 200);//255
						homeTitleView.getBackground().setAlpha(progress);
					} else {
						homeTitleView.getBackground().setAlpha(255 - 55);
					}
				}
			}
		}


		/*@Override
		public void onBottom() {
			super.onBottom();
			Log.d(TAG, "onBottom");
			LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
			if(state == LoadingFooter.State.Loading) {
				Log.d(TAG, "the state is Loading, just wait..");
				return;
			}

			if (mCurPageIndex < totalPage) {
				// loading more
				RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
				mHandler.sendEmptyMessage(GET_LIST_DATA);
				Log.d(TAG, "onBottom loading");
			} else {
				//the end
				RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
			}
		}

		@Override
		public void onScrollStateChanged(int newState) {
			//根据newState状态做处理
			if (imageLoader != null) {
				switch (newState) {
					case 0:
						imageLoader.resume();
						break;

					case 1:
						if (pauseOnScroll) {
							imageLoader.pause();
						} else {
							imageLoader.resume();
						}
						break;

					case 2:
						if (pauseOnFling) {
							imageLoader.pause();
						} else {
							imageLoader.resume();
						}
						break;
				}
			}
		}*/
	};
}
