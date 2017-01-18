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
 * Description:  商品收藏列表 dapter
 * @version V1.0
 */
package com.soubao.tpshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.soubao.tpshop.R;
import com.soubao.tpshop.view.SPArrowRowView;


import java.util.List;


/**
 * @author 飞龙
 *
 */
public class SPSettingListAdapter extends BaseAdapter {

	private String TAG = "SPSettingListAdapter";

	private List<String> mTexts ;
	private Context mContext ;

	public SPSettingListAdapter(Context context){
		this.mContext = context;

	}
	
	public void setData(List<String> texts){
		if(texts == null)return;
		this.mTexts = texts;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mTexts == null)return 0;
		return mTexts.size();
	}

	@Override
	public Object getItem(int position) {
		if(mTexts == null) return null;
		return mTexts.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mTexts == null) return -1;
		return -1;
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.person_settings_list_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();
			holder.settingArrow = ((SPArrowRowView) convertView.findViewById(R.id.person_setting_item));
			  //设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
        String title = mTexts.get(position);
		holder.settingArrow.setText(title);

        return convertView;
	}
	
	class ViewHolder{
		SPArrowRowView settingArrow;

	}


}
