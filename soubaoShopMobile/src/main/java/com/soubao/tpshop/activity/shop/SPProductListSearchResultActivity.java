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
 * Date: @date 2015年11月3日 下午10:04:49 
 * Description:产品列表搜索结果页
 * @version V1.0
 */
package com.soubao.tpshop.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.common.SPBaseActivity;
import com.soubao.tpshop.activity.common.SPSearchCommonActivity_;
import com.soubao.tpshop.adapter.SPProductListAdapter;
import com.soubao.tpshop.adapter.SPProductListAdapter.ItemClickListener;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.fragment.SPProductListFilterFragment;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.shop.SPShopRequest;
import com.soubao.tpshop.model.SPCategory;
import com.soubao.tpshop.model.SPProduct;
import com.soubao.tpshop.model.shop.SPShopOrder;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPDialogUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.soubao.tpshop.view.SPProductFilterTabView;

import org.json.JSONObject;

import java.util.List;

/*import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;*/

/**
 * @author 飞龙
 * http://www.tpshop.com/index.php/Api/Goods/goodsInfo?id=1
 */
public class SPProductListSearchResultActivity extends SPBaseActivity implements  ItemClickListener , SPProductFilterTabView.OnSortClickListener {

	private String TAG = "SPProductListSearchResultActivity";

	private static SPProductListSearchResultActivity instance;

	PtrClassicFrameLayout ptrClassicFrameLayout;
	ListView mListView;
	TextView syntheisTxtv ;
	TextView salenumTxtv ;
	TextView priceTxtv ;
	EditText searchText ;//搜索文本框
	ImageView backImgv;
	SPProductListAdapter mAdapter ;
	SPCategory mCategory ;	//分类

	SPProductFilterTabView mFilterTabView;

	DrawerLayout mDrawerLayout;

