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
 * Date: @date 2015年10月28日 下午9:31:20
 * Description: 用户中心数据接口
 * @version V1.0
 */

package com.soubao.tpshop.http.person;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.model.SPCommentCondition;
import com.soubao.tpshop.model.SPProduct;
import com.soubao.tpshop.model.order.SPExchange;
import com.soubao.tpshop.model.person.SPConsigneeAddress;
import com.soubao.tpshop.model.order.SPOrder;
import com.soubao.tpshop.model.person.SPRegionModel;
import com.soubao.tpshop.model.person.SPWalletLog;
import com.soubao.tpshop.model.shop.SPCollect;
import com.soubao.tpshop.model.shop.SPCoupon;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.soubao.tpshop.utils.SPStringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 2016/6/21.
 */
public class SPPersonRequest {

    private static String TAG = "SPPersonRequest";

    /**
     * 收藏/取消收藏商品
     * @URL index.php?m=Api&c=Goods&a=collectGoods
     * @param goodsID
     * @param type 操作类型: 0 添加收藏 1 删除收藏 , 该值为nil也代表收藏商品
     * @param successListener
     * @param failuredListener
     * @throws JSONException
     */
    public static void collectOrCancelGoodsWithID( String goodsID , String type , final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);
        String url =  SPMobileHttptRequest.getRequestUrl("Goods", "collectGoods");

        RequestParams params = new RequestParams();
        if(!SPStringUtils.isEmpty(goodsID)){
            params.put("goods_id" , goodsID);
        }
        if(!SPStringUtils.isEmpty(type)){
            params.put("type" , type);
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject dataJson = new JSONObject();
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    if (status > 0) {
                        successListener.onRespone(msg, msg);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 商品收藏列表
     * @URL index.php?m=Api&c=User&a=getGoodsCollect
     * @param successListener
     * @param failuredListener
     * @throws JSONException
     */
    public static void getGoodsCollectWithSuccess( final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);
        String url =  SPMobileHttptRequest.getRequestUrl("User", "getGoodsCollect");

        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);

                    if (status > 0) {
                        JSONArray resultArray = response.getJSONArray(SPMobileConstants.Response.RESULT);
                        List<SPCollect> collects = SPJsonUtil.fromJsonArrayToList(resultArray, SPCollect.class);
                        successListener.onRespone(msg, collects);
                    } else {
                        failuredListener.handleResponse(msg , status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG , "onFailure->headers.toString()");
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable){
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }


    /**
     *  @URL  index.php/Api/User/allAddress
     *  获取地址列表
     */
    public static void getAllAddress( final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);
        String url =  SPMobileHttptRequest.getRequestUrl("User", "allAddress");

        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);

