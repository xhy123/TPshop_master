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
 * Date: @date 2015年10月23日 下午9:20:58 
 * Description: 新增或编辑收货地址
 * @version V1.0
 */
package com.soubao.tpshop.activity.person.address;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.common.SPBaseActivity;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.dao.SPPersonDao;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.person.SPPersonRequest;
import com.soubao.tpshop.model.person.SPConsigneeAddress;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPStringUtils;
import com.soubao.tpshop.view.SwitchButton;
import com.soubao.tpshop.view.SwitchButton.OnChangeListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * @author 飞龙
 *
 */
@EActivity(R.layout.consignee_address_edit)
public class SPConsigneeAddressEditActivity extends SPBaseActivity {

	private String TAG = "SPConsigneeAddressEditActivity";
	private SPConsigneeAddress consignee;
	private SPConsigneeAddress selectRegionConsignee;

	//收货人姓名
	@ViewById(R.id.consignee_name_edtv)
	EditText consigneeEdtv;

	//收货人电话
	@ViewById(R.id.consignee_mobile_edtv)
	EditText mobileEdtv;

	//收货地址
	@ViewById(R.id.consignee_region_txtv)
	TextView regionTxtv;

	//收货地址
	@ViewById(R.id.consignee_address_edtv)
	EditText addressEdtv;

	//默认地址开关
	@ViewById(R.id.consignee_setdefault_sth)
	SwitchButton setdefaultSth;

	String fullRegion;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.setCustomerTitle(true, true , getString(R.string.title_consignee));
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	public void init(){
		super.init();
	}

	@Override
	public void initSubViews() {
		setdefaultSth.setOnChangeListener(new OnChangeListener() {
			@Override
			public void onChange(SwitchButton sb, boolean state) {
				if (state) {
					consignee.setIsDefault("1");
				} else {
					consignee.setIsDefault("0");
				}
			}
		});
	}

	@Override
	public void initData() {

		if(getIntent()!=null && getIntent().getSerializableExtra("consignee") != null){
			consignee = (SPConsigneeAddress)getIntent().getSerializableExtra("consignee");
		}

		if (consignee == null){
			consignee = new SPConsigneeAddress();
			consignee.setIsDefault("0");
		}else{
			consigneeEdtv.setText(consignee.getConsignee());
			mobileEdtv.setText(consignee.getMobile());
			addressEdtv.setText(consignee.getAddress());
			if ("1".equals(consignee.getIsDefault())){
				setdefaultSth.setSwitchOn(true);
			}else{
				setdefaultSth.setSwitchOn(false);
			}
			String firstPart = SPPersonDao.getInstance(this).queryFirstRegion(consignee.getProvince() , consignee.getCity() , consignee.getDistrict() , consignee.getTown());
			regionTxtv.setText(firstPart);
		}
	}

	@Override
	public void initEvent() {

	}

	@Click({R.id.submit_btn , R.id.consignee_region_txtv})
	public void onViewClick(View view){

		if (view.getId() == R.id.submit_btn){
			if (!checkData()){
				return;
			}


			RequestParams params = new RequestParams();
			params.put("consignee" , consignee.getConsignee());
			params.put("province" , consignee.getProvince());
			params.put("city" , consignee.getCity());

			params.put("district" , consignee.getDistrict());
			params.put("street" , consignee.getTown());
			params.put("address" , consignee.getAddress());
			params.put("mobile" , consignee.getMobile());
			params.put("is_default" , consignee.getIsDefault());

			if (!SPStringUtils.isEmpty(consignee.getAddressID())) {//编辑
				params.put("address_id" , consignee.getAddressID());
			}
			showLoadingToast("正在保存数据");
			SPPersonRequest.saveUserAddressWithParameter(params, new SPSuccessListener() {
				@Override
				public void onRespone(String msg, Object response) {
					hideLoadingToast();
					showToast(msg);
					setResult(SPMobileConstants.Result_Code_Refresh);
					SPConsigneeAddressEditActivity.this.finish();
				}
			}, new SPFailuredListener(SPConsigneeAddressEditActivity.this) {
				@Override
				public void onRespone(String msg, int errorCode) {
					hideLoadingToast();
					showToast(msg);
				}
			});

		}else if (view.getId() == R.id.consignee_region_txtv){
			Intent regionIntent = new Intent(this, SPCitySelectActivity_.class);
			regionIntent.putExtra("consignee" , consignee);
			startActivityForResult(regionIntent , SPMobileConstants.Result_Code_GetValue);
		}
	}

	private boolean checkData(){
		if (SPStringUtils.isEmpty(consigneeEdtv.getText().toString())){
			showToast("请输入收货人");
			return false;
		}
		consignee.setConsignee(consigneeEdtv.getText().toString());

		if (SPStringUtils.isEmpty(mobileEdtv.getText().toString())){
			showToast("请输入联系方式");
			return false;
		}
		consignee.setMobile(mobileEdtv.getText().toString());

		if (SPStringUtils.isEmpty(addressEdtv.getText().toString())){
			showToast("请输入详细地址");
			return false;
		}
		consignee.setAddress(addressEdtv.getText().toString());

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == SPMobileConstants.Result_Code_GetValue){
			if (data==null || data.getSerializableExtra("consignee") == null){
				return;
			}
			selectRegionConsignee = (SPConsigneeAddress)data.getSerializableExtra("consignee");
			fullRegion = selectRegionConsignee.getProvinceLabel() + selectRegionConsignee.getCityLabel() + selectRegionConsignee.getDistrictLabel() + selectRegionConsignee.getTownLabel();
			regionTxtv.setText(fullRegion);

			consignee.setProvince(selectRegionConsignee.getProvince());
			consignee.setCity(selectRegionConsignee.getCity());
			consignee.setDistrict(selectRegionConsignee.getDistrict());
			consignee.setTown(selectRegionConsignee.getTown());

			SMobileLog.i(TAG , " province : "+selectRegionConsignee.getProvinceLabel() + " "+selectRegionConsignee.getCityLabel() +" "+selectRegionConsignee.getCityLabel() +" "+selectRegionConsignee.getTownLabel());
		}
	}
}