	int mPageIndex = 1 ;		//当前第几页
	boolean mIsMaxPage;			//是否最大页数
	SPShopOrder mShopOrder;		//排序实体
	String mHref ;				//请求URL
	String mSearchkey;			//搜索关键字
	List<SPProduct> mProducts ;
	SPProductListFilterFragment mFilterFragment;
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case SPMobileConstants.MSG_CODE_FILTER_CHANGE_ACTION:
					if (msg.obj != null){
						mHref = msg.obj.toString();
						refreshData();
					}
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle bundle) {
		//super.setCustomerTitle(true, true , "商品列表");
		super.onCreate(bundle);
		/** 自定义标题栏 , 执行顺序必须是一下顺序, 否则无效果.  */
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.product_list);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.product_list_header);
		/** 自定义标题栏  */
		Intent intent = getIntent();
		if(intent != null){
			mCategory = (SPCategory)intent.getSerializableExtra("category");
		}
		//过滤标题
		super.init();
		refreshData();
		instance = this;
	}

	public static SPProductListSearchResultActivity getInstance(){
		return instance;
	}
	
	@Override
	public void initSubViews() {

		WindowManager wm = (WindowManager)getBaseContext().getSystemService(Context.WINDOW_SERVICE);

		mFilterTabView = (SPProductFilterTabView)findViewById(R.id.filter_tabv);
		mFilterTabView.setOnSortClickListener(this);
		ptrClassicFrameLayout = (PtrClassicFrameLayout) this.findViewById(R.id.test_list_view_frame);
		mListView = (ListView)findViewById(R.id.pull_product_listv);
		
		//综合
		syntheisTxtv = (TextView)findViewById(R.id.sort_button_synthesis);
		salenumTxtv = (TextView)findViewById(R.id.sort_button_salenum);
		priceTxtv = (TextView)findViewById(R.id.sort_button_price);

		searchText = (EditText)findViewById(R.id.search_edtv);
		backImgv = (ImageView)findViewById(R.id.title_back_imgv);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

		SPMobileApplication.getInstance().productListType = 2;
		mFilterFragment = (SPProductListFilterFragment)getSupportFragmentManager().findFragmentById(R.id.right_rlayout);

		mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerStateChanged(int newState) {
				super.onDrawerStateChanged(newState);
			}
		});
	}




	@Override
	public void initData() {

		if (getIntent()!=null && getIntent().getStringExtra("searchKey") != null){
			mSearchkey = getIntent().getStringExtra("searchKey");
		}

		if(searchText!=null)searchText.setText(mSearchkey);


		mPageIndex = 1;
		mIsMaxPage = false;
		mAdapter = new SPProductListAdapter(this,this);
		mListView.setAdapter(mAdapter);

		ptrClassicFrameLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				//ptrClassicFrameLayout.autoRefresh(true);
			}
		}, 150);

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
				;
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

		mPageIndex = 1;
		mIsMaxPage = false;
		try {
			showLoadingToast("正在加载商品数据");
			SPShopRequest.searchResultProductListWithPage(mPageIndex, mSearchkey, mHref, new SPSuccessListener() {
				@Override
				public void onRespone(String msg, Object data) {
					hideLoadingToast();
					try {
						mDataJson = (JSONObject) data;
						if (mDataJson != null) {
							mProducts = (List<SPProduct>) mDataJson.get("product");
							mShopOrder = (SPShopOrder) mDataJson.get("order");

							if (mProducts != null) {
								mAdapter.setData(mProducts);
								mAdapter.notifyDataSetChanged();
							}
							if (SPProductListFilterFragment.getInstance(mHandler) != null) {
								SPProductListFilterFragment.getInstance(mHandler).setDataSource(mDataJson);
							}
							mIsMaxPage = false;
							ptrClassicFrameLayout.setLoadMoreEnable(true);
						} else {
							mIsMaxPage = true;
							ptrClassicFrameLayout.setLoadMoreEnable(false);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					refreshView();
					ptrClassicFrameLayout.refreshComplete();

				}
			}, new SPFailuredListener() {
				@Override
				public void onRespone(String msg, int errorCode) {
					hideLoadingToast();
					SPDialogUtils.showToast(SPProductListSearchResultActivity.this, msg);
					ptrClassicFrameLayout.refreshComplete();
					ptrClassicFrameLayout.setLoadMoreEnable(true);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 加载数据
	 * @Description: 加载数据
	 * @return void    返回类型
	 * @throws
	 */
	public void loadMoreData(){
		if (mIsMaxPage){
			return;
		}
		mPageIndex++;
		try {
			showLoadingToast("正在加载商品数据");
			SPShopRequest.searchResultProductListWithPage(mPageIndex, mSearchkey , mHref, new SPSuccessListener() {
				@Override
				public void onRespone(String msg, Object data) {
					hideLoadingToast();
					try {
						mDataJson = (JSONObject) data;
						if (mDataJson != null) {
							mShopOrder = (SPShopOrder) mDataJson.get("order");
							List<SPProduct> results = (List<SPProduct>) mDataJson.get("product");
							if (results != null && mProducts != null) {
								mProducts.addAll(results);
								mAdapter.setData(mProducts);
								mAdapter.notifyDataSetChanged();
							} else if (results == null) {
								ptrClassicFrameLayout.setLoadMoreEnable(false);
							}
							if (SPProductListFilterFragment.getInstance(mHandler) != null) {
								SPProductListFilterFragment.getInstance(mHandler).setDataSource(mDataJson);
							}
							mIsMaxPage = false;
							ptrClassicFrameLayout.setLoadMoreEnable(true);
						} else {
							mIsMaxPage = true;
							ptrClassicFrameLayout.setLoadMoreEnable(false);
						}
						refreshView();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, new SPFailuredListener() {
				@Override
				public void onRespone(String msg, int errorCode) {
					hideLoadingToast();
					SPDialogUtils.showToast(SPProductListSearchResultActivity.this, msg);
					ptrClassicFrameLayout.loadMoreComplete(true);
					mPageIndex--;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initEvent() {
		searchText.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SPProductListSearchResultActivity.this , SPSearchCommonActivity_.class);
				if (!SPStringUtils.isEmpty(mSearchkey)){
					intent.putExtra("searchKey" , mSearchkey);
				}
				SPProductListSearchResultActivity.this.startActivity(intent);
			}
		});
		backImgv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SPProductListSearchResultActivity.this.finish();
			}
		});
	}

	public void startupActivity(String goodsID){
		Intent intent = new Intent(this , SPProductDetailActivity_.class);
		intent.putExtra("goodsID", goodsID);
		startActivity(intent);
	}


	@Override
	public void onItemClickListener(SPProduct product) {
		startupActivity(product.getGoodsID());
	}

	@Override
	public void onFilterClick(SPProductFilterTabView.ProductSortType sortType) {

		switch (sortType){
			case composite:
				if (mShopOrder!=null){
					mHref = mShopOrder.getDefaultHref();
				}
				break;
			case salenum:
				if (mShopOrder!=null) {
					mHref = mShopOrder.getSaleSumHref();
				}
				break;
			case price:
				if (mShopOrder!=null) {
					mHref = mShopOrder.getPriceHref();
				}
				break;
			case filter:
				openRightFilterView();
				return;
		}
		refreshData();
	}

	public void openRightFilterView(){
		mDrawerLayout.openDrawer(Gravity.RIGHT);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
	}

	public void refreshView(){
		if (mShopOrder!=null && mShopOrder.getSortAsc()!=null) {
			if (mShopOrder.getSortAsc().equalsIgnoreCase("desc")) {
				mFilterTabView.setSort(true);
			}else{
				mFilterTabView.setSort(false);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		searchText.setFocusable(false);
		searchText.setFocusableInTouchMode(false);
	}
}
