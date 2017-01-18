package com.soubao.tpshop.http.person;

import android.support.annotation.NonNull;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.model.person.SPUser;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPEncryptUtil;
import com.soubao.tpshop.utils.SPJsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ben on 2016/7/9.
 */
public class SPUserRequest {

    private static final String TAG = "SPUserRequest";

    /**
     * 用户登录接口
     * @param phoneNumber       手机号码
     * @param password          密码
     * @param successListener   成功回调
     * @param failuredListener  失败回调
     */
    public static void doLogin(String phoneNumber, String password, final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);
        /** 组装用户登录URL */
        String url =  SPMobileHttptRequest.getRequestUrl("User", "login");

        /** 组装请求参数 */
        RequestParams params = new RequestParams();

        try {
            String authPwd = SPMobileConstants.SP_AUTH_CODE + password;
            String md5pwd = SPEncryptUtil.md5Digest(authPwd);
            params.put("username" , phoneNumber);
            params.put("password", md5pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        /** 工具类json转为User实体 **/
                        SPUser user = SPJsonUtil.fromJsonToModel(response.getJSONObject(SPMobileConstants.Response.RESULT), SPUser.class);
                        successListener.onRespone(msg, user);
                    }else{
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    successListener.onRespone(e.getMessage(), -1);
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

    public static void doRegister(String phoneNumber, String password,String code, final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);
        String url =  SPMobileHttptRequest.getRequestUrl("User", "reg");
        RequestParams params = new RequestParams();
        params.put("username" , phoneNumber);
        params.put("password" , password);
        params.put("password2" , password);
        params.put("code" , code);

        SPMobileHttptRequest.post(url, params, getResponseHandler(successListener, failuredListener));
    }

    public static void sendSMSRegCode(String phoneNumber, final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);
        String url =  SPMobileHttptRequest.getRequestUrl("User", "send_sms_reg_code");
        RequestParams params = new RequestParams();
        params.put("mobile" , phoneNumber);

        SPMobileHttptRequest.post(url, params, getResponseHandler(successListener, failuredListener));
    }

    public static void updateUserInfo(SPUser user, final SPSuccessListener successListener, final SPFailuredListener failuredListener){
        assert(successListener!=null);
        assert(failuredListener!=null);
        String url =  SPMobileHttptRequest.getRequestUrl("User", "updateUserInfo");
        RequestParams params = new RequestParams();
        params.put("user_id" , user.getUserID());
        params.put("nickname" , user.getNickname());
        params.put("head_pic" , user.getHeadPic());
        params.put("sex" , user.getSex());
        params.put("birthday" , user.getBirthday());

        SPMobileHttptRequest.post(url, params, getResponseHandler(successListener, failuredListener));
    }

    @NonNull
    private static JsonHttpResponseHandler getResponseHandler(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject dataJson = new JSONObject();
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    if (status > 0) {
                        try {
                            JSONObject resultObject = response.getJSONObject(SPMobileConstants.Response.RESULT);
                            if (resultObject!=null) {
                                SPUser user = SPJsonUtil.fromJsonToModel(resultObject, SPUser.class);
                                successListener.onRespone(msg, user);
                            }else{
                                successListener.onRespone(msg, "success");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            successListener.onRespone(msg, "success");
                        }
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        };
    }
}
