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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.model.SPCategory;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

/**
 * @author 飞龙
 *
 */
public class SPCategoryRightAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter{
	
	private String TAG = "SPCategoryRightAdapter";
	private List<SPCategory> mCategoryLst ;
	private Context mContext ;
	private int totalCount = 0;

	
	public SPCategoryRightAdapter(Context context){
		this.mContext = context;
	}

	@Override
	public int getCount() {
		if(mCategoryLst == null)return 0;
		return totalCount;
	}
	
	public void setData(List<SPCategory> categorys){
		if (categorys != null){
			this.mCategoryLst = new ArrayList<>();

			for (SPCategory category : categorys){
				List<SPCategory> subCategorys = category.getSubCategory();
				if (subCategorys !=null){
					int size = subCategorys.size();
					for(SPCategory subCategory : subCategorys){
						if (subCategory!=null){
							subCategory.setParentCategory(category);
							this.mCategoryLst.add(subCategory);
						}
					}
					//每一行不足3个的填充3个, 以下代码没有任何业务逻辑意义, 只是为了适配页面显示效果 @{
					int sy = size % 3;
					int count = (sy == 0 ) ? 0 : (3 - sy);
					if (count >= 0){
						for(int i=0; i<count ; i++){
							SPCategory thirdCategory = new SPCategory();
							thirdCategory.setParentCategory(category);
							thirdCategory.setIsBlank(true);
							mCategoryLst.add(thirdCategory);
						}
					}
					//}@
				}
			}
			totalCount = this.mCategoryLst.size();
			this.notifyDataSetChanged();
		}
	}
	

	@Override
	public Object getItem(int position) {
		if(mCategoryLst == null) return null;
		return mCategoryLst.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mCategoryLst == null) return -1;
		return mCategoryLst.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
	          convertView = LayoutInflater.from(mContext).inflate(R.layout.category_right_item, parent, false);
	          //使用减少findView的次数
			  holder = new ViewHolder();
			  holder.r1View = (View) convertView.findViewById(R.id.category_item_r1_layout);
			  holder.r1Imgv = (ImageView) convertView.findViewById(R.id.category_item_r1_imgv);
			  holder.r1Txtv = (TextView) convertView.findViewById(R.id.category_item_r1_txtv);
			  
			  //设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }
        
        if(mCategoryLst== null){
        	Log.w(TAG, "getView mCategoryLst is null .");
        	return null;
        }
        
        SPCategory category = null;
        
        if (position < mCategoryLst.size()) {
        	 category = (SPCategory)mCategoryLst.get(position);
			 if (category.isBlank()){
				 holder.r1Txtv.setVisibility(View.INVISIBLE);
				 holder.r1Imgv.setVisibility(View.INVISIBLE);

			 }else{
				 holder.r1Txtv.setVisibility(View.VISIBLE);
				 holder.r1Imgv.setVisibility(View.VISIBLE);

				 holder.r1Txtv.setText(category.getName());
				 holder.r1Imgv.setImageResource(R.drawable.category_default);
			 }
		}
        return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		SPCategory parentCategory = mCategoryLst.get(position).getParentCategory();
		return parentCategory.getId() ;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {

		HeaderViewHolder headerHolder;
		if (convertView == null) {
			headerHolder = new HeaderViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.category_grid_title_item, parent, false);
			headerHolder.titleTxtv = (TextView) convertView.findViewById(R.id.catelogy_right_title_txtv);
			convertView.setTag(headerHolder);
		} else {
			headerHolder = (HeaderViewHolder) convertView.getTag();
		}
		SPCategory parentCategory = mCategoryLst.get(position).getParentCategory();
		headerHolder.titleTxtv.setText(parentCategory.getName());

		return convertView;
	}

	class HeaderViewHolder{
		TextView titleTxtv;
	}

	class ViewHolder{
		
		View r1View ; 
		ImageView r1Imgv;
		TextView r1Txtv;
		
		View r2View ; 
		ImageView r2Imgv;
		TextView r2Txtv;
		
		View r3View ; 
		ImageView r3Imgv;
		TextView r3Txtv;
	}

}
