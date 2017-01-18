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
 * Date: @date 2015年10月20日 下午7:19:26
 * Description:SPPersonFragment 商品列表 -> 筛选View
 * @version V1.0
 */
package com.soubao.tpshop.fragment;

import android.app.Activity;
import android.net.Uri;
import android.nfc.cardemulation.OffHostApduService;
import android.os.Bundle;
;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.shop.SPProductListActivity;
import com.soubao.tpshop.activity.shop.SPProductListSearchResultActivity;
import com.soubao.tpshop.adapter.SPProductListFilterAdapter;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.model.shop.SPFilter;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.view.tagview.Tag;
import com.soubao.tpshop.view.tagview.TagListView;
import com.soubao.tpshop.view.tagview.TagView;

import org.androidannotations.annotations.EFragment;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SPProductListFilterFragment extends Fragment implements  TagListView.OnTagCheckedChangedListener , TagListView.OnTagClickListener {

    private String TAG = "SPProductListFilterFragment";
    private View mView;
    private TextView mTitleView;
    private ListView mFilterListv;
    private TagListView mTagListView;
    private JSONObject dataJson;
    private List<SPFilter> mFilters;
    private SPProductListFilterAdapter mFilterAdapter;
    private static SPProductListFilterFragment instance;
    private static Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null)
        {
            initView(inflater, container);
        }
        instance = this;
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if(SPMobileApplication.getInstance().productListType == 1){
                //产品列表
                JSONObject dataJson = SPProductListActivity.getInstance().mDataJson;
                setDataSource(dataJson);
            }else{
                //搜索结果列表
                JSONObject dataJson = SPProductListSearchResultActivity.getInstance().mDataJson;
                setDataSource(dataJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initView(LayoutInflater inflater, ViewGroup container){
        mView = inflater.inflate(R.layout.fragment_spproduct_list_filter, container, false);
        mFilterListv = (ListView)mView.findViewById(R.id.product_filter_lstv);
        mFilterAdapter = new SPProductListFilterAdapter(getActivity() , this);
        mFilterListv.setAdapter(mFilterAdapter);
    }


    public void setDataSource(JSONObject jsonObject){
        if(jsonObject ==null ) return;
        if (mFilters==null){
            mFilters = new ArrayList<SPFilter>();
        }else{
            mFilters.clear();
        }
        dataJson = jsonObject;
        try {
            if (dataJson.has("menu")){
                SPFilter menuFilter = (SPFilter)dataJson.get("menu");
                mFilters.add(menuFilter);
            }
            if (dataJson.has("filter")){
                List<SPFilter> filters = (List<SPFilter>)dataJson.get("filter");
                mFilters.addAll(filters);
            }
            mFilterAdapter.setData(mFilters);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SPProductListFilterFragment getInstance(Handler handler){
        mHandler = handler;
        return instance;
    }

    @Override
    public void onTagCheckedChanged(TagView tagView, Tag tag) {
    }

    @Override
    public void onTagClick(TagView tagView, Tag tag) {
        if (mHandler!=null){
            Message msg = mHandler.obtainMessage(SPMobileConstants.MSG_CODE_FILTER_CHANGE_ACTION);
            msg.obj = tag.getValue();
            mHandler.sendMessage(msg);
        }
    }
}
