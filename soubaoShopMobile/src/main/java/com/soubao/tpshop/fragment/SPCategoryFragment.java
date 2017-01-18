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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.shop.SPProductListActivity;
import com.soubao.tpshop.adapter.SPCategoryLeftAdapter;
import com.soubao.tpshop.adapter.SPCategoryRightAdapter;
import com.soubao.tpshop.dao.SPCategoryDao;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.category.SPCategoryRequest;
import com.soubao.tpshop.model.SPCategory;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.common.SPMobileConstants.CategoryLevel;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

/**
 *  首页 -> 分类 
 *
 */
public class SPCategoryFragment extends SPBaseFragment {

	private String TAG = "SPCategoryFragment";
	 
	
	/** 分类数据集合 */
	private List<SPCategory> mLeftCategorys ;
	private List<SPCategory> mRightCategorys ;
	private ListView mLeftLstv;						//左边大分类listview
	private StickyGridHeadersGridView mRightGdv;	//右边小分类listview

	SPCategory mLeftCategory;		//左边分类
	SPCategoryLeftAdapter mLeftAdapter ;
	SPCategoryRightAdapter mRightAdapter ;
	Button backBtn;
	TextView titleTxtv;


	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			CategoryLevel level = CategoryLevel.values()[msg.what];
			switch (level){
				case topLevel:
					mLeftCategorys = (List<SPCategory>)msg.obj;
					refreshTopCategory();
					break;
				case thirdLevel:
					mRightCategorys = (List<SPCategory>)msg.obj;
					thirdCategoryDateChange(mRightCategorys);
					break;
			}
		}
	};
	
	private HashMap<Integer , List<SPCategory>> mRightCategorysCache ;
	
	private Context mContext;
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRightCategorysCache = new HashMap<Integer, List<SPCategory>>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
	    View view = inflater.inflate(R.layout.category_fragment, null,false);
		super.init(view);

		return view;
	}
	
	public void refreshData(){

		mLeftCategorys = SPMobileApplication.getInstance().getTopCategorys();
		refreshTopCategory();

		if (mLeftCategorys == null || mLeftCategorys.size() < 1){
			SPCategoryRequest.getCategory(0 , new SPSuccessListener() {
				@Override
				public void onRespone(String msg, Object response) {
					if (response!=null){
						mLeftCategorys = (List<SPCategory>)response;
						SPMobileApplication.getInstance().setTopCategorys(mLeftCategorys);
						refreshTopCategory();
					}
					hideLoadingToast();
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

	private void refreshTopCategory(){
		if (SPCommonUtils.verifyArray(mLeftCategorys)) {
			mLeftAdapter.setSelectIndex(0);
			mLeftAdapter.setData(mLeftCategorys);
			mLeftAdapter.notifyDataSetChanged();
			mLeftCategory = mLeftCategorys.get(0);
			selectLeftCategory(mLeftCategory);
		}
	}
	
	
	/**
	 * 
	* @Description: 左边分类点击
	* @return void    返回类型
	* @throws
	 */
	public void selectLeftCategory(SPCategory category){
		if (category == null)return;
		mLeftCategory = category;
	   /** 先从缓存获取, 如果缓存中已经存在, 则不从服务器拉取数据 */
	   if(mRightCategorysCache != null && mRightCategorysCache.get(mLeftCategory.getId()) != null){
			List<SPCategory> cacheCategorys = mRightCategorysCache.get(mLeftCategory.getId());
			mRightAdapter.setData(cacheCategorys);
			mRightAdapter.notifyDataSetChanged();
			return ;
		}

		//showLoadingToast();
		SPCategoryRequest.goodsSecAndThirdCategoryList(category.getId(), new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {
				if (response != null ){
					mRightCategorys = (List<SPCategory>)response;
					thirdCategoryDateChange(mRightCategorys);
				}
				hideLoadingToast();
			}
		}, new SPFailuredListener() {
			@Override
			public void onRespone(String msg, int errorCode) {
				hideLoadingToast();
				showToast(msg);
			}
		});
	}

	public void thirdCategoryDateChange(List<SPCategory> thirdCategory){
		mRightCategorys = thirdCategory;
		if (mRightCategorys != null) {
			mRightAdapter.setData(mRightCategorys);
			/** 以category id 为key  ,  mRightCategorys 为值, 缓存每一个大分类的子分类 */
			mRightCategorysCache.put(mLeftCategory.getId(), mRightCategorys);
			mRightAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	/**
	 * 初始化View, 重写父类方法
	 */
	@Override
	public void initSubView(View view) {

		titleTxtv = (TextView)view.findViewById(R.id.titlebar_title_txtv);
		titleTxtv.setText(getString(R.string.title_category));

		backBtn = (Button)view.findViewById(R.id.titlebar_back_btn);
		backBtn.setVisibility(View.GONE);

		mLeftLstv = (ListView)view.findViewById(R.id.category_left_lstv);
		mRightGdv = (StickyGridHeadersGridView)view.findViewById(R.id.category_right_gdvv);
		mRightGdv.setAreHeadersSticky(false);
		mLeftAdapter = new SPCategoryLeftAdapter(getActivity());
		mRightAdapter = new SPCategoryRightAdapter(getActivity());
		
		mLeftLstv.setAdapter(mLeftAdapter);
		mRightGdv.setAdapter(mRightAdapter);
		
	}
	
	/**
	 * 事件绑定, 重写父类方法
	 */
	@Override
	public void initEvent() {

		mLeftLstv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				mLeftAdapter.setSelectIndex(position);
				mLeftAdapter.notifyDataSetChanged();
				mLeftCategory = (SPCategory)mLeftAdapter.getItem(position);
				selectLeftCategory(mLeftCategory);
			}
		});
		
		
		mRightGdv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SPCategory category = (SPCategory)mRightAdapter.getItem(position);
				if (category.isBlank()){
					return;
				}
				startupActivity(category);
			}
		});
	}

	@Override
	public void initData() {

		refreshData();
	}

	public void startupActivity(SPCategory category){
		Intent intent = new Intent(getActivity() , SPProductListActivity.class);
		intent.putExtra("category", category);
		getActivity().startActivity(intent);
	}


	class CategoryQueryTask extends AsyncTask<URL, Integer, List<SPCategory>> {

		public CategoryLevel level;
		public int parentId;

		public CategoryQueryTask(CategoryLevel level , int parentId){
			this.level = level;
			this.parentId = parentId;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(List<SPCategory> categories) {
			super.onPostExecute(categories);
			Message message = mHandler.obtainMessage(level.ordinal());
			message.obj = categories;
			mHandler.sendMessage(message);
		}

		@Override
		protected List<SPCategory> doInBackground(URL... params) {

			List<SPCategory> categorys = null;
			if (level == CategoryLevel.topLevel){
				categorys = SPCategoryDao.getInstance(getActivity()).queryCategoryByParentID(0);
			}else{
				//查询二级分类
				categorys = new ArrayList<SPCategory>();
				List<SPCategory> secondCategorys = SPCategoryDao.getInstance(getActivity()).queryCategoryByParentID(this.parentId);
				if (SPCommonUtils.verifyArray(secondCategorys)){
					for(SPCategory secondCategory : secondCategorys){
						//查询每个二级分类下的子分类
						List<SPCategory> thirdCategorys = SPCategoryDao.getInstance(getActivity()).queryCategoryByParentID(secondCategory.getId());
						if (SPCommonUtils.verifyArray(thirdCategorys)) {
							int size = thirdCategorys.size();
							for (SPCategory thirdCategory : thirdCategorys){
								thirdCategory.setParentCategory(secondCategory);
								thirdCategory.setIsBlank(false);
								//对每个子分类设置父分类后添加到集合中
								categorys.add(thirdCategory);
							}
							int sy = size % 3;
							int count = (sy == 0 ) ? 0 : (3 - sy);
							if (count >= 0){
								for(int i=0; i<count ; i++){
									SPCategory thirdCategory = new SPCategory();
									thirdCategory.setParentCategory(secondCategory);
									thirdCategory.setIsBlank(true);
									categorys.add(thirdCategory);
								}
							}
						}
					}
				}
			}
			return categorys;
		}
	}
}
