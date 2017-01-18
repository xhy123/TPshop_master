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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.R;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.model.shop.SPCollect;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;


/**
 * @author 飞龙
 *
 */
public class SPCollectListAdapter extends BaseAdapter {

	private String TAG = "SPCollectListAdapter";

	private List<SPCollect> mCollectLst ;
	private Context mContext ;

	public SPCollectListAdapter(Context context){
		this.mContext = context;

	}
	
	public void setData(List<SPCollect> collectLst){
		if(collectLst == null)return;
		this.mCollectLst = collectLst;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mCollectLst == null)return 0;
		return mCollectLst.size();
	}

	@Override
	public Object getItem(int position) {
		if(mCollectLst == null) return null;
		return mCollectLst.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mCollectLst == null) return -1;
		return Long.valueOf(mCollectLst.get(position).getGoodsID());
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.person_collect_list_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();

			holder.picIngv = ((ImageView) convertView.findViewById(R.id.product_pic_imgv));
			holder.nameTxtv = ((TextView) convertView.findViewById(R.id.product_name_txtv));
			holder.priceTxtv = ((TextView) convertView.findViewById(R.id.product_price_txtv));

			  //设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
        SPCollect collect = (SPCollect)mCollectLst.get(position);

		String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, collect.getGoodsID());

		Glide.with(mContext).load(imgUrl1).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picIngv);
		if (!SPStringUtils.isEmpty(collect.getGoodsName())){
			holder.nameTxtv.setText(collect.getGoodsName());
		}

		if (!SPStringUtils.isEmpty(collect.getShopPrice())){
			holder.priceTxtv.setText(collect.getShopPrice());
		}
        return convertView;
	}
	
	class ViewHolder{
		ImageView picIngv ;
		TextView nameTxtv;	//商品名称
		TextView priceTxtv;	//商品价格
	}


}
