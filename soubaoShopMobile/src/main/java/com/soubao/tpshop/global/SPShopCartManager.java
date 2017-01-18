package com.soubao.tpshop.global;

import android.content.Context;
import android.content.Intent;

import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.shop.SPShopRequest;
import com.soubao.tpshop.utils.SMobileLog;

/**
 * Created by admin on 2016/6/21.
 */
public class SPShopCartManager {

    private String TAG = "SPShopCartManager";
    private static SPShopCartManager instance ;
    private int shopCount;
    private Context mContent;

    private SPShopCartManager(){

    }

    public static SPShopCartManager getInstance(Context content){
        if (instance == null){
            instance = new SPShopCartManager();
            instance.mContent = content;

            if (SPMobileApplication.getInstance().isLogined){
                instance.initData();
            }
        }
        return instance;
    }

    public void initData(){

        shopCount = 0 ;
        //hasFirstStartup = NO;
        /*SPShopRequest.getShopCartNumber(new SPSuccessListener(){
            @Override
            public void onRespone(String msg, Object response) {
                shopCount = Integer.valueOf(response.toString());
                SMobileLog.i(TAG , "SPShopRequest.getShopCartNumber : "+shopCount);
                //购物车状态改变广播
                if(mContent!=null)mContent.sendBroadcast(new Intent(SPMobileConstants.ACTION_SHOPCART_CHNAGE));
            }
        } , new SPFailuredListener(){
            @Override
            public void onRespone(String msg, int errorCode) {
                SMobileLog.i(TAG , "SPShopRequest.getShopCartNumber msg : "+msg);
            }
        });*/
    }


    /**
    操作购物车商品数量
    @remark 商品详情对购物车的操作
    Cart/addCart?goods_spec[尺码]=3&goods_spec[颜色]=4&goods_num=2&goods_id=1
     */
    public void shopCartGoodsOperation(String goodsID , String specs , int number , final SPSuccessListener success  ,final SPFailuredListener failure){

        SPShopRequest.shopCartGoodsOperation(goodsID,specs,number, new SPSuccessListener(){

            @Override
            public void onRespone(String msg, Object response) {
                if (response!=null) {
                   shopCount = Integer.valueOf(response.toString());
                    if (success!=null) {
                        success.onRespone("success" , shopCount);
                    }
                    if(mContent!=null)mContent.sendBroadcast(new Intent(SPMobileConstants.ACTION_SHOPCART_CHNAGE));
                }
            }
        },new SPFailuredListener(failure.getViewController()){

            @Override
            public void onRespone(String msg, int errorCode) {
                if (failure!=null) {
                    failure.onRespone(msg , errorCode);
                }
            }
        });


       /* [SPShopRequestManager shopCartGoodsOperationWith:goodsID specs:specs number:number success:^(NSString *msg, id responseObject) {

            if (responseObject) {

                NSString* cartNum = responseObject;

                weakSelf.shopCount = [cartNum integerValue];

                if (success) {
                    success(msg , responseObject);
                }
            }

            [[NSNotificationCenter defaultCenter] postNotificationName:NotificationShoppingCartChanged object:@"操作购物车成功"];

        } failure:^(NSString *msg, NSInteger errorCode) {
            if(failure){
                failure(msg , -1);
            }

        }];*/
    }

    public void reloadCart(){
        initData();
    }

    /**
     * 充值购物车数据
     */
    public void resetShopCart(){
        shopCount = 0;
    }

    public int getShopCount() {
        return shopCount;
    }

    public void setShopCount(int shopCount) {
        this.shopCount = shopCount;
    }
}
