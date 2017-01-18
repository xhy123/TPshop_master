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
 * Date: @date 2015年10月26日 下午9:52:45 
 * Description: 商城 -> 首页 adapter
 * @version V1.0
 */
package com.soubao.tpshop.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.R;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.model.SPProduct;
import com.soubao.tpshop.utils.SPCommonUtils;

import java.util.List;


public class SPHomeGridAdapter extends BaseAdapter {
	private List<SPProduct> mProducts ;
	private Context mContext;
	LayoutInflater mInflater;
	private ItemClickListener mListener;

	public SPHomeGridAdapter(List<SPProduct> products,Context context,LayoutInflater inflater,ItemClickListener listener){
		mProducts = products;
		mContext = context;
		mInflater = inflater;
		mListener = listener;
	}

	@Override
	public int getCount() {
		return mProducts.size();
	}

	@Override
	public Object getItem(int position) {
		return mProducts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.home_list_grid_item, null,false);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.home_list_grid_item_img);
			holder.title = (TextView) convertView.findViewById(R.id.home_list_grid_item_title);
			holder.price = (TextView) convertView.findViewById(R.id.home_list_grid_item_price);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		SPProduct product = mProducts.get(position);
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener != null){
					mListener.onItemClickListener( (SPProduct)mProducts.get(position) );
				}
			}
		});
		String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, product.getGoodsID());
		Glide.with(mContext).load(imgUrl1).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.img);

		String price1 = String.format(mContext.getResources().getString(R.string.product_price), Float.valueOf(product.getShopPrice()));
		holder.title.setText(product.getGoodsName());
		holder.price.setText(""+String.valueOf(price1));
		Log.e("SPHomeGridAdapter","zzx==>products: "+mProducts.size());
		return convertView;
	}
	public static class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView price;
	}

	public interface ItemClickListener{
		public void onItemClickListener(SPProduct product);
	}
}
