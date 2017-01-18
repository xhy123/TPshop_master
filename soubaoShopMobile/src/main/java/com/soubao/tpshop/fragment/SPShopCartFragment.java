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
 * Description:购物车Fragment
 * @version V1.0
 */
package com.soubao.tpshop.fragment;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.soubao.tpshop.R;
import com.soubao.tpshop.SPMainActivity;
import com.soubao.tpshop.activity.shop.SPConfirmOrderActivity_;
import com.soubao.tpshop.activity.shop.SPProductDetailActivity_;
import com.soubao.tpshop.adapter.SPShopcartListAdapter;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.global.SPShopCartManager;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.shop.SPShopRequest;
import com.soubao.tpshop.model.SPProduct;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPConfirmDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;


/**
 *  首页 -> 购物车
 *
 */
public class SPShopCartFragment extends SPBaseFragment implements View.OnClickListener , SPShopcartListAdapter.ShopCarClickListener , SPConfirmDialog.ConfirmDialogListener{

	private String TAG = "SPShopCartFragment";
	private Context mContext;

	private ListView shopcartListv;
	private TextView totalfeeTxtv;
	private TextView cutfeeTxtv;
	private Button checkallBtn;
	private Button buyBtn;
	Button backBtn;
	private TextView titleTxtv;

	private PtrClassicFrameLayout shopcartPcf;
	private List<SPProduct> products;
	private SPProduct currentProduct;
	private JSONArray formDataArray;
	private SPShopcartListAdapter mAdapter;

