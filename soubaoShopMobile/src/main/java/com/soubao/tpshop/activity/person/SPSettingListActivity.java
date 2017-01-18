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
 * Description: 设置列表
 * @version V1.0
 */
package com.soubao.tpshop.activity.person;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.common.SPBaseActivity;
import com.soubao.tpshop.activity.shop.SPProductDetailActivity_;
import com.soubao.tpshop.adapter.SPCollectListAdapter;
import com.soubao.tpshop.adapter.SPSettingListAdapter;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.person.SPPersonRequest;
import com.soubao.tpshop.model.shop.SPCollect;
import com.soubao.tpshop.utils.SMobileLog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 飞龙
 *
 */
@EActivity(R.layout.person_setting_list)
public class SPSettingListActivity extends SPBaseActivity {

	private String TAG = "SPSettingListActivity";

	@ViewById(R.id.setting_listv)
	ListView settingListv;

	@ViewById(R.id.exit_btn)
	Button exitBtn;

	SPSettingListAdapter mAdapter ;
	List<String> mTexts ;

	@Override
	protected void onCreate(Bundle bundle) {
		super.setCustomerTitle(true, true , getString(R.string.settings));
		super.onCreate(bundle);}


	@AfterViews
	public void init(){
		super.init();
	}
	
	@Override
	public void initSubViews() {

	}

	@Override
	public void initData() {

		mAdapter = new SPSettingListAdapter(this);
		settingListv.setAdapter(mAdapter);

		mTexts = new ArrayList<String>();
		mTexts.add("客服电话:0755-86140485");
		mTexts.add("触屏版");

		mAdapter.setData(mTexts);

		if (SPMobileApplication.getInstance().isLogined){
			this.exitBtn.setEnabled(true);
			this.exitBtn.setBackgroundResource(R.drawable.button_selector);
		}else{
			this.exitBtn.setEnabled(true);
			this.exitBtn.setBackgroundResource(R.drawable.button_gray_selector);
		}

	}

	@Override
	public void initEvent() {
		settingListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
					//联系客服
					Intent intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:" + SPMobileConstants.MOBILE_CUSTOMER));
					if (intent.resolveActivity(getPackageManager()) != null) {
						startActivity(intent);
					}
				} else if (position == 1) {
					//
					startWebViewActivity(SPMobileConstants.WAP_URL, "触屏版");
				}
			}
		});
	}


	@Click({R.id.exit_btn})
	public void onViewClick(View v){

		if (v.getId() == R.id.exit_btn){
			SPMobileApplication.getInstance().exitLogin();
			this.sendBroadcast(new Intent(SPMobileConstants.ACTION_LOGIN_CHNAGE));
			this.exitBtn.setEnabled(false);
			this.exitBtn.setBackgroundResource(R.drawable.button_gray_selector);
			showToast("安全退出");
		}
	}



}
