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
 * Description:Activity webview activity
 * @version V1.0
 */
package com.soubao.tpshop.activity.common;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.soubao.tpshop.R;
import com.soubao.tpshop.utils.SPDialogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by admin on 2016/7/4.
 */
@EActivity(R.layout.common_webview_main)
public class SPWebviewActivity extends SPBaseActivity  {

    @ViewById(R.id.common_webview)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true , true , "'");
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public  void init(){
        super.init();
        String url = "";
        if (getIntent()==null || getIntent().getStringExtra("url") == null){
            SPDialogUtils.showToast(this , getString(R.string.data_error));
            this.finish();
            return;
        }

        url = getIntent().getStringExtra("url");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);

        webView.setWebViewClient(new MyWebViewClient());
    }

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {
        String title = getIntent().getStringExtra("title");
        super.setTitle(title);
    }

    @Override
    public void initEvent() {

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        // 在WebView中而不是默认浏览器中显示页面
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }



}
