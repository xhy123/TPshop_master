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
package com.soubao.tpshop.view.tagview;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;

import com.soubao.tpshop.R;
import com.soubao.tpshop.utils.SPCommonUtils;

/**
 * @author kince
 * @category
 * 
 */
public class TagListView extends FlowLayout implements OnClickListener {

	private String TAG = "TagListView";

	private boolean mIsDeleteMode;
	private OnTagCheckedChangedListener mOnTagCheckedChangedListener;
	private OnTagClickListener mOnTagClickListener;
	private int mTagViewBackgroundResId;
	private int mTagViewTextColorResId;
	private int mTtagViewWidth;
	private Tag mSelectTag;	//选择的Tag

	private final List<Tag> mTags = new ArrayList<Tag>();
	private float mTagListViewHeight ;//taglistview高度
	private int mTtagCountOfRow;	//每行显示tag数量

	private Context mContext;
	/**
	 * @param context
	 */
	public TagListView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	/**
	 * @param context
	 * @param attributeSet
	 */
	public TagListView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	/**
	 * @param context
	 * @param attributeSet
	 * @param defStyle
	 */
	public TagListView(Context context, AttributeSet attributeSet, int defStyle) {
		super(context, attributeSet, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	@Override
	public void onClick(View v) {
		if ((v instanceof TagView)) {
			TagView checkTagView = (TagView)v;
			Tag localTag = (Tag) checkTagView.getTag();
			//if (mSelectTag!=null && mSelectTag.getId() == localTag.getId()) return ;
			mSelectTag = localTag;
			checkTagView.setSelected(true);
			if (this.mOnTagClickListener != null) {
				this.mOnTagClickListener.onTagClick(checkTagView, localTag);
			}
		}

		int count =this.getChildCount();
		for (int i=0; i<count ; i++){
			 View childrenView = getChildAt(i);
			if (childrenView instanceof TagView){
				TagView childrenTagView = (TagView)childrenView;
				if(((Tag)childrenTagView.getTag()).getId() == mSelectTag.getId()){
					continue;
				}else{
					childrenTagView.setSelected(false);
				}
			}
		}
	}

	private void init() {
		mTtagCountOfRow = 3;
	}

	private void inflateTagView(final Tag t, boolean b) {


		TagView localTagView = (TagView) View.inflate(getContext(), R.layout.tag, null);
		localTagView.setText(t.getTitle());
		localTagView.setTag(t);

		if (this.mTtagViewWidth <=0){
			mTtagViewWidth = 320;
		}

		int selfWidth = SPCommonUtils.dip2px(mContext, 320);
		int horizontalSpacing = SPCommonUtils.dip2px(mContext , mContext.getResources().getDimension(R.dimen.tag_horizontal_spacing));
		int paddingBound = SPCommonUtils.dip2px(mContext , mContext.getResources().getDimension(R.dimen.tag_common_margin));
		int itemHeight = SPCommonUtils.dip2px(mContext , mContext.getResources().getDimension(R.dimen.tag_height));
		int itemWidth = (selfWidth - (horizontalSpacing * 2 + paddingBound * 2)) / mTtagCountOfRow;

		int verticalSpacing = SPCommonUtils.dip2px(mContext, mContext.getResources().getDimension(R.dimen.tag_vertical_spacing));//垂直间隙
		int listviewPaddingTop =SPCommonUtils.dip2px(mContext, mContext.getResources().getDimension(R.dimen.tag_listview_padding_top));//taglistview距离顶部间隙
		int listviewBottomTop = SPCommonUtils.dip2px(mContext, mContext.getResources().getDimension(R.dimen.tag_common_margin));//taglistview距离底部间隙



		FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(itemWidth, itemHeight);
		Button button = new Button(mContext);
		button.setText(t.getTitle());
		localTagView.setLayoutParams(layoutParams);

		if (mTagViewTextColorResId <= 0) {
			int c = getResources().getColor(R.color.color_font_gray);
			localTagView.setTextColor(c);

		}

		if (mTagViewBackgroundResId <= 0) {
			mTagViewBackgroundResId = R.drawable.tag_button_bg_unchecked;
			localTagView.setBackgroundResource(mTagViewBackgroundResId);
		}

		localTagView.setSelected(t.isChecked());
		localTagView.setCheckEnable(b);
		if (mIsDeleteMode) {
			int k = (int) TypedValue.applyDimension(1, 5.0F, getContext()
					.getResources().getDisplayMetrics());
			localTagView.setPadding(localTagView.getPaddingLeft(),
					localTagView.getPaddingTop(), k,
					localTagView.getPaddingBottom());
			//localTagView.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.forum_tag_close, 0);
		}
		if (t.getBackgroundResId() > 0) {
			localTagView.setBackgroundResource(t.getBackgroundResId());
		}
		if ((t.getLeftDrawableResId() > 0) || (t.getRightDrawableResId() > 0)) {
			localTagView.setCompoundDrawablesWithIntrinsicBounds(
					t.getLeftDrawableResId(), 0, t.getRightDrawableResId(), 0);
		}
		localTagView.setOnClickListener(this);
		localTagView
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(
							CompoundButton paramAnonymousCompoundButton,
							boolean paramAnonymousBoolean) {
						t.setChecked(paramAnonymousBoolean);
						if (TagListView.this.mOnTagCheckedChangedListener != null) {
							TagListView.this.mOnTagCheckedChangedListener
									.onTagCheckedChanged(
											(TagView) paramAnonymousCompoundButton,
											t);
						}
					}
				});

		addView(localTagView);

		/*FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(200, 40);
		Button button = new Button(mContext);
		button.setText(t.getTitle());

		button.setTextSize(mContext.getResources().getDimension(R.dimen.font_size_small));
		button.setTextColor(mContext.getResources().getColor(R.color.black));
		button.setLayoutParams(layoutParams);

		addView(button);*/
	}

