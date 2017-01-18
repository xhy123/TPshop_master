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
 * Date: @date 2015年10月20日 下午7:52:58 
 * Description:Activity 基类
 * @version V1.0
 */
package com.soubao.tpshop.activity.common;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.person.user.SPLoginActivity_;
import com.soubao.tpshop.activity.person.order.SPOrderDetailActivity_;
import com.soubao.tpshop.activity.person.order.SPOrderListActivity;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.utils.SPConfirmDialog;
import com.soubao.tpshop.utils.SPDialogUtils;
import com.soubao.tpshop.utils.SPLoadingDialog;
import com.soubao.tpshop.utils.SPStringUtils;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Field;

/**
 * @author 飞龙
 *
 */
public abstract class SPBaseActivity extends FragmentActivity implements SPIViewController {

	private String TAG = "SPBaseActivity";
	public final int TITLE_HOME = 1;
	public final int TITLE_DEFAULT = 0;
	public final int TITLE_CATEGORY = 2;

	public JSONObject mDataJson;		//包含网络请求所有结果
	public SPLoadingDialog mLoadingDialog;
	public boolean isCustomerTtitle ;	//是否自定义标题栏
	public boolean isBackShow ;			//是否显示返回箭头
	private String mTtitle ;				//标题栏

	private Button mBackBtn	;
	private TextView mTitleTxtv;

	FrameLayout mTitleBarLayout;
	FrameLayout mDefaultLayout;
	LinearLayout mHomeLayout;
	LinearLayout mCategoryLayout;
	SearchView homeSearch;
	SearchView categorySearch;
	RelativeLayout fragmentView;

	/**
	 * 是否自定义标题 , 该方法必须在子Activity的 super.onCreate()之前调用, 否则无效
	 * @param customerTtitle
	 */
	public void setCustomerTitle(boolean backShow, boolean customerTtitle , String title){
		isCustomerTtitle = customerTtitle;
		isBackShow = backShow;
		mTtitle = title;
	}

	public void setTitle(String title){
		mTtitle = title;
		if (mTitleTxtv!=null)mTitleTxtv.setText(mTtitle);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		if (isCustomerTtitle){
			//自定义标题
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		}
	}
	
	public void bindClickListener(View v , View.OnClickListener listener){
		if(v != null && listener != null){
			v.setOnClickListener(listener);
		}
		
	}
	
