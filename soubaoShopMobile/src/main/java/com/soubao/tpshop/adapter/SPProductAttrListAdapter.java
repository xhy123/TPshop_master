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
 * Description: 商品详情 -> 商品属性 adapter
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
import com.soubao.tpshop.model.SPCategory;
import com.soubao.tpshop.model.shop.SPProductAttribute;

import java.util.List;

/**
 * @author 飞龙
 *
 */
public class SPProductAttrListAdapter extends BaseAdapter {

	private List<SPProductAttribute> mAttrLst ;
	private Context mContext ;

	public SPProductAttrListAdapter(Context context){
		this.mContext = context;
	}

	public void setData(List<SPProductAttribute> attrs){
		if(attrs == null)return;
		this.mAttrLst = attrs;
	}

	@Override
	public int getCount() {
		if(mAttrLst == null)return 0;
		return mAttrLst.size();
	}

	@Override
	public Object getItem(int position) {
		if(mAttrLst == null) return null;
		return mAttrLst.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mAttrLst == null) return -1;
		if (mAttrLst.get(position).getAttrID()!=null){
			return Long.valueOf(mAttrLst.get(position).getAttrID());
		}
		return 0;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
			holder = new ViewHolder();
		  	//使用自定义的list_items作为Layout
		  	convertView = LayoutInflater.from(mContext).inflate(R.layout.product_attr_list_item, parent, false);
		 	 holder.titleTxtv = ((TextView) convertView.findViewById(R.id.product_attr_title_txtv)) ;
			holder.valueTxtv = ((TextView) convertView.findViewById(R.id.product_attr_value_txtv)) ;
		 	//设置标记
		  	convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
        SPProductAttribute attribute = (SPProductAttribute)mAttrLst.get(position);
        
        //设置数据到View
        holder.titleTxtv.setText(attribute.getAttrName());
		holder.valueTxtv.setText(attribute.getAttrValue());

        return convertView;
	}
	
	class ViewHolder{
		TextView titleTxtv;
		TextView valueTxtv;
	}

}