                    String startDate = SPCommonUtils.getDateFullTime(System.currentTimeMillis());
                    if (status > 0) {
                        List<SPRegionModel> regions = SPJsonUtil.fromJsonArrayToList( response.getJSONArray(SPMobileConstants.Response.RESULT) , SPRegionModel.class);
                        successListener.onRespone(msg, regions);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

        });
    }

    /**
     *  @URL  index.php/Api/User/getAddressList
     *  获取收货人列表
     */
    public static void getConsigneeAddressList( final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);
        String url =  SPMobileHttptRequest.getRequestUrl("User", "getAddressList");

        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);

                    if (status > 0) {
                        List<SPConsigneeAddress> consignees = SPJsonUtil.fromJsonArrayToList( response.getJSONArray(SPMobileConstants.Response.RESULT) , SPConsigneeAddress.class);
                        successListener.onRespone(msg, consignees);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }


    /**
     *  根据订单编号获取订单详情
     *  @URL index.php?m=Api&c=User&a=getOrderDetail
     *  @param successListener success description
     *  @param failuredListener failure description
     */
    public static void getOrderDetail(String orderID , final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);
        String url =  SPMobileHttptRequest.getRequestUrl("User", "getOrderDetail");

        RequestParams params = new RequestParams();
        params.put("id", orderID);

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);

                    if (status > 0) {
                        SPOrder order  = SPJsonUtil.fromJsonToModel(response.getJSONObject(SPMobileConstants.Response.RESULT), SPOrder.class);
                        if (order!=null){
                            List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(order.getProductsArray(), SPProduct.class);
                            order.setProducts(products);
                        }
                        successListener.onRespone(msg, order);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }


    /**
     *  所有订单
     *  @URL index.php?m=Api&c=User&a=getOrderList
     *  @param successListener success description
     *  @param failuredListener failure description
     */
    public static void getOrderListWithParams(RequestParams params , final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);
        String url =  SPMobileHttptRequest.getRequestUrl("User", "getOrderList");

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);

                    if (status > 0) {
                        List<SPOrder> orders  = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(SPMobileConstants.Response.RESULT), SPOrder.class);
                        if (orders!=null){
                            for (SPOrder order : orders){
                                List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(order.getProductsArray(), SPProduct.class);
                                order.setProducts(products);
                            }
                        }
                        successListener.onRespone(msg, orders);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     *  根据订单编号获取订单详情
     *  @URL index.php?m=Api&c=User&a=getOrderDetail
     *  @param successListener success description
     *  @param failuredListener failure description
     */
    public static void getOrderDetailByID(String orderID , final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);
        String url =  SPMobileHttptRequest.getRequestUrl("User", "getOrderDetail");

        RequestParams params = new RequestParams();
        params.put("id" , orderID);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);

                    if (status > 0) {
                        SPOrder order  = SPJsonUtil.fromJsonToModel(response.getJSONObject(SPMobileConstants.Response.RESULT), SPOrder.class);
                        if (order!=null){
                            List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(order.getProductsArray(), SPProduct.class);
                            order.setProducts(products);
                        }
                        successListener.onRespone(msg, order);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     *  取消订单
     *  @URL m=Api&c=User&a=cancelOrder
     *  @param successListener success description
     *  @param failuredListener failure description
     */
    public static void cancelOrderWithOrderID(String orderID , final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);
        String url =  SPMobileHttptRequest.getRequestUrl("User", "cancelOrder");

        RequestParams params = new RequestParams();
        params.put("order_id" , orderID);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);

                    if (status > 0) {
                        successListener.onRespone(msg, msg);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     *  确认收货
     *  @URL m=Api&c=User&a=confirmOrder
     *  @param successListener success description
     *  @param failuredListener failure description
     */
    public static void confirmOrderWithOrderID(String orderID , final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);
        String url =  SPMobileHttptRequest.getRequestUrl("User", "orderConfirm");

        RequestParams params = new RequestParams();
        params.put("order_id" , orderID);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    if (status > 0) {
                        successListener.onRespone(msg, msg);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }


    /**
     *  获取退换货列表
     *  @URL index.php?m=Api&c=User&a=return_goods_list
     *  @param pageIndex :
     *  @param successListener success description
     *  @param failuredListener failure description
     */
    public static void getExchangeListWithPage(int pageIndex , final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);
        String url =  SPMobileHttptRequest.getRequestUrl("User", "return_goods_list");

        RequestParams params = new RequestParams();
        params.put("p" , pageIndex);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    if (status > 0) {
                        List<SPExchange> exchanges = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(SPMobileConstants.Response.RESULT), SPExchange.class);
                        successListener.onRespone(msg, exchanges);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }


    /**
     *  查看某个售后详情
     *  @URL index.php?m=Api&c=User&a=return_goods_info
     *  @param exchangeId :
     *  @param successListener success description
     *  @param failuredListener failure description
     */
    public static void getExchangeDetailWithId(String exchangeId , final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);
        String url =  SPMobileHttptRequest.getRequestUrl("User", "return_goods_info");

        RequestParams params = new RequestParams();
        params.put("id" , exchangeId);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    if (status > 0) {
                        SPExchange exchange = SPJsonUtil.fromJsonToModel(response.getJSONObject(SPMobileConstants.Response.RESULT), SPExchange.class);
                        List<String> images = SPMobileHttptRequest.convertJsonArrayToList(exchange.getImageArray());
                        exchange.setImages(images);
                        successListener.onRespone(msg, exchange);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     *  获取积分/余额历史记录
     *  @URL index.php?m=Api&c=User&a=account
     *  @param pageIndex :
     *  @param successListener success description
     *  @param failuredListener failure description
     */
    public static void getWalletLogsWithPage(int pageIndex , final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);

        String url =  SPMobileHttptRequest.getRequestUrl("User", "account");

        RequestParams params = new RequestParams();
        params.put("p", pageIndex);

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    if (status > 0) {
                        List<SPWalletLog> walletLogs = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(SPMobileConstants.Response.RESULT), SPWalletLog.class);
                        successListener.onRespone(msg, walletLogs);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }


    /**
     *  @URL  index.php/Api/User/addAddress
     *  添加或编辑收货地址
     *  @param params  params description
     *  @param successListener success description,n
     *  @param failuredListener failure description
     */
    public static void saveUserAddressWithParameter(RequestParams params , final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);

        String url =  SPMobileHttptRequest.getRequestUrl("User", "addAddress");

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    if (status > 0) {
                        successListener.onRespone(msg, "");
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     *  删除收货地址信息
     *  使用万能SQL: index.php?m=Api&c=User&a=del_address&id=100
     *  @param successListener success description
     *  @param failuredListener failure description
     */
    public static void delConsigneeAddressByID(String consigneeID , final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);

        String url =  SPMobileHttptRequest.getRequestUrl("User", "del_address");
        RequestParams params = new RequestParams();
        params.put("id" , consigneeID);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    if (status > 0) {
                        successListener.onRespone(msg, "1");
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     *  获取优惠券列表
     *  @URL index.php?m=Api&c=User&a=getCouponList&type=0&p=1
     *  @param successListener success description
     *  @param failuredListener failure description
     */
    public static void getCouponListWithType(int type , int page , final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);

        String url =  SPMobileHttptRequest.getRequestUrl("User", "getCouponList");

        RequestParams params = new RequestParams();
        params.put("p", page);
        params.put("type", type);


        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    if (status > 0) {
                        List<SPCoupon> coupons = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(SPMobileConstants.Response.RESULT), SPCoupon.class);
                        successListener.onRespone(msg, coupons);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     *  查看某个商品是否可以申请退换货
     *  @URL index.php?m=Api&c=User&a=return_goods_status
     *  @param successListener success description
     *  @param failuredListener failure description
     */
    public static void queryReturnGoodsStatusWithOrderId(String orderId, String goodsId, String specKey, final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);

        String url =  SPMobileHttptRequest.getRequestUrl("User", "return_goods_status");

        RequestParams params = new RequestParams();
        params.put("order_id", orderId);
        params.put("goods_id", goodsId);
        params.put("spec_key", specKey);


        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    if (status > 0) {
                        int result = response.getInt(SPMobileConstants.Response.RESULT);
                        successListener.onRespone(msg, result);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     *  商品评论(图片上传)
     *  @URL ndex.php?m=Api&c=User&a=add_comment
     *  @param successListener
     *  @param failuredListener
     */
    public static void commentGoodsWithGoodsID(SPCommentCondition commentCondition , final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);

        String url =  SPMobileHttptRequest.getRequestUrl("User", "add_comment");

        RequestParams params = new RequestParams();
        params.put("order_id", commentCondition.getOrderID());
        params.put("goods_id", commentCondition.getGoodsID());
        params.put("spec_key", commentCondition.getSpecKey());

        params.put("goods_rank", commentCondition.getGoodsRank());
        params.put("deliver_rank", commentCondition.getExpressRank());
        params.put("service_rank", commentCondition.getServiceRank());

        if (!SPStringUtils.isEmpty(commentCondition.getComment())){
            params.put("content", commentCondition.getComment());
        }

        List<File> images = commentCondition.getImages();
        if (images !=null){
            for (int i=0; i<images.size(); i++){
                File file = images.get(i);
                try {
                    params.put("img_file["+i+"]", file, "image/png");
                } catch (FileNotFoundException e) {
                    failuredListener.onRespone("file not found", -1);
                    return;
                }
            }
        }


        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    if (status > 0) {
                        String result = response.getString(SPMobileConstants.Response.RESULT);
                        successListener.onRespone(msg, result);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     *  申请退换货
     *  @URL index.php?m=Api&c=User&a=return_goods
     *  @param params :
     *  @param successListener success description
     *  @param failuredListener failure description
     */
    public static void exchangeApplyWithParameter(RequestParams params,List<File> images , final SPSuccessListener successListener,
                                               final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);

        String url =  SPMobileHttptRequest.getRequestUrl("User", "return_goods");

        if (images !=null && images.size() > 0){
            for (int i=0; i<images.size(); i++){
                File file = images.get(i);
                try {
                    params.put("img_file["+i+"]", file, "image/png");
                } catch (FileNotFoundException e) {
                    failuredListener.onRespone("file not found", -1);
                    return;
                }
            }
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    if (status > 0) {
                        String result = response.getString(SPMobileConstants.Response.RESULT);
                        successListener.onRespone(msg, result);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }
}
