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
 * Date: @date 2015年11月14日 下午8:17:18 
 * Description: 猜你喜欢
 * @version V1.0
 */
package com.soubao.tpshop.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.loadmore.GridViewWithHeaderAndFooter;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.soubao.tpshop.R;
import com.soubao.tpshop.adapter.SPGuessYouLikeAdapter;
import com.soubao.tpshop.model.SPProduct;
import com.soubao.tpshop.utils.SMobileLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 *
 */
public class SPGuessYouLikeView extends LinearLayout {

	private String TAG = "SPGuessYouLikeView";

	int mPageIndex = 1 ;		//当前第几页
	boolean mIsMaxPage;			//是否最大页数
	PtrClassicFrameLayout ptrClassicFrameLayout;
	GridViewWithHeaderAndFooter mGridView;
	SPGuessYouLikeViewListener listener;
	SPGuessYouLikeAdapter mAdapter;
	List<SPProduct> mProducts ;


	/**
	 * @param context
	 * @param attrs
	 */
	public SPGuessYouLikeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	    View view = LayoutInflater.from(context).inflate(R.layout.product_guess_you_like_list, this);
		ptrClassicFrameLayout = (PtrClassicFrameLayout)view.findViewById(R.id.product_list_view_frame);
		mGridView = (GridViewWithHeaderAndFooter)view.findViewById(R.id.product_gdvv);
		mAdapter = new SPGuessYouLikeAdapter(context);
		mGridView.setAdapter(mAdapter);
		initViewEvent();
	}

	public void initViewEvent(){
		ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void loadMore() {
				//上拉加载更多
				loadMoreData();
			}
		});
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (mProducts!=null && position>=0 && position < mProducts.size()){
					SPProduct product = mProducts.get(position);
					if (listener!=null)listener.itemClick(product);
				}
			}
		});
	}

	//下拉刷新
	public void refreshData(){
		mPageIndex = 1;
		mIsMaxPage = false;
		if (listener!=null)listener.refreshData(mPageIndex);
	}

	//上拉加载更多
	private void loadMoreData(){
		if (mIsMaxPage){
			return;
		}
		mPageIndex++;
		if (listener!=null)listener.loadMoreData(mPageIndex);
	}

	public interface SPGuessYouLikeViewListener{
		public void refreshData(int page);
		public void loadMoreData(int page);
		public void itemClick(SPProduct product);
	}

	public void refreshProducts(List<SPProduct> products ,  boolean isMax){
		mIsMaxPage = isMax;
		this.mProducts = products;
		if (mProducts == null){
			mProducts = new ArrayList<SPProduct>();
		}
		mAdapter.setData(mProducts);
		ptrClassicFrameLayout.refreshComplete();
		/*if (isMax){
			ptrClassicFrameLayout.setLoadMoreEnable(false);
		}else{
			ptrClassicFrameLayout.setLoadMoreEnable(true);
		}*/
	}

	public void loadMoreProducts(List<SPProduct> products ,  boolean isMax){
		if (products != null){
			mProducts.addAll(products);
		}

		if (isMax){
			ptrClassicFrameLayout.setLoadMoreEnable(false);
			ptrClassicFrameLayout.loadMoreComplete(false);
			mPageIndex--;
		}else{
			ptrClassicFrameLayout.setLoadMoreEnable(true);
			ptrClassicFrameLayout.loadMoreComplete(true);
		}

		mIsMaxPage = isMax;
		mAdapter.setData(mProducts);
	}

	public int getProductCount(){
		if (mProducts!=null)return mProducts.size();
		return 0;
	}

	public void setGuessYouLikeViewListener(SPGuessYouLikeViewListener listener){
		this.listener = listener;
	}

}





















































































