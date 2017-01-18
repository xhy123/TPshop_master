package com.soubao.tpshop.activity.person.address;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.common.SPBaseActivity;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.dao.SPPersonDao;
import com.soubao.tpshop.model.person.SPConsigneeAddress;
import com.soubao.tpshop.model.person.SPRegionModel;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPCityUtil;
import com.soubao.tpshop.utils.SPStringUtils;
import com.ta.utdid2.android.utils.StringUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.consignee_select_citiy)
public class SPCitySelectActivity extends SPBaseActivity {

	private String TAG = "SPCitySelectActivity";
	@ViewById(R.id.btn_right)
	Button btn_right;

	@ViewById(R.id.btn_back)
	Button btn_back;

	@ViewById(R.id.scrollview)
	HorizontalScrollView scrollview;

	@ViewById(R.id.lv_city)
	ListView lv_city;

	private List<SPRegionModel> regions;
	private SPConsigneeAddress consignee;
	private CityAdapter adapter;
	private SPCityUtil util;

	private TextView[] tvs = new TextView[4];
	private int[] ids = { R.id.rb_province, R.id.rb_city, R.id.rb_district , R.id.rb_town };//顶栏省市县
	int current; 	//0:省 ,1:市 ,2:县/区 ,3:镇/街道 ,
	int next; 		//-1 ,0:省 ,1:市 ,2:县/区 ,3:镇/街道 ,

	Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case SPPersonDao.LEVEL_PROVINCE :
				case SPPersonDao.LEVEL_CITY:
				case SPPersonDao.LEVEL_DISTRICT:
				case SPPersonDao.LEVEL_TOWN:
					System.out.println("镇/街道列表what======" + msg.what);
					regions = (List<SPRegionModel>) msg.obj;
					adapter.clear();
					adapter.addAll(regions);
					adapter.update();
					break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@AfterViews
	public void init(){
		super.init();
	}

	@Override
	public void initSubViews() {

	}

	@Override
	public void initData() {

		consignee = (SPConsigneeAddress)getIntent().getSerializableExtra("consignee");
		for (int i = 0; i < tvs.length; i++) {
			tvs[i] = (TextView) findViewById(ids[i]);
		}

		if(consignee == null){

			consignee = new SPConsigneeAddress();
			consignee.setProvinceLabel("");
			consignee.setCityLabel("");
			consignee.setDistrictLabel("");
			consignee.setTownLabel("");

		}else{

			String provinceLabel = SPPersonDao.getInstance(this).queryNameById(consignee.getProvince());
			String cityLabel = SPPersonDao.getInstance(this).queryNameById(consignee.getCity());
			String districtLabel = SPPersonDao.getInstance(this).queryNameById(consignee.getDistrict());
			String townLabel = SPPersonDao.getInstance(this).queryNameById(consignee.getTown());
			consignee.setProvinceLabel(provinceLabel);
			consignee.setCityLabel(cityLabel);
			consignee.setDistrictLabel(districtLabel);
			consignee.setTownLabel(townLabel);

			if(!StringUtils.isEmpty(consignee.getProvinceLabel())){
				tvs[0].setText(consignee.getProvinceLabel());
			}
			if(!StringUtils.isEmpty(consignee.getCityLabel())){
				tvs[1].setText(consignee.getCityLabel());
			}
			if(!StringUtils.isEmpty(consignee.getDistrictLabel())){
				tvs[2].setText(consignee.getDistrictLabel());
			}
			if(!StringUtils.isEmpty(consignee.getTownLabel())){
				tvs[3].setText(consignee.getTownLabel());
			}


		}

		current = 0 ;
		next = -1;
		setTextViewSelect();
		adapter = new CityAdapter(this);
		lv_city.setAdapter(adapter);

		util = new SPCityUtil(this ,mHandler);
		util.initProvince();
	}

	@Override
	public void initEvent() {
		lv_city.setOnItemClickListener(onItemClickListener);
	}

	protected void onStart() {
		super.onStart();
	}

	public void setTextViewSelect(){

		for (int i=0; i<tvs.length;i++){
			if (current == i){
				tvs[i].setBackgroundColor(Color.GRAY);
			}else{
				tvs[i].setBackgroundColor(Color.WHITE);
			}
		}
		if (current == 4 ){
			tvs[3].setBackgroundColor(Color.GRAY);
		}

	}

