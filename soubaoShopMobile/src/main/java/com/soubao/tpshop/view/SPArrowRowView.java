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
 * Date: @date 2015年11月14日 下午8:17:18 
 * Description:带箭头的自定义view
 * @version V1.0
 */
package com.soubao.tpshop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soubao.tpshop.R;

/**
 * @author 飞龙
 *
 */
public class SPArrowRowView extends FrameLayout {

	private TextView mTitleTxtv;
	private TextView mSubTitleTxtv;
	private ImageView imageImgv;
	private ImageView arrowImgv;
	private boolean indicatorShow;
	/**
	 * @param context
	 * @param attrs
	 */
	public SPArrowRowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		 
		/** 获取自定义属性 titleText */
	    TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArrowView, 0, 0);
	    
	    String titleText = typeArray.getString(R.styleable.ArrowView_titleText);
		Drawable drawable = typeArray.getDrawable(R.styleable.ArrowView_arrowSrc);
		indicatorShow = typeArray.getBoolean(R.styleable.ArrowView_indicatorShow, true);
	    View view = LayoutInflater.from(context).inflate(R.layout.right_arrow_row_view, this);
	    mTitleTxtv = (TextView)view.findViewById(R.id.title_txtv);
		mSubTitleTxtv = (TextView)view.findViewById(R.id.sub_title_txtv);
		imageImgv = (ImageView)view.findViewById(R.id.image_imgv);
		arrowImgv = (ImageView)view.findViewById(R.id.arrow_imgv);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);

		float marginLeft = 0 ;

		if (drawable != null){
			marginLeft = context.getResources().getDimension(R.dimen.margin_space)
					+ getResources().getDimension(R.dimen.margin_space_half)
					+ getResources().getDimension(R.dimen.arrow_image_size);

			params.setMargins(Float.valueOf(marginLeft).intValue(), 0, 0, 0);
			imageImgv.setBackgroundDrawable(drawable);
		}else{
			marginLeft = context.getResources().getDimension(R.dimen.margin_space);
			params.setMargins(Float.valueOf(marginLeft).intValue() , 0 , 0 , 0);
		}

		if (indicatorShow){
			arrowImgv.setVisibility(View.VISIBLE);
		}else {
			arrowImgv.setVisibility(View.INVISIBLE);
		}

		mTitleTxtv.setLayoutParams(params);
		mTitleTxtv.setText(titleText);
		
	}

	public void setIndicatorShow(boolean show){
		indicatorShow = show;
		if (indicatorShow){
			arrowImgv.setVisibility(View.VISIBLE);
		}else {
			arrowImgv.setVisibility(View.INVISIBLE);
		}
	}

	public void setText(String text){
		if(mTitleTxtv == null || text == null)return;
		mTitleTxtv.setText(text);
	}

	public void setSubText(String text){
		if(mSubTitleTxtv == null || text == null)return;
		mSubTitleTxtv.setText(text);
	}

}





















































































