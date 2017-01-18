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
 * Description:Activity 订单列表
 * @version V1.0
 */
package com.soubao.tpshop.activity.person.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.common.SPOrderBaseActivity;
import com.soubao.tpshop.adapter.SPOrderListAdapter;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.person.SPPersonRequest;
import com.soubao.tpshop.model.order.SPOrder;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPConfirmDialog;
import com.soubao.tpshop.utils.SPOrderUtils;
import com.soubao.tpshop.utils.SPOrderUtils.OrderStatus;
import com.soubao.tpshop.utils.SPStringUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by admin on 2016/7/1.
 */
@EActivity(R.layout.order_list)
public class SPOrderListActivity extends SPOrderBaseActivity implements SPConfirmDialog.ConfirmDialogListener {

    private String TAG = "SPOrderListActivity";

    @ViewById(R.id.order_listv)
    ListView orderListv;

    @ViewById(R.id.test_list_view_frame)
    PtrClassicFrameLayout ptrClassicFrameLayout;


    OrderStatus orderStatus;    //订单类型:
    List<SPOrder> orders;
    private SPOrder currentSelectOrder;    //选中的订单

    /**
     *  弹框类型 , 1: 取消框; 2:收货确认
     */
    int alertType ;
    int pageIndex;   //当前第几页:从1开始
    /**
      *  最大页数
     */
    boolean maxIndex;

    SPOrderListAdapter mAdapter;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            SPOrder order = (SPOrder)msg.obj;
            switch (msg.what){
                case SPMobileConstants.tagPay:
                    gotoPay(order);
                    break;
                case SPMobileConstants.tagCancel:
                    currentSelectOrder = order;
                    cancelOrder(order);
                    break;
                case SPMobileConstants.tagCustomer:
                    connectCustomer();
                    break;
                case SPMobileConstants.tagShopping:
                    lookShopping(order);
                    break;
                case SPMobileConstants.tagReceive:
                    confirmReceive(order);
                    break;
                case SPMobileConstants.tagReturn:
                    break;
                case SPMobileConstants.tagBuyAgain:
                    gotoProductDetail(order.getOrderID());
                    break;
                case SPMobileConstants.MSG_CODE_ORDER_LIST_ITEM_ACTION:
                    Intent detailIntent = new Intent(SPOrderListActivity.this  , SPOrderDetailActivity_.class);
                    detailIntent.putExtra("orderId", order.getOrderID());
                    startActivity(detailIntent);
                    break;
            }
        }
    };

    /**
     * 取消订单
     * @param order
     */
    public void cancelOrder(SPOrder order){
        showConfirmDialog("确定取消订单", "订单提醒", this, SPMobileConstants.tagCancel);

    }

    /**
     * 确认收货
     * @param order
     */
    public void confirmReceive(SPOrder order){

        showLoadingToast("正在操作");
        confirmOrderWithOrderID(order.getOrderID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                showToast(msg);
                refreshData();
            }
        }, new SPFailuredListener(SPOrderListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true , true , "订单列表");
        super.onCreate(savedInstanceState);
    }


    @AfterViews
    public void init(){
        super.init();
    }

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {

        if (getIntent()!=null){
            int value = getIntent().getIntExtra("orderStatus" , 0);
            orderStatus = SPOrderUtils.getOrderStatusByValue(value);
        }

        String title = SPOrderUtils.getOrderTitlteWithOrderStatus(orderStatus);
        setTitle(title);


        mAdapter = new SPOrderListAdapter(this , mHandler);
        orderListv.setAdapter(mAdapter);
        refreshData();
    }

    @Override
    public void initEvent() {

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉刷新
                refreshData();
            }
        });

        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                //上拉加载更多
                loadMoreData();
            }
        });

        orderListv.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SPOrder order = (SPOrder)mAdapter.getItem(position);
                Intent detailIntent = new Intent(SPOrderListActivity.this  , SPOrderDetailActivity_.class);
                detailIntent.putExtra("orderId", order.getOrderID());
                startActivity(detailIntent);
            }
        });
    }



    /**
     *  刷新数据
     */
    public void refreshData(){
        pageIndex = 1;
        maxIndex = false;
        String type = SPOrderUtils.getOrderTypeWithOrderStatus(orderStatus);
        RequestParams params = new RequestParams();

        params.put("type" , type);
        params.put("p" , pageIndex);

        showLoadingToast();
        SPPersonRequest.getOrderListWithParams(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    orders = (List<SPOrder>) response;
                    mAdapter.setData(orders);
                    ptrClassicFrameLayout.setLoadMoreEnable(true);
                } else {
                    maxIndex = true;
                    ptrClassicFrameLayout.setLoadMoreEnable(false);
                }

                ptrClassicFrameLayout.refreshComplete();

                hideLoadingToast();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }

    public void loadMoreData(){
        if(maxIndex)
        {
            return;
        }
        pageIndex++;

        String type = SPOrderUtils.getOrderTypeWithOrderStatus(orderStatus);
        RequestParams params = new RequestParams();

        if (!SPStringUtils.isEmpty(type)) {
            params.put("type" , type);
            params.put("p" , pageIndex);
        }

        showLoadingToast();
        SPPersonRequest.getOrderListWithParams(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null && ((List<SPOrder>) response).size() > 0) {
                    List<SPOrder> tempOrders = (List<SPOrder>) response;
                    orders.addAll(tempOrders);
                    mAdapter.setData(orders);
                    ptrClassicFrameLayout.setLoadMoreEnable(true);
                }else{
                    pageIndex--;
                    maxIndex = true;
                    ptrClassicFrameLayout.setLoadMoreEnable(false);
                }
                ptrClassicFrameLayout.refreshComplete();
                hideLoadingToast();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
                pageIndex--;
            }
        });
    }


    /**
     *  处理服务器获取的数据
     */
    public void dealModel(){
        //处理product缩略图url

    }

    @Override
    public void clickOk(int actionType) {
        //用户点击了确定
        if (actionType == SPMobileConstants.tagCancel){

            showLoadingToast("正在操作");
            cancelOrder(currentSelectOrder.getOrderID(), new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingToast();
                    showToast(msg);
                    refreshData();
                }
            }, new SPFailuredListener(SPOrderListActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingToast();
                    showToast(msg);
                }
            });
        }
    }


}

































































