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
 * Description: 确认订单 -> 物流列表-> 物流列表 adapter
 * @version V1.0
 */
package com.soubao.tpshop.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.model.person.SPConsigneeAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author 飞龙
 *
 */
public class SPLogisticAdapter extends BaseAdapter{

	private JSONArray logisticArray ;
	private Context mContext ;
	private String  checkCode ;

	public SPLogisticAdapter(Context context , String checkCode){
		this.mContext = context;
		this.checkCode = checkCode;
	}

	public void setData(JSONArray logisticArray){
		if(logisticArray == null)return;
		this.logisticArray = logisticArray;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(logisticArray == null)return 0;
		return logisticArray.length();
	}

	@Override
	public Object getItem(int position) {
		if(logisticArray == null) return null;
		try {
			return logisticArray.get(position);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		 return -1;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
			//使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.logistic_list_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();
			holder.titleTxtv = ((TextView) convertView.findViewById(R.id.logistic_title_txtv)) ;
			holder.checkImgv = ((ImageView) convertView.findViewById(R.id.logistic_check_imgv)) ;
			//设置标记
			convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }

		try {
			//获取该行数据
			JSONObject deliverJson = logisticArray.getJSONObject(position);
			String code = deliverJson.getString("code");
			//设置数据到View
			holder.titleTxtv.setText(deliverJson.getString("name"));

			if (code.equals(this.checkCode)){
				holder.checkImgv.setImageResource(R.drawable.icon_checked);
			}else{
				holder.checkImgv.setImageResource(R.drawable.icon_checkno);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return convertView;
	}

	class ViewHolder{
		TextView titleTxtv;
		ImageView checkImgv;
	}

}