	private double totalFee ;
	private double cutFee ;
	boolean isAllCheck ;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.shopcart_fragment, null,false);

		super.init(view);

		//refreshData();

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	@Override
	public void onDetach() {
		super.onDetach();
	}


	@Override
	public void initSubView(View view) {
		shopcartListv = (ListView)view.findViewById(R.id.shopcart_listv);
		shopcartPcf = (PtrClassicFrameLayout)view.findViewById(R.id.shopcart_pcf);
		backBtn = (Button)view.findViewById(R.id.titlebar_back_btn);
		backBtn.setVisibility(View.GONE);

		titleTxtv = (TextView)view.findViewById(R.id.titlebar_title_txtv);
		titleTxtv.setText(getString(R.string.title_shopcart));

		totalfeeTxtv = (TextView)view.findViewById(R.id.totalfee_txtv); ;
		cutfeeTxtv = (TextView)view.findViewById(R.id.cutfee_txtv); ;
		checkallBtn = (Button)view.findViewById(R.id.checkall_btn); ;
		buyBtn = (Button)view.findViewById(R.id.buy_btn);
		View emptyView = view.findViewById(R.id.empty_lstv);
		shopcartListv.setEmptyView(emptyView);
	}

	@Override
	public void initEvent() {
		checkallBtn.setOnClickListener(this);
		buyBtn.setOnClickListener(this);
		shopcartListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SPProduct product = (SPProduct)mAdapter.getItem(position);
				Intent intent = new Intent(getActivity() , SPProductDetailActivity_.class);
				intent.putExtra("goodsID", product.getGoodsID());
				getActivity().startActivity(intent);
			}
		});

		shopcartPcf.setPtrHandler(new PtrDefaultHandler() {
			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				//下拉刷新
				refreshData();
			}
		});
		formDataArray = new JSONArray();
		mAdapter = new SPShopcartListAdapter(getActivity() , this);
		shopcartListv.setAdapter(mAdapter);
		isAllCheck = false;
		refreshData();

	}

	@Override
	public void initData() {

	}

	public void refreshData(){
		SPMainActivity mainActivity = (SPMainActivity)getActivity();
		//showLoadingToast();
		SPShopRequest.getShopCartList(formDataArray, new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {

				hideLoadingToast();
				shopcartPcf.refreshComplete();
				shopcartPcf.setLoadMoreEnable(false);
				mDataJson = (JSONObject) response;
				try {
					if (mDataJson.has("products")) {
						products = (List<SPProduct>) mDataJson.get("products");
						mAdapter.setData(products);
						dealModels(products);
					}
				} catch (Exception e) {
					showToast(e.getMessage());
				}


			}
		}, new SPFailuredListener() {
			@Override
			public void onRespone(String msg, int errorCode) {
				hideLoadingToast();
				shopcartPcf.refreshComplete();
				shopcartPcf.setLoadMoreEnable(false);
			}
		});
	}

	/**
	 *  将每一个购物车商品生产对应的表单数据缓存起来, 以便表单提交试用
	 *
	 */
	public void dealModels(List<SPProduct> products){

		formDataArray = new JSONArray();
		isAllCheck = true;
		if (products == null || products.size() < 1) {
			totalFee = 0.00;
			cutFee = 0.00;
			refreshFeeView();;
			checkallBtn.setBackgroundResource(R.drawable.icon_checkno);
			return;
		}
		try {
			for (SPProduct  product : products) {
				JSONObject formJson = new JSONObject();
				formJson.put("cartID", product.getCartID());
				formJson.put("goodsNum", product.getGoodsNum());
				formJson.put("selected", product.getSelected());
				formJson.put("storeCount", product.getStoreCount());

				if ("0".equals(product.getSelected())) {
					isAllCheck = false;
				}
				formDataArray.put(formJson);
			}

			//设置全选状态
			if(isAllCheck){
				checkallBtn.setBackgroundResource(R.drawable.icon_checked);
			}else{
				checkallBtn.setBackgroundResource(R.drawable.icon_checkno);
			}
			if (mDataJson.has("totalFee")){
				totalFee = mDataJson.getDouble("totalFee");
			}
			if (mDataJson.has("cutFee")){
				cutFee = mDataJson.getDouble("cutFee");
			}
			refreshFeeView();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 刷新商品总价
	 */
	public void refreshFeeView(){

		String totalFeeFmt = "合计:¥"+totalFee;
		String cutFeeFmt = "共节省:¥"+cutFee;

		int startIndex = 3;
		int endIndex = totalFeeFmt.length() ;
		SpannableString totalFeeSpanStr = new SpannableString(totalFeeFmt);
		totalFeeSpanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色

		totalfeeTxtv.setText(totalFeeSpanStr);
		cutfeeTxtv.setText(cutFeeFmt);

		if (isAllCheck){
			checkallBtn.setBackgroundResource(R.drawable.icon_checked);
		}else{
			checkallBtn.setBackgroundResource(R.drawable.icon_checkno);
		}

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.checkall_btn){
			//全选或全不选
			checkAllOrNo();
		}else if(v.getId() == R.id.buy_btn){
			if (!SPMobileApplication.getInstance().isLogined){
				showToastUnLogin();
				toLoginPage();
				return;
			}

			Intent confirmIntent = new Intent(getActivity() , SPConfirmOrderActivity_.class);
			getActivity().startActivity(confirmIntent);
		}
	}



	@Override
	public void minuProductFromCart(SPProduct product) {
		try {
			boolean isReload = true;
			//修改商品信息, 减少商品数量
			int length = formDataArray.length();
			for (int i=0;i<length ; i++){
				JSONObject cartJson =  formDataArray.getJSONObject(i);
				String tempID = cartJson.getString("cartID");
				if (tempID.equals(product.getCartID())){
					Integer goodsNum = cartJson.getInt("goodsNum");
					if (goodsNum > 1) {
						goodsNum = goodsNum - 1;
						cartJson.put("goodsNum" ,goodsNum);
					}else{
						isReload = false;
					}
					break;
				}
			}
			if (isReload) {
				refreshData();
			}else{
				showToast("不能再减了");
			}
		} catch (Exception e) {
			e.printStackTrace();
			showToast(e.getMessage());
		}
	}

	@Override
	public void plusProductFromCart(SPProduct product) {
		try {

			boolean isReload = true;
			//修改商品信息, 减少商品数量
			int length = formDataArray.length();
			for (int i=0;i<length ; i++){
				JSONObject cartJson =  formDataArray.getJSONObject(i);
				String tempID = cartJson.getString("cartID");
				if (tempID.equals(product.getCartID())){
					Integer goodsNum = cartJson.getInt("goodsNum");
					Integer storeCount =  cartJson.getInt("storeCount");
					if ((goodsNum+1)<=storeCount) {
						goodsNum = goodsNum + 1;
						cartJson.put("goodsNum" , goodsNum);
					}else{
						//库存不足
						showToast("库存不足,无法继续添加");
						isReload = false;
					}
					break;
				}
			}
			if (isReload) {
				refreshData();
			}
		}catch (Exception e){
			showToast(e.getMessage());
		}
	}

	@Override
	public void checkProductFromCart(SPProduct product, boolean checked) {
		try {
			String selected = checked ? "1" : "0";
			int length = formDataArray.length();
			for (int i=0;i<length ; i++) {
				JSONObject cartJson = formDataArray.getJSONObject(i);
				String tempID = cartJson.getString("cartID");
				if (tempID.equals(product.getCartID())) {
					cartJson.put("selected" , selected);
					break;
				}
			}
			refreshData();
		} catch (Exception e) {
			e.printStackTrace();
			showToast(e.getMessage());
		}
	}

	@Override
	public void deleteProductFromCart(SPProduct product) {
		currentProduct = product;
		showConfirmDialog("确定删除该商品" , "删除提醒" , this  , 1);
	}



	public void checkAllOrNo(){
		boolean needCheckAll = false;//是否需要全选
		try {
			//1. 判断是否需要全选
			int length = formDataArray.length();
			for (int i=0;i< length ; i++) {
				JSONObject cartJson = formDataArray.getJSONObject(i);
				if (cartJson.getString("selected").equals("0")) {
					needCheckAll = true;
					break;
				}
			}
			//2. 全选或反选
			String selected = needCheckAll ? "1": "0";
			int length2 = formDataArray.length();
			for (int j=0;j< length2 ; j++) {
				JSONObject cartJson2 = formDataArray.getJSONObject(j);
				cartJson2.put("selected" , selected);
			}
			refreshData();
		} catch (Exception e) {
			e.printStackTrace();
			showToast(e.getMessage());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
		refreshData();
	}

	/**
	 *  从购物车删除商品
	 *
	 *  @param  cartID description
	 */
	public void deleteProductFromCart(){
		showToast("正在删除");
		SPShopRequest.deleteShopCartProductWithIds(currentProduct.getCartID(), new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {
				int count = Integer.valueOf(response.toString().toString());
				SPShopCartManager.getInstance(getActivity()).setShopCount(count);
				hideLoadingToast();
				showToast(msg);
				refreshData();
			}
		}, new SPFailuredListener() {
			@Override
			public void onRespone(String msg, int errorCode) {
				hideLoadingToast();
				showToast(msg);
			}
		});
	}

	@Override
	public void clickOk(int actionType) {
		if (actionType == 1){
			deleteProductFromCart();
		}
	}
}
