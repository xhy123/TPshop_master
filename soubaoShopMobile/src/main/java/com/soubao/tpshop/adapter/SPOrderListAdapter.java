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
 * Date: @date 2015年10月30日 下午10:03:56 
 * Description: 我的 -> 订单列表 -> 订单Adapter
 * @version V1.0
 */
package com.soubao.tpshop.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.R;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.model.OrderButtonModel;
import com.soubao.tpshop.model.SPProduct;
import com.soubao.tpshop.model.order.SPOrder;
import com.soubao.tpshop.utils.SMobileLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 *
 */
public class SPOrderListAdapter extends BaseAdapter implements View.OnClickListener {

	private String TAG = "SPOrderListAdapter";

	private List<SPOrder> mOrders ;
	private Context mContext ;
	private Handler mHandler;
	private int selectIndex ;

	public SPOrderListAdapter(Context context , Handler handler){
		this.mContext = context;
		this.mHandler = handler;
	}
	

	public void setData(List<SPOrder> orders){
		if(orders == null)return;
		this.mOrders = orders;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mOrders == null)return 0;
		return mOrders.size();
	}

	@Override
	public Object getItem(int position) {
		if(mOrders == null) return null;
		return mOrders.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mOrders == null) return -1;
		return Long.valueOf(mOrders.get(position).getOrderID());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
			//使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.order_list_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();
			holder.orderProductScrollv = ((HorizontalScrollView) convertView.findViewById(R.id.order_product_scrollv)) ;
			holder.productListGalleryLyaout = (LinearLayout) convertView.findViewById(R.id.product_list_gallery_lyaout);

			holder.orderButtonScrollv = ((HorizontalScrollView) convertView.findViewById(R.id.order_button_scrollv)) ;
			holder.orderButtonGalleryLyaout = (LinearLayout) convertView.findViewById(R.id.order_button_gallery_lyaout);

			holder.orderProductRlayout = ((RelativeLayout) convertView.findViewById(R.id.order_product_rlayout)) ;
			holder.orderPicImgv = (ImageView) convertView.findViewById(R.id.order_pic_imgv);
			holder.orderProductNameTxtv = (TextView) convertView.findViewById(R.id.order_product_name_txtv);
			holder.orderProductDetailTxtv = (TextView) convertView.findViewById(R.id.order_product_detail_txtv);
			holder.orderButtonLayout = (LinearLayout) convertView.findViewById(R.id.order_button_layout);
			//设置标记
			convertView.setTag(holder);
        }else{
      	  	holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
		SPOrder order = mOrders.get(position);
		String orderAmout = order.getOrderAmount();

		int count = 0;
		if (order.getProducts()!=null && order.getProducts().size() > 1 ){
			//多个商品
			buildProductPreviewGallery(holder.productListGalleryLyaout, order.getProducts() , position);
			count = order.getProducts().size();
			holder.orderProductScrollv.setVisibility(View.VISIBLE);
			holder.orderProductRlayout.setVisibility(View.GONE);
		}else{
			//单个商品
			if (order.getProducts() != null && order.getProducts().size() == 1){
				SPProduct product = order.getProducts().get(0);
				holder.orderProductNameTxtv.setText(product.getGoodsName());
				String url = product.getImageThumlUrl();
				Glide.with(mContext).load(url).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.orderPicImgv);

				count = 1 ;
			}
			holder.orderProductScrollv.setVisibility(View.GONE);
			holder.orderProductRlayout.setVisibility(View.VISIBLE);
		}

		holder.orderProductRlayout.setOnClickListener(orderButtonClickListener);
		holder.orderProductRlayout.setTag(position);
		holder.orderButtonScrollv.setOnClickListener(orderButtonClickListener);
		holder.orderButtonScrollv.setTag(position);
		holder.orderProductScrollv.setOnClickListener(orderButtonClickListener);
		holder.orderProductScrollv.setTag(position);

		List<OrderButtonModel> buttonModels = getOrderButtonModelByOrder(order);
		buildProductButtonGallery(holder.orderButtonGalleryLyaout, buttonModels , position);

		String goodsInfo = "共"+count+"件商品 实付款:¥"+orderAmout;
		holder.orderProductDetailTxtv.setText(goodsInfo);

        return convertView;
	}

	@Override
	public void onClick(View v) {
		int tag = Integer.valueOf(v.getTag(R.id.key_tag_index1).toString());
		int position = Integer.valueOf(v.getTag(R.id.key_tag_index2).toString());

		if (mHandler!=null){
			Message msg = mHandler.obtainMessage(SPMobileConstants.MSG_CODE_ORDER_BUTTON_ACTION);
			SPOrder order = mOrders.get(position);
			msg.obj = order;
			msg.what = tag;
			mHandler.sendMessage(msg);
		}
	}

	class ViewHolder{
		/** 产品预览图View **/
		HorizontalScrollView orderProductScrollv;
		LinearLayout productListGalleryLyaout;

		/** 底部操作按钮View **/
		HorizontalScrollView orderButtonScrollv;
		LinearLayout orderButtonGalleryLyaout;


		RelativeLayout orderProductRlayout;
		ImageView orderPicImgv;
		TextView orderProductNameTxtv;
		TextView orderProductDetailTxtv;
		LinearLayout orderButtonLayout;
	}

	private void buildProductPreviewGallery(LinearLayout gallery , List<SPProduct> products , int position){
		gallery.removeAllViews();
		for (int i = 0; i < products.size(); i++){
			String url = products.get(i).getImageThumlUrl();
			View view = LayoutInflater.from(mContext).inflate(R.layout.activity_index_gallery_item, gallery, false);
			view.setOnClickListener(orderButtonClickListener);
			view.setTag(position);
			ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
			Glide.with(mContext).load(url).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);


			gallery.addView(view);
		}
	}

	private void buildProductButtonGallery(LinearLayout gallery , List<OrderButtonModel> buttonModels , int position){
		gallery.removeAllViews();
		if (buttonModels !=null && buttonModels.size() > 0){
			for (int i = 0; i < buttonModels.size(); i++){
				OrderButtonModel buttonModel = buttonModels.get(i);
				View view = LayoutInflater.from(mContext).inflate(R.layout.order_button_gallery_item, gallery, false);
				Button button = (Button) view.findViewById(R.id.id_index_gallery_item_button);
				button.setText(buttonModel.getText());
				button.setFocusable(false);
				button.setTag(R.id.key_tag_index1, buttonModel.getTag());
				button.setTag(R.id.key_tag_index2, position);

				if (buttonModel.isLight()){
					button.setBackgroundResource(R.drawable.tag_button_bg_checked);
				}else{
					button.setBackgroundResource(R.drawable.tag_button_bg_unchecked);
				}
				button.setOnClickListener(this);

				gallery.addView(view);
			}
		}else{
			View view = LayoutInflater.from(mContext).inflate(R.layout.order_button_gallery_item, gallery, false);
			Button button = (Button) view.findViewById(R.id.id_index_gallery_item_button);
			button.setText("再次购买");
			button.setFocusable(false);
			button.setTag(R.id.key_tag_index1, SPMobileConstants.tagBuyAgain);
			button.setTag(R.id.key_tag_index2, position);
			button.setBackgroundResource(R.drawable.tag_button_bg_unchecked);
			button.setOnClickListener(this);
			gallery.addView(view);
		}
	}

	public List<OrderButtonModel> getOrderButtonModelByOrder(SPOrder order) {

		List<OrderButtonModel> buttons = new ArrayList<OrderButtonModel>();
		if (order.getPayBtn() == 1) {//显示支付按钮
			OrderButtonModel payModel = new OrderButtonModel("支付", SPMobileConstants.tagPay, true);
			buttons.add(payModel);
		}

		if (order.getCancelBtn() == 1) {//取消订单按钮
			OrderButtonModel cancelModel = new OrderButtonModel("取消订单", SPMobileConstants.tagCancel, false);
			buttons.add(cancelModel);
		}

		if (order.getReceiveBtn() == 1) {//确认收货按钮
			OrderButtonModel cancelModel = new OrderButtonModel("确认收货", SPMobileConstants.tagReceive, true);
			buttons.add(cancelModel);
		}

		if (order.getShippingBtn() == 1) {//查看物流按钮
			OrderButtonModel cancelModel = new OrderButtonModel("查看物流", SPMobileConstants.tagShopping, true);
			buttons.add(cancelModel);
		}
		return buttons;
	}

	public View.OnClickListener orderButtonClickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			int position = Integer.valueOf(v.getTag().toString());
			if (mHandler!=null){
				Message msg = mHandler.obtainMessage(SPMobileConstants.MSG_CODE_ORDER_LIST_ITEM_ACTION);
				SPOrder order = mOrders.get(position);
				msg.obj = order;
				mHandler.sendMessage(msg);
			}


		}
	};
}
