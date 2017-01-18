/**
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2127 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015-10-15 20:32:41
 * Description: 商城主界面Activity (底部包含四个tab item)
 * @version V1.0
 */
package com.soubao.tpshop;

import com.soubao.tpshop.activity.common.SPBaseActivity;


import com.soubao.tpshop.common.SPDataAsyncManager;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.dao.SPPersonDao;
import com.soubao.tpshop.fragment.SPBaseFragment;
import com.soubao.tpshop.fragment.SPCategoryFragment;
import com.soubao.tpshop.fragment.SPHome2Fragment;
//import com.soubao.tpshop.fragment.SPHomeFragment;
import com.soubao.tpshop.fragment.SPPersonFragment;
import com.soubao.tpshop.fragment.SPShopCartFragment;
import com.soubao.tpshop.global.SPSaveData;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.shop.SPShopRequest;
import com.soubao.tpshop.model.person.SPRegionModel;

import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPDialogUtils;
import com.soubao.tpshop.utils.SPMyFileTool;
import com.soubao.tpshop.utils.SPStringUtils;
import com.soubao.tpshop.utils.SPUtils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.net.URL;
import java.util.List;

import it.sauronsoftware.base64.Base64;

public class SPMainActivity extends SPBaseActivity {
	
	private String TAG = "SPMainActivity";

	List<SPRegionModel> mRegionModels;

	public static final String SELECT_INDEX = "selectIndex";
	public static final String CACHE_SELECT_INDEX = "cacheSelectIndex";
	public static final int INDEX_HOME = 0;
	public static final int INDEX_CATEGORY = 1;
	public static final int INDEX_SHOPCART = 2;
	public static final int INDEX_PERSON = 3;

	public int mCurrentSelectIndex  ;

	FragmentManager mFragmentManager ;
	SPHome2Fragment mHomeFragment ;
	SPCategoryFragment mCategoryFragment ;
	SPShopCartFragment mShopCartFragment ;
	SPPersonFragment mPersonFragment ;
	
	RadioGroup mRadioGroup;
	RadioButton rbtnHome;
	RadioButton rbtnCategory;
	RadioButton rbtnShopcart;
	RadioButton rbtnPerson;
	
	RadioButton mCurrRb;
	RadioButton mLastRb;

	private static SPMainActivity mInstance;

	public Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case SPMobileConstants.MSG_CODE_LOAD_DATAE_CHANGE:
					if (msg.obj !=null){
						mRegionModels = (List<SPRegionModel>)msg.obj ;
						SaveAddressTask task = new SaveAddressTask();
						task.execute();
					}
					break;
				case SPMobileConstants.MSG_CODE_SHOW:
					if (msg.obj!=null){
						SPDialogUtils.showToast(SPMainActivity.this , msg.obj.toString());
					}
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.setCustomerTitle(false, false, getString(R.string.title_home));
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mFragmentManager = this.getSupportFragmentManager();
		super.init();
		addFragment();
		hiddenFragment();

		if (savedInstanceState!=null){
			mCurrentSelectIndex = savedInstanceState.getInt(CACHE_SELECT_INDEX , INDEX_HOME);
		}else{
			mCurrentSelectIndex = INDEX_HOME;
		}
		setSelectIndex(mCurrentSelectIndex);

