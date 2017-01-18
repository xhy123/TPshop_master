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
 * Date: @date 2015年11月12日 下午8:08:13
 * Description:商品详情 -> 商品评论, 图文详情
 * @version V1.0
 */
package com.soubao.tpshop.activity.person;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.common.SPBaseActivity;
import com.soubao.tpshop.adapter.SPCouponTabAdapter;
import com.soubao.tpshop.adapter.SPProductDetailInnerTabAdapter;
import com.viewpagerindicator.TabPageIndicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.person_coupon_main_view)
public class SPCouponListActivity extends SPBaseActivity {

    private String TAG = "SPCouponListActivity";
    private static final int TAB_INDEX_FIRST = 0;
    private static final int TAB_INDEX_SECOND = 1;
    private static final int TAB_INDEX_THREE = 2;

    @ViewById(R.id.coupon_page_indicator)
    TabPageIndicator mPageIndicator;

    @ViewById(R.id.coupon_view_pager)
    ViewPager mViewPager;

    FragmentPagerAdapter fragPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true , true , getString(R.string.title_coupon));
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    public void init(){
       super.init();
    }

    @Override
    public void initSubViews() {

        fragPagerAdapter = new SPCouponTabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(fragPagerAdapter);
        mPageIndicator.setViewPager(mViewPager, 0);
        mPageIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }



}
