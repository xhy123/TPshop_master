package com.soubao.tpshop.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.soubao.tpshop.fragment.SPProductCommentListFragment;
import com.soubao.tpshop.fragment.SPProductPictureTextDetaiFragment;

public class SPProductDetailInnerTabAdapter extends FragmentPagerAdapter {

	private SPProductPictureTextDetaiFragment pictureTextDetaiFragment;
	private SPProductCommentListFragment commentListFragment;

	private String goodsId ;
	private String contents ;

	//Titles的标识
	public static  String[] productDetailInnerTitles = new String[]{"图文详情", "商品评价"};

	public SPProductDetailInnerTabAdapter(FragmentManager fm , String goodsId , String contents) {
		super(fm);
		this.goodsId = goodsId;
		this.contents = contents;
		pictureTextDetaiFragment = new SPProductPictureTextDetaiFragment();
		pictureTextDetaiFragment.setContent(this.contents);
		commentListFragment = new SPProductCommentListFragment();
		commentListFragment.setGoodsId(this.goodsId);
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 0){
			pictureTextDetaiFragment.loadData();
			return pictureTextDetaiFragment;
		}else{
			commentListFragment.loadData();
			return commentListFragment;
		}
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return productDetailInnerTitles[position];
	}

	@Override
	public int getCount() {
		return productDetailInnerTitles.length;
	}

}
