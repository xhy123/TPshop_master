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
 * Description:MineFragment
 * @version V1.0
 */
package com.soubao.tpshop.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.person.SPCouponListActivity;
import com.soubao.tpshop.activity.shop.SPProductListActivity;
import com.soubao.tpshop.adapter.SPCouponListAdapter;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.condition.SPProductCondition;
import com.soubao.tpshop.http.person.SPPersonRequest;
import com.soubao.tpshop.http.shop.SPShopRequest;
import com.soubao.tpshop.model.SPProduct;
import com.soubao.tpshop.model.shop.SPCoupon;
import com.soubao.tpshop.model.shop.SPGoodsComment;
import com.soubao.tpshop.model.shop.SPShopOrder;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPDialogUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 *  优惠券列表 -> 优惠券
 *
 */
public class SPCouponListFragment extends SPBaseFragment {

	private String TAG = "SPCouponListFragment";
	private Context mContext;
	private int mType ;
	SPCouponListAdapter mAdapter;
	boolean hasLoadData = false;

	PtrClassicFrameLayout ptrClassicFrameLayout;
	ListView listView;
	List<SPCoupon> coupons;
	int mPageIndex;   //当前第几页:从1开始
	/**
	 *  最大页数
	 */
	boolean mIsMaxPage;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
	}

	public void setType(int type){
		this.mType = type;
		hasLoadData = false;
		if(mAdapter != null)mAdapter.setType(mType);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		hasLoadData = false;
	    View view = inflater.inflate(R.layout.person_coupon_fragment_view, null,false);
		mAdapter = new SPCouponListAdapter(getActivity() , mType);
		initSubView(view);
		return view;
	}

	public void loadData(){
		if (hasLoadData){
			return;
		}
		refreshData();

	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void initSubView(View view) {

		ptrClassicFrameLayout = (PtrClassicFrameLayout)view.findViewById(R.id.coupon_list_view_frame);
		listView = (ListView)view.findViewById(R.id.coupon_listv);
		listView.setAdapter(mAdapter);

		ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				//下拉刷新
				refreshData();
			}
		});

		ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void loadMore() {
				//上拉加载更多
				loadMoreData();
			}
		});
	}


	/**
	 * 加载数据
	 * @Description: 加载数据
	 * @return void    返回类型
	 * @throws
	 */
	public void refreshData(){

		coupons = new ArrayList<SPCoupon>();
		mPageIndex = 1;
		mIsMaxPage = false;
		SPCouponListActivity couponListActivity = (SPCouponListActivity)getActivity();
		SPPersonRequest.getCouponListWithType(mType , mPageIndex , new SPSuccessListener(){
			@Override
			public void onRespone(String msg, Object response) {
				if (response != null){
					coupons = (List<SPCoupon> )response;
					mIsMaxPage = false;
					mAdapter.setData(coupons);
					ptrClassicFrameLayout.setLoadMoreEnable(true);
				} else {
					mIsMaxPage = true;
					ptrClassicFrameLayout.setLoadMoreEnable(false);
				}
				hasLoadData = true;
				ptrClassicFrameLayout.refreshComplete();
			}
		} , new SPFailuredListener(couponListActivity){
				@Override
				public void onRespone(String msg, int errorCode) {
					ptrClassicFrameLayout.setLoadMoreEnable(false);
				}
		});
	}


	public void loadMoreData() {

		if (mIsMaxPage) {
			return;
		}
		mPageIndex++;
		SPPersonRequest.getCouponListWithType(mType, mPageIndex, new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {
				if(response!=null){
					List<SPCoupon> tempComment = (List<SPCoupon>) response;
					coupons.addAll(tempComment);
					//更新收藏数据
					mAdapter.setData(coupons);
					mIsMaxPage = false;
					ptrClassicFrameLayout.setLoadMoreEnable(true);
				}else{
					mPageIndex--;
					mIsMaxPage = true;
					ptrClassicFrameLayout.setLoadMoreEnable(false);
				}
				ptrClassicFrameLayout.loadMoreComplete(true);
			}
	}, new SPFailuredListener() {
			@Override
			public void onRespone(String msg, int errorCode) {
				showToast(msg);
				mPageIndex--;
			}
		});
	}

	@Override
	public void initEvent() {

	}

	@Override
	public void initData() {

	}


}
