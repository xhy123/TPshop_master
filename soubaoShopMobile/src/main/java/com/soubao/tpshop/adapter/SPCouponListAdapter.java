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
 * Description: 我的 -> 优惠券列表
 * @version V1.0
 */
package com.soubao.tpshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.model.shop.SPCoupon;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;

/**
 * @author 飞龙
 *
 */
public class SPCouponListAdapter extends BaseAdapter {

	private List<SPCoupon> mCoupons ;
	private Context mContext ;
	private int mType;

	public SPCouponListAdapter(Context context , int type){
		this.mContext = context;
		this.mType = type;
	}


	public void setType(int type){
		this.mType = type;
	}
	

	public void setData(List<SPCoupon> coupons){
		if(coupons == null)return;
		this.mCoupons = coupons;
	}

	@Override
	public int getCount() {
		if(mCoupons == null)return 0;
		return mCoupons.size();
	}

	@Override
	public Object getItem(int position) {
		if(mCoupons == null) return null;
		return mCoupons.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mCoupons == null) return -1;
		return Integer.valueOf(mCoupons.get(position).getCouponID());
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
        if(convertView == null){
	        //使用自定义的list_items作为Layout
	        convertView = LayoutInflater.from(mContext).inflate(R.layout.person_coupon_list_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();
			holder.couponRlayout = ((View) convertView.findViewById(R.id.coupon_rlayout)) ;
			holder.rmbTxtv = ((TextView) convertView.findViewById(R.id.coupon_rmb_txtv)) ;
		 	holder.moneyTxtv = ((TextView) convertView.findViewById(R.id.coupon_money_txtv)) ;
			holder.titleTxtv = ((TextView) convertView.findViewById(R.id.coupon_title_txtv)) ;
			holder.timeTxtv = ((TextView) convertView.findViewById(R.id.coupon_time_txtv)) ;
			//设置标记
			convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }
        
        SPCoupon coupon = mCoupons.get(position);

		String money = coupon.getMoney();
		String title = coupon.getName();
		String userTimeText = "";

		int colorId = mContext.getResources().getColor(R.color.color_font_gray);
		switch (this.mType){
			case 0:
				colorId = mContext.getResources().getColor(R.color.color_font_blue);
				if(!SPStringUtils.isEmpty(coupon.getUseEndTime())){
					userTimeText = "过期时间:\n"+ SPCommonUtils.getDateFullTime(Long.valueOf(coupon.getUseEndTime()));
				}
				holder.couponRlayout.setBackgroundResource(R.drawable.icon_coupon_unuse);
				break;
			case 1:
				colorId = mContext.getResources().getColor(R.color.color_font_gray);
				if(!SPStringUtils.isEmpty(coupon.getUseTime())){
					userTimeText = "使用时间:\n"+ SPCommonUtils.getDateFullTime(Long.valueOf(coupon.getUseTime()));
				}
				holder.couponRlayout.setBackgroundResource(R.drawable.icon_coupon_used);
				break;
			case 2:
				colorId = mContext.getResources().getColor(R.color.color_font_gray);
				if(!SPStringUtils.isEmpty(coupon.getUseTime())){
					userTimeText = "使用时间:\n"+ SPCommonUtils.getDateFullTime(Long.valueOf(coupon.getUseTime()));
				}
				holder.couponRlayout.setBackgroundResource(R.drawable.icon_coupon_used);
				break;
		}

		if (!SPStringUtils.isEmpty(money)){
			holder.moneyTxtv.setText(Double.valueOf(money).intValue()+"");;
		}

		holder.titleTxtv.setText(title);
		holder.timeTxtv.setText(userTimeText);
		holder.rmbTxtv.setTextColor(colorId);
		holder.moneyTxtv.setTextColor(colorId);
        return convertView;
	}
	
	class ViewHolder{
		View couponRlayout;
		TextView rmbTxtv;
		TextView moneyTxtv;
		TextView titleTxtv;
		TextView timeTxtv;
	}

}