		mInstance = this;
	}

	@Override
	public void initSubViews() {
		mHomeFragment = new SPHome2Fragment();
		mCategoryFragment = new SPCategoryFragment();
		mShopCartFragment = new SPShopCartFragment();
		mPersonFragment = new SPPersonFragment();
		mHomeFragment.setMainActivity(this);

		mRadioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
		rbtnHome = (RadioButton) this.findViewById(R.id.rbtn_home);
		rbtnCategory = (RadioButton) this.findViewById(R.id.rbtn_category);
		rbtnShopcart = (RadioButton) this.findViewById(R.id.rbtn_shopcart);
		rbtnPerson = (RadioButton) this.findViewById(R.id.rbtn_mine);
	}

	@Override
	public void initData() {
		//同步数据
		SPDataAsyncManager.getInstance(this , mHandler).startSyncData(new SPDataAsyncManager.SyncListener() {
			@Override
			public void onPreLoad() {

			}

			@Override
			public void onLoading() {

			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(String error) {

			}
		});

	}

	@Override
	public void initEvent() {
		///Log.i(TAG, "initEvent...");
		mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int key) {
				switch (key) {
					case R.id.rbtn_home:
						setSelectIndex(INDEX_HOME);
						break;
					case R.id.rbtn_category:
						setSelectIndex(INDEX_CATEGORY);
						break;
					case R.id.rbtn_shopcart:
						setSelectIndex(INDEX_SHOPCART);
						break;
					case R.id.rbtn_mine:
						setSelectIndex(INDEX_PERSON);
						break;
					default:
						break;
				}
			}
		});
	}

	public void setSelectIndex(int index){
		switch (index){
			case INDEX_HOME:
				//setTitleType(TITLE_HOME);
				showFragment(mHomeFragment);
				changeTabtextSelector(rbtnHome);
				setTitle(getString(R.string.title_home));
				mCurrentSelectIndex = INDEX_HOME;
				break;
			case INDEX_CATEGORY:
				//setTitleType(TITLE_CATEGORY);
				showFragment(mCategoryFragment);
				changeTabtextSelector(rbtnCategory);
				setTitle(getString(R.string.tab_item_category));
				mCurrentSelectIndex = INDEX_CATEGORY;
				break;
			case INDEX_SHOPCART:
				//setTitleType(TITLE_DEFAULT);
				showFragment(mShopCartFragment);
				changeTabtextSelector(rbtnShopcart);
				setTitle(getString(R.string.tab_item_shopcart));
				mCurrentSelectIndex = INDEX_SHOPCART;
				break;
			case INDEX_PERSON:
				//setTitleType(TITLE_DEFAULT);
				showFragment(mPersonFragment);
				changeTabtextSelector(rbtnPerson);
				setTitle(getString(R.string.tab_item_mine));
				mCurrentSelectIndex = INDEX_PERSON;
				break;
		}
	}


	/**
	 * 
	 * @Title: showFragment
	 * @Description: 
	 * @param: @param fragment
	 * @return: void
	 * @throws
	 */
	private void showFragment(SPBaseFragment fragment) {
		hiddenFragment();
		FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
		mTransaction.show(fragment);
		mTransaction.commitAllowingStateLoss();

	}
	//add by zzx
	public void setShowFragment(int flag){
		if(flag == SPHome2Fragment.CATEGORY_FRAGMENT){
			//setTitleType(TITLE_CATEGORY);
			showFragment(mCategoryFragment);
			changeTabtextSelector(rbtnCategory);
			setTitle(getString(R.string.tab_item_category));
			rbtnCategory.setChecked(true);
		}else if(flag == SPHome2Fragment.SHOPCART_FRAGMENT){
			//setTitleType(TITLE_DEFAULT);
			showFragment(mShopCartFragment);
			changeTabtextSelector(rbtnShopcart);
			setTitle(getString(R.string.tab_item_shopcart));
			rbtnShopcart.setChecked(true);
		}
	}

	/**
	 * 
	 * @Title: hiddenFragment
	 * @Description:
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void hiddenFragment() {
		FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
		mTransaction.hide(mHomeFragment);
		mTransaction.hide(mCategoryFragment);
		mTransaction.hide(mShopCartFragment);
		mTransaction.hide(mPersonFragment);
		mTransaction.commitAllowingStateLoss();
	}
	
	/**
	 * 
	 * @Title: addFragment
	 * @Description:
	 * @param:
	 * @return: void
	 * @throws
	 */

	private void addFragment() {

		FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
		mTransaction.add(R.id.fragmentView, mHomeFragment);
		mTransaction.add(R.id.fragmentView, mCategoryFragment);
		mTransaction.add(R.id.fragmentView, mShopCartFragment);
		mTransaction.add(R.id.fragmentView, mPersonFragment);
		mTransaction.commitAllowingStateLoss();
	}
	
	public void changeTabtextSelector(RadioButton rb){
		
		mLastRb = mCurrRb;
		mCurrRb = rb;
		
		if(mLastRb != null){
			mLastRb.setTextColor(getResources().getColor(R.color.color_tab_item_normal));
			mLastRb.setSelected(false);
		}
		
		if(mCurrRb != null){
			mCurrRb.setTextColor(getResources().getColor(R.color.color_tab_item_fous));
			mCurrRb.setChecked(true);
		}
	}

	private class SaveAddressTask extends AsyncTask<URL, Integer, Long>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Long aLong) {
			super.onPostExecute(aLong);

		}

		@Override
		protected Long doInBackground(URL... params) {


			SPPersonDao personDao = SPPersonDao.getInstance(SPMainActivity.this);
			personDao.insertRegionList(mRegionModels);
			SPSaveData.putValue(SPMainActivity.this, SPMobileConstants.KEY_IS_FIRST_STARTUP, false);

			return null;

		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		//Log.d(TAG, "onRestart.." +getIntent().hasExtra(SELECT_INDEX));
		int selectIndex = -1;
		if (getIntent()!=null && getIntent().hasExtra(SELECT_INDEX)){
			selectIndex = getIntent().getIntExtra(SELECT_INDEX, -1);
			//Log.d(TAG, "onRestart , selectIndex : " + selectIndex );
			if (selectIndex!= -1)setSelectIndex(selectIndex);
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		SPShopRequest.refreshServiceTime(new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {

			}
		}, new SPFailuredListener() {
			@Override
			public void onRespone(String msg, int errorCode) {

			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(CACHE_SELECT_INDEX, mCurrentSelectIndex);
	}

	@Override
	public void onBackPressed() {
		if(mCurrRb == rbtnHome ){
			super.onBackPressed();
		}else{
			setSelectIndex(INDEX_HOME);
		}
	}

	public static SPMainActivity getmInstance(){
		return mInstance;
	}

}
