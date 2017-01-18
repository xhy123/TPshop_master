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
package com.soubao.tpshop.activity.shop;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.common.SPBaseActivity;
import com.soubao.tpshop.adapter.SPProductDetailInnerTabAdapter;
import com.viewpagerindicator.TabPageIndicator;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.SimpleViewPagerDelegate;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.Arrays;
import java.util.List;

@EActivity(R.layout.product_details_inner)
public class SPProductDetailInnerActivity extends SPBaseActivity {

    private String TAG = "SPProductDetailInnerActivity";
    private static final int TAB_INDEX_ONE = 0;
    private static final int TAB_INDEX_TWO = 1;


    MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private FragmentPagerAdapter fragPagerAdapter;

    public static  String[] productDetailInnerTitles = new String[]{"图文详情", "商品评价"};
    List<String> mDataList = Arrays.asList(productDetailInnerTitles);

    private String goodsId ;    //商品ID
    private String contents ;   //图文详情
    private int position ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true , true , "图文详情");
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    public void init(){
       super.init();
    }

    @Override
    public void initSubViews() {

        if (getIntent()==null || getIntent().getStringExtra("goodsId") == null || getIntent().getStringExtra("content") == null){
            showToast(getString(R.string.data_error));
            return;
        }

        mMagicIndicator = (MagicIndicator)findViewById(R.id.magic_indicator);
        mViewPager = (ViewPager)findViewById(R.id.view_pager);

        this.goodsId = getIntent().getStringExtra("goodsId");
        this.contents  = getIntent().getStringExtra("content");
        this.position = getIntent().getIntExtra("position" ,0);

        fragPagerAdapter = new SPProductDetailInnerTabAdapter(getSupportFragmentManager() , goodsId ,contents);
        mViewPager.setAdapter(fragPagerAdapter);


        // 当前页不定位到中间
        final CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setScrollPivotX(0.15f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.sub_title));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.light_red));
                simplePagerTitleView.setTextSize(12);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setYOffset(UIUtil.dip2px(context, 3));
                indicator.setColors(getResources().getColor(R.color.light_red));
                return indicator;
            }
        });

        mMagicIndicator.setNavigator(commonNavigator);
        mMagicIndicator.setDelegate(new SimpleViewPagerDelegate(mViewPager));

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }



}
