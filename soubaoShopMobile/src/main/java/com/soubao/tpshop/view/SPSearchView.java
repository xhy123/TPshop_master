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
 * Description: 搜索框
 * @version V1.0
 */
package com.soubao.tpshop.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.utils.SPStringUtils;

/**
 * @author 飞龙
 *
 */
public class SPSearchView extends LinearLayout {

	private ImageView backImgv;
	private EditText searchEdtv;
	private SPSearchViewListener searchListener;

	/**
	 * @param context
	 * @param attrs
	 */
	public SPSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
	    View view = LayoutInflater.from(context).inflate(R.layout.search_heard_view, this);
		searchEdtv = (EditText)view.findViewById(R.id.search_edtv);
		backImgv = (ImageView)view.findViewById(R.id.back_imgv);
		searchEdtv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (searchListener != null) {
					//searchListener.onSearchBoxClick(searchEdtv.getText().toString());
				}
			}
		});
		backImgv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (searchListener != null) searchListener.onBackClick();
			}
		});

		searchEdtv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {

					notifyStartSearching(textView.getText().toString());
				}
				return true;
			}
		});
	}

	/**
	 * 通知监听者 进行搜索操作
	 * @param text
	 */
	private void notifyStartSearching(String text){
		if (searchListener != null) {
			searchListener.onSearchBoxClick(text);
		}
	}

	public void setSearchKey(String searchKey){
		if (this.searchEdtv!=null && !SPStringUtils.isEmpty(searchKey)){
			this.searchEdtv.setText(searchKey);
		}
	}

	public EditText getSearchEditText(){
		return this.searchEdtv;
	}

	public void setSearchViewListener(SPSearchViewListener listener){
		this.searchListener = listener;
	}

	public interface SPSearchViewListener{
		public void onBackClick();
		public void onSearchBoxClick(String keyword);
	}

}





















































































