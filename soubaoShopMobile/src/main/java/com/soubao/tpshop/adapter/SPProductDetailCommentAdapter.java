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
 * Description:  积分金额明细 dapter
 * @version V1.0
 */
package com.soubao.tpshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.R;
import com.soubao.tpshop.model.shop.SPGoodsComment;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.soubao.tpshop.view.SPStarView;

import java.util.List;


/**
 * @author 飞龙
 *
 */
public class SPProductDetailCommentAdapter extends BaseAdapter {

	private String TAG = "SPProductDetailCommentAdapter";

	private List<SPGoodsComment> mComments ;
	private Context mContext ;

	public SPProductDetailCommentAdapter(Context context){
		this.mContext = context;
	}
	
	public void setData(List<SPGoodsComment> comments){
		if(comments == null)return;
		this.mComments = comments;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mComments == null)return 0;
		return mComments.size();
	}

	@Override
	public Object getItem(int position) {
		if(mComments == null) return null;
		return mComments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return -1 ;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.product_details_comment_list_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();

			holder.headImgv = ((ImageView) convertView.findViewById(R.id.comment_head_imgv));
			holder.usernameTxtv = ((TextView) convertView.findViewById(R.id.comment_username_txtv));
			holder.dateTxtv = ((TextView) convertView.findViewById(R.id.comment_addtime_txtv));
			holder.contentTxtv = ((TextView) convertView.findViewById(R.id.comment_content_txtv));
			holder.starView = ((SPStarView) convertView.findViewById(R.id.comment_star_layout));
			holder.gallery = ((LinearLayout) convertView.findViewById(R.id.comment_gallery_lyaout));
			holder.scrollv = ((HorizontalScrollView) convertView.findViewById(R.id.comment_product_scrollv));

			holder.starView.setIsResponseClickListener(false);
			  //设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
		SPGoodsComment goodsComment = (SPGoodsComment)mComments.get(position);
		if (!SPStringUtils.isEmpty(goodsComment.getAddTime())){
			 String date = SPCommonUtils.getDateFullTime(Long.valueOf(goodsComment.getAddTime()));
			holder.dateTxtv.setText(date);
		}
		Glide.with(mContext).load(goodsComment.getHeadUrl()).placeholder(R.drawable.person_default_head).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.headImgv);

		holder.usernameTxtv.setText(goodsComment.getUsername());
		holder.contentTxtv.setText(goodsComment.getContent());

		if (goodsComment.getGoodsRank()!=null){
			int rank = Integer.valueOf(goodsComment.getGoodsRank());
			holder.starView.checkStart(rank-1);
		}

		List<String> images = goodsComment.getImages();
		if (SPCommonUtils.verifyArray(images)){
			buildProductGallery(holder.gallery,goodsComment);
			holder.scrollv.setVisibility(View.VISIBLE);
			convertView.setMinimumHeight(Float.valueOf(mContext.getResources().getDimension(R.dimen.comment_big_height)).intValue());
		}else{
			holder.scrollv.setVisibility(View.GONE);
			convertView.setMinimumHeight(Float.valueOf(mContext.getResources().getDimension(R.dimen.comment_normal_height)).intValue());
		}

        return convertView;
	}
	
	class ViewHolder{
		ImageView headImgv;
		TextView usernameTxtv;
		TextView dateTxtv;
		TextView contentTxtv;
		SPStarView starView;
		LinearLayout gallery;
		HorizontalScrollView scrollv;
	}

	private void buildProductGallery(LinearLayout gallery , SPGoodsComment goodsComment){
		gallery.removeAllViews();
		List<String> images = goodsComment.getImages();
		if (SPCommonUtils.verifyArray(images)){
			for (int i = 0; i < images.size(); i++){
				String url = images.get(i);
				View view = LayoutInflater.from(mContext).inflate(R.layout.activity_index_gallery_item, gallery, false);
				ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
				Glide.with(mContext).load(url).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);

				gallery.addView(view);
			}
		}
	}

}
