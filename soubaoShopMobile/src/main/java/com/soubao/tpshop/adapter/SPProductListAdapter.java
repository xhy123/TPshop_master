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
 * Description: 商城 -> 产品列表Adapter
 * @version V1.0
 */
package com.soubao.tpshop.adapter;

import java.util.List;

import android.content.Context;
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
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPCommonUtils;


/**
 * @author 飞龙
 *
 */
public class SPProductListAdapter extends BaseAdapter {
	
	private String TAG = "SPProductListAdapter";
	
	private List<SPProduct> mProductLst ;
	private Context mContext ;
	private ItemClickListener mListener;
	
	public SPProductListAdapter(Context context, ItemClickListener listener){
		this.mContext = context;
		this.mListener = listener;

	}
	
	public void setData(List<SPProduct> products){
		if(products == null)return;
		this.mProductLst = products;
	}

	@Override
	public int getCount() {
		if(mProductLst == null)return 0;
		
		// 每列两项
		if (mProductLst.size() % 2 == 0) {
			return mProductLst.size() / 2;
		}
		return mProductLst.size() / 2 + 1;
				
	}

	@Override
	public Object getItem(int position) {
		if(mProductLst == null) return null;
		return mProductLst.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mProductLst == null) return -1;
		return Long.valueOf(mProductLst.get(position).getGoodsID());
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
	          convertView = LayoutInflater.from(mContext).inflate(R.layout.product_list_item, parent, false);
	          //使用减少findView的次数
			  holder = new ViewHolder();
			  
			  holder.view1 = ((View) convertView.findViewById(R.id.product_cell_layout1)) ;
			  holder.nameTxtv1 = ((TextView) convertView.findViewById(R.id.product_name_txtv1)) ;
			  holder.priceTxtv1 = ((TextView) convertView.findViewById(R.id.product_price_txtv1)) ;
			  holder.picImgv1 = ((ImageView) convertView.findViewById(R.id.product_pic_imgv1)) ;
			  
			  holder.view2 = ((View) convertView.findViewById(R.id.product_cell_layout2)) ;
			  holder.nameTxtv2 = ((TextView) convertView.findViewById(R.id.product_name_txtv2)) ;
			  holder.priceTxtv2 = ((TextView) convertView.findViewById(R.id.product_price_txtv2)) ;
			  holder.picImgv2 = ((ImageView) convertView.findViewById(R.id.product_pic_imgv2)) ;

			  holder.bottonLineView = ((View) convertView.findViewById(R.id.product_bottom_line_view)) ;

			  //设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
        SPProduct product1 = (SPProduct)mProductLst.get(position * 2 );
        
        //设置数据到View
        //格式化价格
        String price1 = String.format(mContext.getResources().getString(R.string.product_price), Float.valueOf(product1.getShopPrice()));
        
        holder.nameTxtv1.setText(product1.getGoodsName());
        holder.priceTxtv1.setText(""+String.valueOf(price1));
        holder.nameTxtv1.setText(product1.getGoodsName());

		String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, 400, 400, product1.getGoodsID());
		Glide.with(mContext).load(imgUrl1).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picImgv1);
		holder.view1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onItemClickListener((SPProduct)mProductLst.get(position * 2) );
				}
			}
		 });
        
        if ((position * 2 + 1) < mProductLst.size()) {
        	
        	SPProduct product2 = (SPProduct)mProductLst.get(position * 2+1 );
            
            //设置数据到View
            //格式化价格
            String price2 = String.format(mContext.getResources().getString(R.string.product_price),Float.valueOf( product2.getShopPrice()));
            holder.nameTxtv2.setText(product2.getGoodsName());
            holder.priceTxtv2.setText(""+String.valueOf(price2));
            holder.nameTxtv2.setText(product2.getGoodsName());
			String imgUrl2 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, product2.getGoodsID());
			Glide.with(mContext).load(imgUrl2).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picImgv2);


            holder.view2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mListener != null){
						mListener.onItemClickListener((SPProduct)mProductLst.get(position*2+1));
					}
				}
			 });
        }
        
        
        return convertView;
	}
	
	class ViewHolder{
		View view1;
		ImageView picImgv1 ;
		TextView nameTxtv1;	//商品名称 
		TextView priceTxtv1; //商品价格
		
		View view2;
		ImageView picImgv2 ;
		TextView nameTxtv2;	//商品名称 
		TextView priceTxtv2; //商品价格

		View bottonLineView;
	}
	
	public interface ItemClickListener{
		public void onItemClickListener(SPProduct product);
	}

}
