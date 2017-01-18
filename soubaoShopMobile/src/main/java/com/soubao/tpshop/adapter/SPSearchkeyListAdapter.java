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
 * Description: 分类 -> 左边菜单 adapter
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.model.OrderButtonModel;
import com.soubao.tpshop.model.SPCategory;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPServerUtils;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 *
 */
public class SPSearchkeyListAdapter extends BaseAdapter {

	private static String TAG = "SPSearchkeyListAdapter";
	private List<String> mSearchkeys ;
	private Context mContext ;
	private Handler mHandler;

	public static final int TYPE_HOT_KEY = 0;
	public static final int TYPE_TITLE = 1;
	public static final int TYPE_NORMAL_KEY = 2;

	public SPSearchkeyListAdapter(Context context , Handler handler){
		this.mContext = context;
		this.mHandler = handler;
	}
	
	public void setData(List<String> searchkeys){
		if(searchkeys == null){
			this.mSearchkeys = new ArrayList<String>();
		}else{
			this.mSearchkeys = searchkeys;
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mSearchkeys == null || mSearchkeys.size() < 1)return 2;
		return mSearchkeys.size() + 2;
	}

	@Override
	public Object getItem(int position) {
		if (position >= 2){
			return mSearchkeys.get(position-2);
		}
		return null;
	}

	@Override
	public int getItemViewType(int position) {
		if(mSearchkeys == null || mSearchkeys.size() < 1){
			if (position == 0){
				return TYPE_HOT_KEY;
			}else{
				return TYPE_TITLE;
			}
		}else{
			if (position == 0){
				return TYPE_HOT_KEY;
			}else if(position == 1){
				return TYPE_TITLE;
			}else{
				return TYPE_NORMAL_KEY;
			}
		}
	}

	@Override
	public int getViewTypeCount() {
		if(mSearchkeys == null || mSearchkeys.size() < 1){
			return 1;
		}else{
			return 3;
		}
	}

	@Override
	public long getItemId(int position) {
		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		int itemViewType = getItemViewType(position);
		final ViewHolder holder;
		switch (itemViewType){
			case TYPE_HOT_KEY:
				if (convertView == null){
					holder = new ViewHolder();
					convertView = LayoutInflater.from(mContext).inflate(R.layout.common_search_hotkey_item, parent, false);
					holder.buttonGallery = (LinearLayout)convertView.findViewById(R.id.search_hotkey_lyaout);
					convertView.setTag(holder);
				}else{
					holder = (ViewHolder) convertView.getTag();
				}
				buildProductButtonGallery(holder.buttonGallery);
				break;
			case TYPE_TITLE:
				if (convertView == null){
					holder = new ViewHolder();
					convertView = LayoutInflater.from(mContext).inflate(R.layout.common_search_key_title_item, parent, false);
					convertView.setTag(holder);
				}else{
					holder = (ViewHolder) convertView.getTag();
				}
				break;
			case TYPE_NORMAL_KEY:
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = LayoutInflater.from(mContext).inflate(R.layout.common_search_key_item, parent, false);
					holder.keyTxtv = (TextView)convertView.findViewById(R.id.search_item_key_txtv);
					convertView.setTag(holder);
				}else{
					holder = (ViewHolder) convertView.getTag();
				}
				String key = mSearchkeys.get(position-2);
				holder.keyTxtv.setText(key);
				break;
		}
        return convertView;
	}
	
	class ViewHolder{
		LinearLayout buttonGallery ;
		TextView keyTxtv;
		TextView titleTxtv;
	}

	private void buildProductButtonGallery(LinearLayout gallery){
		gallery.removeAllViews();
		List<String> hotKeys = SPServerUtils.getHotKeyword();

		if (hotKeys !=null && hotKeys.size() > 0){
			for (int i = 0; i < hotKeys.size()+1; i++){
				if (i == 0){
					View txtView = LayoutInflater.from(mContext).inflate(R.layout.text_gallery_item, gallery, false);
					gallery.addView(txtView);
				}else{
					View view = LayoutInflater.from(mContext).inflate(R.layout.order_button_gallery_item, gallery, false);
					Button button = (Button) view.findViewById(R.id.id_index_gallery_item_button);
					button.setText(hotKeys.get(i-1));
					button.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View v) {
							if (v instanceof Button){
								Button hotBtn = (Button)v;
								if (mHandler!=null){
									Message msg = mHandler.obtainMessage(SPMobileConstants.MSG_CODE_SEARCHKEY);
									msg.obj = hotBtn.getText();
									mHandler.sendMessage(msg);
								}
							}
						}
					});
					button.setBackgroundResource(R.drawable.tag_button_bg_unchecked);
					gallery.addView(view);
				}
			}
		}
	}

	public View.OnClickListener buttonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			if (v instanceof Button){
				Button hotBtn = (Button)v;
			}

		}
	};

}
