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
 * Description:Activity 收货地址列表
 * @version V1.0
 */
package com.soubao.tpshop.activity.person.address;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.common.SPBaseActivity;
import com.soubao.tpshop.activity.shop.SPConfirmOrderActivity;
import com.soubao.tpshop.adapter.SPAddressListAdapter;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.dao.SPPersonDao;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.person.SPPersonRequest;
import com.soubao.tpshop.model.person.SPConsigneeAddress;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPConfirmDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by admin on 2016/6/27.
 */
@EActivity(R.layout.person_address_list)
public class SPConsigneeAddressListActivity extends SPBaseActivity implements SPAddressListAdapter.AddressListListener , SPConfirmDialog.ConfirmDialogListener {

    private String TAG = "SPConsigneeAddressListActivity";

    @ViewById(R.id.address_listv)
    ListView addressLstv;

    @ViewById(R.id.address_list_pcl)
    PtrClassicFrameLayout ptrClassicFrameLayout;

    SPAddressListAdapter mAdapter;
    List<SPConsigneeAddress> consignees;
    SPConsigneeAddress selectConsignee;

    @Override
    public void onCreate(Bundle savedInstanceStat) {
        super.setCustomerTitle(true, true, getString(R.string.title_consignee_list));
        super.onCreate(savedInstanceStat);
    }

    @AfterViews
    public void init(){
        super.init();
    }

    @Override
    public void initSubViews() {

        addressLstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getIntent() != null && getIntent().hasExtra("getAddress")) {
                    SPConsigneeAddress consigneeAddress = consignees.get(position);
                    Intent resultIntent = new Intent(SPConsigneeAddressListActivity.this , SPConfirmOrderActivity.class);
                    resultIntent.putExtra("consignee" , consigneeAddress);
                    setResult(SPMobileConstants.Result_Code_GetAddress , resultIntent);
                    SPConsigneeAddressListActivity.this.finish();
                }
            }
        });
    }

    @Override
    public void initData() {
        mAdapter = new SPAddressListAdapter(this , this);
        addressLstv.setAdapter(mAdapter);
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉刷新
                refreshData();
            }
        });
        refreshData();
    }

    @Override
    public void initEvent() {

    }

    public void refreshData(){

        showLoadingToast();
        SPPersonRequest.getConsigneeAddressList(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    consignees = (List<SPConsigneeAddress>) response;
                    dealModels();
                    mAdapter.setData(consignees);
                }
                ptrClassicFrameLayout.refreshComplete();
                ptrClassicFrameLayout.setLoadMoreEnable(false);
                hideLoadingToast();
            }
        }, new SPFailuredListener(SPConsigneeAddressListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }

   public void dealModels(){

       if (consignees == null)return;
        for (SPConsigneeAddress consigneeAddress : consignees) {

            String firstAddress = SPPersonDao.getInstance(this).queryFirstRegion(consigneeAddress.getProvince() , consigneeAddress.getCity() ,consigneeAddress.getDistrict(), consigneeAddress.getTown());
            if (firstAddress != null){
                consigneeAddress.setFullAddress(firstAddress+consigneeAddress.getAddress());;
            }else{
                consigneeAddress.setFullAddress(consigneeAddress.getAddress());;
            }
        }
    }


    @Override
    public void onItemDelete(SPConsigneeAddress consigneeAddress) {
        selectConsignee = consigneeAddress;
        showConfirmDialog("确定删除该地址吗" , "删除提醒" , this , 1 );
    }

    @Override
    public void onItemEdit(SPConsigneeAddress consigneeAddress) {
        Intent intent = new Intent(this, SPConsigneeAddressEditActivity_.class);
        intent.putExtra("consignee" , consigneeAddress);
        startActivityForResult(intent, SPMobileConstants.Result_Code_Refresh);
    }

    @Override
    public void onItemSetDefault(SPConsigneeAddress consigneeAddress) {
        Intent intent = new Intent(this, SPConsigneeAddressEditActivity_.class);
        intent.putExtra("consignee" , consigneeAddress);
        startActivityForResult(intent, SPMobileConstants.Result_Code_Refresh);
    }

    @Click({R.id.add_address_btn})
    public void onViewClick(View v){
        if (v.getId() == R.id.add_address_btn){
            Intent intent = new Intent(this, SPConsigneeAddressEditActivity_.class);
            startActivityForResult(intent, SPMobileConstants.Result_Code_Refresh);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SPMobileConstants.Result_Code_Refresh){
            refreshData();
        }
    }

    @Override
    public void clickOk(int actionType) {

        showLoadingToast("正在删除");
        SPPersonRequest.delConsigneeAddressByID(selectConsignee.getAddressID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                showToast(msg);
                refreshData();
            }
        }, new SPFailuredListener(SPConsigneeAddressListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });


    }
}
