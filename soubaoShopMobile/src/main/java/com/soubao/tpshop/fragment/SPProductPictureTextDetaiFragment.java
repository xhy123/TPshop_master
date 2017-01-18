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
 * Description:MineFragment
 * @version V1.0
 */
package com.soubao.tpshop.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.soubao.tpshop.R;


/**
 *  商品详情 -> 图文详情
 *
 */
public class SPProductPictureTextDetaiFragment extends SPBaseFragment {

	private String mHtml;
	private Context mContext;
	private WebView mWebView ;
	boolean isFirstLoad;


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
	}

	public void setContent(String content){
		this.mHtml = content;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		isFirstLoad = true;
	    View view = inflater.inflate(R.layout.common_webview_main, null,false);
		mWebView = (WebView)view.findViewById(R.id.common_webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.getSettings().setSupportMultipleWindows(true);
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.getSettings().setBuiltInZoomControls(false);
		mWebView.getSettings().setSupportZoom(false);//设定支持缩放

		loadData();
		return view;
	}

	public void loadData(){
		if (isFirstLoad && mWebView!=null) {
			mWebView.loadDataWithBaseURL(null, mHtml, "text/html", "utf-8", null);
			isFirstLoad = false;
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void initSubView(View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initData() {

	}


}
