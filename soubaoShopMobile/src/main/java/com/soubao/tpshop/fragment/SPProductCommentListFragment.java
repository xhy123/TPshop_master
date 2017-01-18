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
 * Description: 商品评论
 * @version V1.0
 */
package com.soubao.tpshop.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.soubao.tpshop.R;
import com.soubao.tpshop.adapter.SPProductDetailCommentAdapter;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.person.SPPersonRequest;
import com.soubao.tpshop.http.shop.SPShopRequest;
import com.soubao.tpshop.model.person.SPWalletLog;
import com.soubao.tpshop.model.shop.SPGoodsComment;
import com.soubao.tpshop.utils.SMobileLog;

import org.androidannotations.annotations.ViewById;

import java.util.List;


/**
 *  商品详情 -> 商品评论列表
 *
 */
public class SPProductCommentListFragment extends SPBaseFragment {

	private String TAG = "SPProductCommentListFragment";
	private Context mContext;
	private String goodsId ;

	PtrClassicFrameLayout ptrClassicFrameLayout;
	ListView commentListv;
	SPProductDetailCommentAdapter mAdapter;
	List<SPGoodsComment> mComments ;

	boolean isFirstLoad;

	int pageIndex;   //当前第几页:从1开始
	/**
	 *  最大页数
	 */
	boolean maxIndex;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
	}

	public void setGoodsId(String goodsId){
		this.goodsId = goodsId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		isFirstLoad = true;
	    View view = inflater.inflate(R.layout.product_details_comment_list, null,false);
		initSubView(view);
		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void initSubView(View view) {
		ptrClassicFrameLayout = (PtrClassicFrameLayout)view.findViewById(R.id.product_comment_list_view_frame);
		commentListv  = (ListView)view.findViewById(R.id.product_comment_listv);

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

		mAdapter = new SPProductDetailCommentAdapter(getActivity());
		commentListv.setAdapter(mAdapter);
		loadData();
	}

	@Override
	public void initEvent() {

	}

	@Override
	public void initData() {

	}

	public void refreshData(){
		pageIndex = 1;
		maxIndex = false;
		//showLoadingToast();
		SPShopRequest.getGoodsCommentWithGoodsID(goodsId , pageIndex , new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {

				if (response != null) {
					mComments = (List<SPGoodsComment>) response;
					//更新收藏数据
					mAdapter.setData(mComments);
					ptrClassicFrameLayout.setLoadMoreEnable(true);
				} else {
					maxIndex = true;
					ptrClassicFrameLayout.setLoadMoreEnable(false);
				}
				ptrClassicFrameLayout.refreshComplete();
				hideLoadingToast();
			}
		}, new SPFailuredListener() {
			@Override
			public void onRespone(String msg, int errorCode) {
				showToast(msg);
				hideLoadingToast();
			}
		});
	}


	public void loadMoreData() {

		if (maxIndex) {
			return;
		}
		pageIndex++;
		//showLoadingToast();
		SPShopRequest.getGoodsCommentWithGoodsID(goodsId, pageIndex, new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {

				if (response != null) {
					List<SPGoodsComment> tempComment = (List<SPGoodsComment>) response;
					mComments.addAll(tempComment);
					//更新收藏数据
					mAdapter.setData(mComments);
					ptrClassicFrameLayout.setLoadMoreEnable(true);
				} else {
					pageIndex--;
					maxIndex = true;
					ptrClassicFrameLayout.setLoadMoreEnable(false);
				}
				ptrClassicFrameLayout.refreshComplete();
				hideLoadingToast();
			}
		}, new SPFailuredListener() {
			@Override
			public void onRespone(String msg, int errorCode) {
				hideLoadingToast();
				showToast(msg);
				pageIndex--;
			}
		});
	}

	public void loadData(){
		if (isFirstLoad && commentListv!=null) {
			refreshData();
			isFirstLoad = false;
		}
	}
	
}
