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
 * Description:  退换货商品列表
 * @version V1.0
 */

package com.soubao.tpshop.adapter;

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
import com.soubao.tpshop.model.order.SPExchange;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPOrderUtils;

import java.util.List;


/**
 * @author 飞龙
 *
 */
public class SPExchangeListAdapter extends BaseAdapter {

	private String TAG = "SPExchangeListAdapter";

	private List<SPExchange> mExchangeLst ;
	private Context mContext ;

	public SPExchangeListAdapter(Context context){
		this.mContext = context;

	}
	
	public void setData(List<SPExchange> exchangeLst){
		if(exchangeLst == null)return;
		this.mExchangeLst = exchangeLst;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mExchangeLst == null)return 0;
		return mExchangeLst.size();
	}

	@Override
	public Object getItem(int position) {
		if(mExchangeLst == null) return null;
		return mExchangeLst.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mExchangeLst == null) return -1;
		return Long.valueOf(mExchangeLst.get(position).getExchangeId());
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.order_exchange_list_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();
			holder.orderSnTxtv = ((TextView) convertView.findViewById(R.id.exchange_ordersn_txtv));
			holder.statusTxtv = ((TextView) convertView.findViewById(R.id.exchange_status_txtv));
			holder.addTimeTxtv = ((TextView) convertView.findViewById(R.id.exchange_addtime_txtv));
			holder.reasonTxtv = ((TextView) convertView.findViewById(R.id.exchange_reason_txtv));
			holder.picIngv = ((ImageView) convertView.findViewById(R.id.exchange_product_pic_imgv));
			holder.nameTxtv = ((TextView) convertView.findViewById(R.id.exchange_product_name_txtv));
			  //设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
        SPExchange exchange = (SPExchange)mExchangeLst.get(position);

		String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, exchange.getGoodsId());

		Glide.with(mContext).load(imgUrl1).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picIngv);
		holder.orderSnTxtv.setText("订单编号:" + exchange.getOrderSN());
		holder.addTimeTxtv.setText("申请时间:" + exchange.getAddtime());
		holder.statusTxtv.setText(SPOrderUtils.getExchangeStatusNameWithStatus(exchange.getStatus()));
		holder.reasonTxtv.setText("原因:"+exchange.getReason());
		holder.nameTxtv.setText(exchange.getGoodsName());

        return convertView;
	}
	
	class ViewHolder{
		TextView orderSnTxtv;
		TextView statusTxtv;
		TextView addTimeTxtv;
		TextView reasonTxtv;
		ImageView picIngv;
		TextView nameTxtv;
	}


}