	/**
	 * activity初始化
	 * 
	 */
	public void init(){

		if (isCustomerTtitle){
			//设置标题为某个layout
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		}
		//mTitleBarLayout = (FrameLayout) findViewById(R.id.titlebar_layout);
		//mDefaultLayout = (FrameLayout) findViewById(R.id.titlebar_normal_layout);
		//mHomeLayout = (LinearLayout) findViewById(R.id.titlebar_home_layout);
		//mCategoryLayout = (LinearLayout) findViewById(R.id.titlebar_category_layout);
		//homeSearch =(SearchView) findViewById(R.id.titlebar_home_seach_view);
		//categorySearch =(SearchView) findViewById(R.id.titlebar_category_seach_view);
		//fragmentView = (RelativeLayout)findViewById(R.id.fragmentView);
		//View line = findViewById(R.id.titlebar_line);
		//if(line != null)line.requestFocus();
		mBackBtn = (Button)findViewById(R.id.titlebar_back_btn);
		if (isBackShow){
			mBackBtn.setVisibility(View.VISIBLE);
			mBackBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SPBaseActivity.this.finish();
				}
			});
		}else{
			if(mBackBtn!=null)mBackBtn.setVisibility(View.GONE);
		}

		String title = this.getTitle().toString();

		mTitleTxtv = (TextView)findViewById(R.id.titlebar_title_txtv);
		if(mTitleTxtv!=null)mTitleTxtv.setText(mTtitle);
		//setSearchViewStyle(homeSearch);
		//setSearchViewStyle(categorySearch);
		initSubViews();
		initEvent();
		initData();
	}

	public void setTitleType(int type){
		int padding = getResources().getDimensionPixelSize(R.dimen.height_tab_bottom_item);
		fragmentView.setPadding(0,padding-10,0,padding);
		mTitleBarLayout.setBackgroundResource(R.color.bg_activity);
		if(type == TITLE_HOME){
			mHomeLayout.setVisibility(View.VISIBLE);
			mCategoryLayout.setVisibility(View.INVISIBLE);
			mDefaultLayout.setVisibility(View.INVISIBLE);
			fragmentView.setPadding(0,0,0,padding);
			mTitleBarLayout.setBackgroundResource(R.color.transparent);
		}else if(type == TITLE_CATEGORY){
			mHomeLayout.setVisibility(View.INVISIBLE);
			mCategoryLayout.setVisibility(View.VISIBLE);
			mDefaultLayout.setVisibility(View.INVISIBLE);
		}else{
			mHomeLayout.setVisibility(View.INVISIBLE);
			mCategoryLayout.setVisibility(View.INVISIBLE);
			mDefaultLayout.setVisibility(View.VISIBLE);
		}
	}

	private void setSearchViewStyle(SearchView search){
		try {
			Field field = search.getClass().getDeclaredField("mSearchPlate");
			field.setAccessible(true);
			View tv = (View)field.get(search);
			tv.setBackgroundResource(R.color.transparent);

			Field field1 = search.getClass().getDeclaredField("mSearchEditFrame");
			field1.setAccessible(true);
			View tv1 = (View)field1.get(search);
			tv1.setBackgroundResource(R.color.transparentwhite);

			Field fieldimg = search.getClass().getDeclaredField("mCloseButton");
			fieldimg.setAccessible(true);
			ImageView image = (ImageView)fieldimg.get(search);
			image.setImageResource(R.drawable.icon_search_close);
			image.setBackgroundResource(R.color.transparent);

			Field[] fs = search.getClass().getDeclaredFields();
			for(int i = 0 ; i < fs.length; i++) {
				Field f = fs[i];
				f.setAccessible(true);
				Object val = f.get(search);
				System.out.println("name:"+f.getName()+"\t value = "+val);
			}
			Field mQueryTextView = search.getClass().getDeclaredField("mQueryTextView");
			mQueryTextView.setAccessible(true);
			TextView tvsearch = (TextView)mQueryTextView.get(search);
			tvsearch.setTextColor(0xffffffff);
			tvsearch.setHintTextColor(0xffffffff);
		} catch (Exception e) {
			try{
				Field mQueryTextView = search.getClass().getDeclaredField("mSearchSrcTextView");
				mQueryTextView.setAccessible(true);
				TextView tvsearch = (TextView)mQueryTextView.get(search);
				tvsearch.setTextColor(0xffffffff);
				tvsearch.setHintTextColor(0xffffffff);
			}catch (Exception ex){
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public void showToast(String msg){
		SPDialogUtils.showToast(this, msg);
	}

	public void showLoadingToast(){
		showLoadingToast(null);
	}

	public void showLoadingToast(String title){
		mLoadingDialog = new SPLoadingDialog(this , title);
		mLoadingDialog.setCanceledOnTouchOutside(false);
		mLoadingDialog.show();
	}

	public void showConfirmDialog(String message , String title , final SPConfirmDialog.ConfirmDialogListener confirmDialogListener , final int actionType){
		SPConfirmDialog.Builder builder = new SPConfirmDialog.Builder(this);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//设置你的操作事项
				if(confirmDialogListener!=null)confirmDialogListener.clickOk(actionType);
			}
		});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	public void hideLoadingToast(){
		if(mLoadingDialog !=null){
			mLoadingDialog.dismiss();
		}
	}


	public void showToastUnLogin(){
		showToast(getString(R.string.toast_person_unlogin));
	}

	public void  toLoginPage(){
		Intent loginIntent = new Intent(this , SPLoginActivity_.class);
		startActivity(loginIntent);
	}

	/**
	 * 进入产品详情页
	 * @param orderID
	 */
	public void gotoProductDetail(String orderID){
		Intent detailIntent = new Intent(this  , SPOrderDetailActivity_.class);
		detailIntent.putExtra("orderId", orderID);
		startActivity(detailIntent);
	}


	/**
	 * 启动web Activity
	 * @param url
	 */
	public void startWebViewActivity(String url , String title){
		Intent shippingIntent = new Intent(this , SPWebviewActivity_.class);
		shippingIntent.putExtra("url" ,url);
		shippingIntent.putExtra("title" , title);
		startActivity(shippingIntent);
	}

	/**
	 * 以下三个函数不需要再子类调用, 只需要在子类的
	 * onCrate()中调用:super.init()方法即可
	 * 基类函数,初始化界面
	 */
	abstract public void initSubViews();
	
	/**
	 * 基类函数, 初始化数据
	 */
	abstract public void initData();
	
	/**
	 * 基类函数, 绑定事件
	 */
	abstract public void initEvent();

	/**
	 * 处理网络加载过的数据
	 */
	public void dealModel(){}

	@Override
	public void gotoLoginPage() {
		/*if (!SPStringUtils.isEmpty(msg)){
			showToast(msg);
		}*/
		toLoginPage();
	}
}
