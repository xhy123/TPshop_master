package com.tpshop.common;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.soubao.tpshop.SPMainActivity;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPStringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Checker {
	private String TAG = "'Checker";
	static {
		System.loadLibrary("curl");
        System.loadLibrary("SPMobile");
    }
	
	public static native boolean Init();    
    public static native int Check(String header, String url);    
    public static native void Finished();
    public void responseMessage(String mesage){
       try {
            if(!SPStringUtils.isEmpty(mesage) && SPMainActivity.getmInstance() != null && SPMainActivity.getmInstance().mHandler != null ){
                Handler handler = SPMainActivity.getmInstance().mHandler;
                Message message = handler.obtainMessage(SPMobileConstants.MSG_CODE_SHOW);
                JSONObject jsonObject = new JSONObject(mesage);
                if(jsonObject.has("msg")){
                    message.obj = jsonObject.getString("msg");
                }
                handler.sendMessage(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
