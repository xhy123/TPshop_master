package com.soubao.tpshop.http.base;

import com.soubao.tpshop.activity.common.SPIViewController;
import com.soubao.tpshop.common.SPMobileConstants;

/**
 * 首先会验证token是否过期/失效,
 * 如果过期/失效会进入登录页面登录
 */
public abstract class SPFailuredListener {

    private SPIViewController viewController;

    public SPFailuredListener(){}

    public SPIViewController getViewController(){
        return viewController;
    }

    public SPFailuredListener(SPIViewController pViewController){
        viewController = pViewController;
    }

    /**
     * 预处理,
     * @param msg
     * @param errorCode
     */
    public void handleResponse(String msg , int errorCode){
        boolean isNeedLogin = preRespone(msg , errorCode);
        if (isNeedLogin){
            //去登陆
            if(viewController != null){
                viewController.gotoLoginPage();
                onRespone(msg , errorCode);
            }else{
                onRespone(msg , errorCode);
            }
        }else{
            onRespone(msg , errorCode);
        }
    }

    public abstract void onRespone(String msg , int errorCode);

    public boolean preRespone(String msg , int errorCode){
        if (errorCode == SPMobileConstants.Response.RESPONSE_CODE_TOEKN_EXPIRE
                || errorCode == SPMobileConstants.Response.RESPONSE_CODE_TOEKN_INVALID
                || errorCode == SPMobileConstants.Response.RESPONSE_CODE_TOEKN_EMPTY){
            return true;
        }
        return false;
    }

}