	@Click({R.id.rb_province ,R.id.rb_city ,R.id.rb_district ,R.id.rb_town , R.id.btn_back , R.id.btn_right })
	public void onViewClick(View v){
		switch (v.getId()) {
			case R.id.btn_back:
				finish();
				return;
			case R.id.btn_right:
				if (SPStringUtils.isEmpty(consignee.getProvince()) || SPStringUtils.isEmpty(consignee.getCity()) ||
				SPStringUtils.isEmpty(consignee.getDistrict()) || SPStringUtils.isEmpty(consignee.getTown())){
					showToast(getString(R.string.toast_address_nocompletion));
					return;
				}
				Intent resultIntent = new Intent();
				resultIntent.putExtra("consignee", consignee);
				setResult(SPMobileConstants.Result_Code_GetValue ,resultIntent);
				finish();
				return;
		}
		if (ids[0] == v.getId()) {//省份
			current = 0;
			consignee.setCity("");
			consignee.setDistrict("");
			consignee.setTown("");
			tvs[1].setText(R.string.city);
			tvs[2].setText(R.string.district);
			tvs[3].setText(R.string.town);
			util.initProvince();
			setTextViewSelect();
		} else if (ids[1] == v.getId()) {//城市
			if (StringUtils.isEmpty(consignee.getProvince())) {
				Toast.makeText(SPCitySelectActivity.this, getString(R.string.toast_no_select_province), Toast.LENGTH_SHORT).show();
				return;
			}

			consignee.setDistrict("");
			consignee.setTown("");

			tvs[2].setText(R.string.district);
			tvs[3].setText(R.string.town);
			current = 1;
			util.initChildrenRegion(consignee.getProvince(),SPPersonDao.LEVEL_CITY);
			setTextViewSelect();
		} else if (ids[2] == v.getId()) {//区县
			if (StringUtils.isEmpty(consignee.getProvince()) || StringUtils.isEmpty(consignee.getCity())) {
				current = SPPersonDao.LEVEL_PROVINCE;
				Toast.makeText(SPCitySelectActivity.this, getString(R.string.toast_no_preview_district), Toast.LENGTH_SHORT).show();
				return;
			}
			consignee.setTown("");
			consignee.setTownLabel("");
			tvs[3].setText(R.string.town);
			current = 2;
			setTextViewSelect();
			util.initChildrenRegion(consignee.getCity(), SPPersonDao.LEVEL_DISTRICT);
		} else if (ids[3] == v.getId()) {//镇/街道
			if (StringUtils.isEmpty(consignee.getProvince()) || StringUtils.isEmpty(consignee.getCity()) || StringUtils.isEmpty(consignee.getDistrict())) {
				current = 0;
				Toast.makeText(SPCitySelectActivity.this, getString(R.string.toast_no_preview_district), Toast.LENGTH_SHORT).show();
				return;
			}
			current = 3;
			setTextViewSelect();
		}
	}



	OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if (current == 0) {
				String newProvince = regions.get(position).getName();
				if (!newProvince.equals(consignee.getProvinceLabel())) {

					consignee.setProvinceLabel(newProvince);
					consignee.setProvince(regions.get(position).getRegionID());
					consignee.setCity("");
					consignee.setDistrict("");
					consignee.setTown("");

					tvs[0].setText(regions.get(position).getName());
					tvs[1].setText(R.string.city);
					tvs[2].setText(R.string.district);
					tvs[3].setText(R.string.town);
				}
				current = 1;
				//点击省份列表中的省份就初始化城市列表
				util.initChildrenRegion(consignee.getProvince() , SPPersonDao.LEVEL_CITY);
			} else if (current == 1) {
				String newCity = regions.get(position).getName();
				if (!newCity.equals(consignee.getCity())) {
					consignee.setCityLabel(newCity);
					consignee.setCity(regions.get(position).getRegionID());
					consignee.setDistrict("");
					consignee.setTown("");

					tvs[1].setText(regions.get(position).getName());
					tvs[2].setText(R.string.district);
					tvs[3].setText(R.string.town);
				}
				current = 2;
				util.initChildrenRegion(consignee.getCity(), SPPersonDao.LEVEL_DISTRICT);

			} else if (current == 2) {
				String newDistrict = regions.get(position).getName();
				if (!newDistrict.equals(consignee.getDistrict())) {
					consignee.setDistrictLabel(newDistrict);
					consignee.setDistrict(regions.get(position).getRegionID());
					consignee.setTown("");
					consignee.setTownLabel("");

					tvs[2].setText(regions.get(position).getName());
					tvs[3].setText(R.string.town);
				}
				current = 3;
				util.initChildrenRegion(consignee.getDistrict(), SPPersonDao.LEVEL_TOWN);

			}else if (current == 4 || current == 3) {
				consignee.setTown(regions.get(position).getRegionID());
				consignee.setTownLabel(regions.get(position).getName());
				tvs[3].setText(regions.get(position).getName());
				current = 4;
			}
			setTextViewSelect();
		}
	};

	class CityAdapter extends ArrayAdapter<SPRegionModel> {
		LayoutInflater inflater;
		public CityAdapter(Context con) {
			super(con, 0);
			inflater = LayoutInflater.from(SPCitySelectActivity.this);
		}

		@Override
		public View getView(int position, View v, ViewGroup arg2) {
			v = inflater.inflate(R.layout.consignee_select_citiy_item, null);
			TextView tv_city = (TextView) v.findViewById(R.id.tv_city);
			tv_city.setText(getItem(position).getName());
			return v;
		}

		public void update() {
			this.notifyDataSetChanged();
		}
	}


}