	public void addTag(int i, String s) {
		addTag(i, s, false);
	}

	public void addTag(int i, String s, boolean b) {
		addTag(new Tag(i, s), b);
	}

	public void addTag(Tag tag) {
		addTag(tag, false);
	}

	public void addTag(Tag tag, boolean b) {
		mTags.add(tag);
		inflateTagView(tag, b);
	}

	public void addTags(List<Tag> lists) {
		addTags(lists, false);
	}

	public void addTags(List<Tag> lists, boolean b) {
		for (int i = 0; i < lists.size(); i++) {
			addTag((Tag) lists.get(i), b);
		}
	}

	public List<Tag> getTags() {
		return mTags;
	}

	public float getTagListViewHeight(){
		return mTagListViewHeight;
	}

	public View getViewByTag(Tag tag) {
		return findViewWithTag(tag);
	}

	public void removeTag(Tag tag) {
		mTags.remove(tag);
		removeView(getViewByTag(tag));
	}

	public void setDeleteMode(boolean b) {
		mIsDeleteMode = b;
	}

	public void setOnTagCheckedChangedListener(
			OnTagCheckedChangedListener onTagCheckedChangedListener) {
		mOnTagCheckedChangedListener = onTagCheckedChangedListener;
	}

	public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
		mOnTagClickListener = onTagClickListener;
	}

	public void setTagViewBackgroundRes(int res) {
		mTagViewBackgroundResId = res;
	}

	public void setTagViewTextColorRes(int res) {
		mTagViewTextColorResId = res;
	}

	public void setTags(List<? extends Tag> lists) {
		setTags(lists, false);
	}

	public void setTags(List<? extends Tag> lists, boolean b) {
		removeAllViews();
		mTags.clear();
		int count = lists.size();
		for (int i = 0; i <count ; i++) {
			addTag((Tag) lists.get(i), b);
		}

		//计算taglistview高度 , 单位px
		int itemHeight = SPCommonUtils.dip2px(mContext , mContext.getResources().getDimension(R.dimen.tag_height));//每一个item高度
		int verticalSpacing = SPCommonUtils.dip2px(mContext, mContext.getResources().getDimension(R.dimen.tag_vertical_spacing));//垂直间隙
		int listviewPaddingTop =SPCommonUtils.dip2px(mContext, mContext.getResources().getDimension(R.dimen.tag_listview_padding_top));//taglistview距离顶部间隙
		int listviewBottomTop = SPCommonUtils.dip2px(mContext, mContext.getResources().getDimension(R.dimen.tag_common_margin));//taglistview距离底部间隙

		int rows = Double.valueOf(Math.ceil(count/3.0)).intValue();
		mTagListViewHeight = itemHeight*rows + (rows-1)* verticalSpacing + listviewPaddingTop + listviewBottomTop;
	}

	public static abstract interface OnTagCheckedChangedListener {
		public abstract void onTagCheckedChanged(TagView tagView, Tag tag);
	}

	public static abstract interface OnTagClickListener {
		public abstract void onTagClick(TagView tagView, Tag tag);
	}

	public void setTtagViewWidth(int tagWidth){
		this.mTtagViewWidth = tagWidth;
	}
}
