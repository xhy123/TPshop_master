package com.soubao.tpshop.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.soubao.tpshop.fragment.SPCouponListFragment;

public class SPCouponTabAdapter extends FragmentPagerAdapter {

	private SPCouponListFragment unuseFragment;	//未用
	private SPCouponListFragment usedFragment;	//已用
	private SPCouponListFragment expireFragment;//过期


	//Titles的标识
	public static  String[] couponTitles = new String[]{"可用", "已用", "过期"};

	public SPCouponTabAdapter(FragmentManager fm) {
		super(fm);
		unuseFragment = new SPCouponListFragment();
		unuseFragment.setType(0);

		usedFragment = new SPCouponListFragment();
		usedFragment.setType(1);

		expireFragment = new SPCouponListFragment();
		expireFragment.setType(2);
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 0){
			unuseFragment.loadData();
			return unuseFragment;
		}else if (position == 1){
			usedFragment.loadData();
			return usedFragment;
		}else{
			expireFragment.loadData();
			return expireFragment;
		}
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return couponTitles[position];
	}

	@Override
	public int getCount() {
		return couponTitles.length;
	}

}
