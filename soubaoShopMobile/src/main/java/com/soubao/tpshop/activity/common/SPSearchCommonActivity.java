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
 * Date: @date 2015年10月20日 下午7:52:58
 * Description:Activity 支付列表
 * @version V1.0
 */
package com.soubao.tpshop.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.shop.SPProductListSearchResultActivity;
import com.soubao.tpshop.adapter.SPSearchkeyListAdapter;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.global.SPSaveData;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPStringUtils;
import com.soubao.tpshop.view.SPSearchView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/29.
 */
@EActivity(R.layout.common_search)
public class SPSearchCommonActivity extends  SPBaseActivity implements SPSearchView.SPSearchViewListener {
    private String TAG = "SPSearchCommonActivity";

    @ViewById(R.id.search_delete_btn)
    Button deleteBtn;

    @ViewById(R.id.search_key_listv)
    ListView searchkeyListv;

    @ViewById(R.id.search_view)
    SPSearchView searchView;
    SPSearchkeyListAdapter mAdapter;
    List<String> mSearchkeys;

    Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SPMobileConstants.MSG_CODE_SEARCHKEY:
                    startSearch(msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public  void init(){
        super.init();
    }

    @Override
    public void initSubViews() {
        searchView.getSearchEditText().setFocusable(true);
        searchView.setSearchViewListener(this);
    }

    @Override
    public void initData() {

        mAdapter = new SPSearchkeyListAdapter(this , mHandler);
        searchkeyListv.setAdapter(mAdapter);

        if (getIntent()!=null && getIntent().getStringExtra("searchKey") != null){
            String searchKey = getIntent().getStringExtra("searchKey");
            searchView.setSearchKey(searchKey);
        }

       /* InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(searchView.getSearchEditText()!=null)imm.showSoftInputFromInputMethod(searchView.getSearchEditText().getWindowToken(), 0);*/

        loadKey();
        mAdapter.setData(mSearchkeys);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //loadKey();
        //if(mAdapter == null)mAdapter.setData(mSearchkeys);
    }

    @Override
    public void initEvent() {
        searchkeyListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = (String) mAdapter.getItem(position);
                startSearch(key);
            }
        });

        if (searchView.getSearchEditText() != null){
            searchView.getSearchEditText().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER){
                        String searchKey = searchView.getSearchEditText().getText().toString();
                        startSearch(searchKey);
                    }
                    return false;
                }
            });
        }
    }

    @Click({R.id.search_delete_btn})
    public void onButtonClick(View v){
        switch (v.getId()){
            case R.id.search_delete_btn:
                if (searchView.getSearchEditText() != null) {
                    searchView.getSearchEditText().setText("");
                }
                //清空搜索历史
                SPSaveData.putValue(this, SPMobileConstants.KEY_SEARCH_KEY , "");
                loadKey();
                mAdapter.setData(mSearchkeys);
                break;
        }
    }

    @Override
    public void onBackClick() {
        this.finish();
    }

    @Override
    public void onSearchBoxClick(String keyword) {

    }

    public void loadKey(){
        mSearchkeys = new ArrayList<String>();
        String searchKey = SPSaveData.getString(this, SPMobileConstants.KEY_SEARCH_KEY);
        if (!SPStringUtils.isEmpty(searchKey)){
            String[] keys = searchKey.split(",");
            if (keys !=null)
                for(int i=0; i< keys.length; i++){
                    if (!SPStringUtils.isEmpty(keys[i])){
                        mSearchkeys.add(keys[i]);
                    }
                }
        }/*else{
            mSearchkeys.add("香水");
            mSearchkeys.add("充值卡");
        }*/

    }

    public void saveKey(String key){
        String searchKey = SPSaveData.getString(this, SPMobileConstants.KEY_SEARCH_KEY);
        if (!SPStringUtils.isEmpty(searchKey) && !searchKey.contains(key)) {
            searchKey+=","+key;
        }else{
            searchKey = key;
        }
        SPSaveData.putValue(this, SPMobileConstants.KEY_SEARCH_KEY, searchKey);
    }

    public void startSearch(String searchKey){
        if (!SPStringUtils.isEmpty(searchKey)) {
            saveKey(searchKey);
        }
        Intent intent = new Intent(this , SPProductListSearchResultActivity.class);
        intent.putExtra("searchKey" , searchKey);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchView.getSearchEditText() != null) {
            searchView.getSearchEditText().setFocusable(true);
            searchView.getSearchEditText().setFocusableInTouchMode(true);
        }
       // searchView.getSearchEditText().requestFocus();
    }
}
