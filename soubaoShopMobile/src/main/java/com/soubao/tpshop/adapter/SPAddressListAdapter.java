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
 * Description: 我的 -> 收货人列表 -> 收货人列表 adapter
 * @version V1.0
 */
package com.soubao.tpshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.model.SPCategory;
import com.soubao.tpshop.model.person.SPConsigneeAddress;

import java.util.List;

/**
 * @author 飞龙
 *
 */
public class SPAddressListAdapter extends BaseAdapter implements View.OnClickListener {

	private List<SPConsigneeAddress> mConsignees ;
	private Context mContext ;
	private AddressListListener mAddressListListener;


	public SPAddressListAdapter(Context context , AddressListListener addressListListener){
		this.mContext = context;
		this.mAddressListListener = addressListListener;
	}
	

	public void setData(List<SPConsigneeAddress> consignees){
		if(consignees == null)return;
		this.mConsignees = consignees;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mConsignees == null)return 0;
		return mConsignees.size();
	}

	@Override
	public Object getItem(int position) {
		if(mConsignees == null) return null;
		return mConsignees.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mConsignees == null) return -1;
		return Long.valueOf(mConsignees.get(position).getAddressID());
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
			//使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.person_address_list_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();
			holder.consigneeTxtv = ((TextView) convertView.findViewById(R.id.address_consignee_txtv)) ;
			holder.mobileTxtv = ((TextView) convertView.findViewById(R.id.address_mobile_txtv)) ;
			holder.addressTxtv = ((TextView) convertView.findViewById(R.id.address_detail_txtv)) ;
			holder.setDefaultBtn= ((Button) convertView.findViewById(R.id.address_setdefault_btn)) ;
			holder.editBtn= ((Button) convertView.findViewById(R.id.address_edit_btn)) ;
			holder.deleteBtn = ((Button) convertView.findViewById(R.id.address_delete_btn)) ;
			//设置标记
			convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }

		holder.editBtn.setOnClickListener(this);
		holder.editBtn.setTag(position);

		holder.deleteBtn.setOnClickListener(this);
		holder.deleteBtn.setTag(position);

		holder.setDefaultBtn.setOnClickListener(this);
		holder.setDefaultBtn.setTag(position);

        //获取该行数据
        SPConsigneeAddress consignee = (SPConsigneeAddress)mConsignees.get(position);
        
        //设置数据到View
		holder.consigneeTxtv.setText(consignee.getConsignee());
		holder.mobileTxtv.setText(consignee.getMobile());
		holder.addressTxtv.setText(consignee.getFullAddress());

		if("1".equals(consignee.getIsDefault())){
			holder.setDefaultBtn.setBackgroundResource(R.drawable.icon_checked);
		}else{
			holder.setDefaultBtn.setBackgroundResource(R.drawable.icon_checkno);
		}

        return convertView;
	}

	@Override
	public void onClick(View v) {
		int position = Integer.valueOf(v.getTag().toString());
		SPConsigneeAddress consignee = mConsignees.get(position);

		switch (v.getId()){
			case R.id.address_delete_btn:
				if (mAddressListListener!= null)mAddressListListener.onItemDelete(consignee);
				break;
			case R.id.address_edit_btn:
				if (mAddressListListener!=null)mAddressListListener.onItemEdit(consignee);
				break;
			case R.id.address_setdefault_btn:
				if (mAddressListListener!=null)mAddressListListener.onItemSetDefault(consignee);
				break;
		}
	}

	class ViewHolder{
		TextView consigneeTxtv;
		TextView mobileTxtv;
		TextView addressTxtv;
		Button  setDefaultBtn;
		Button  editBtn;
		Button  deleteBtn;
		TextView setDefaultTxtv;
	}

	public interface AddressListListener{
		public void onItemDelete(SPConsigneeAddress consigneeAddress);
		public void onItemEdit(SPConsigneeAddress consigneeAddress);
		public void onItemSetDefault(SPConsigneeAddress consigneeAddress);
	}

}
