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
 * Date: @date 2015年10月20日 下午7:13:14 
 * Description:
 * @version V1.0
 */
package com.soubao.tpshop.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.common.SPIViewController;
import com.soubao.tpshop.activity.person.user.SPLoginActivity_;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPConfirmDialog;
import com.soubao.tpshop.utils.SPDialogUtils;
import com.soubao.tpshop.utils.SPLoadingDialog;
import com.soubao.tpshop.utils.SPStringUtils;

import org.json.JSONObject;

/**
 * @author admin
 *
 */
public abstract class SPBaseFragment extends Fragment implements SPIViewController {

	SPLoadingDialog mLoadingDialog;

	JSONObject mDataJson;
	/**
	 * 跳转登录界面
	 */
	public void gotoLogin(){
		
	}

	public void init(View view){
		initSubView(view);
		initEvent();
		initData();
	}
	
	/**
	 * 取消网络请求
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param obj    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void cancelRequest(Object obj){
		
	}

	public void showToast(String msg){
		SPDialogUtils.showToast(getActivity(), msg);
	}

	public void showToastUnLogin(){
		showToast(getString(R.string.toast_person_unlogin));
	}
	public void  toLoginPage(){
		Intent loginIntent = new Intent(getActivity() , SPLoginActivity_.class);
		getActivity().startActivity(loginIntent);
	}

	public void showLoadingToast(){
		showLoadingToast(null);
	}

	public void showLoadingToast(String title){
		mLoadingDialog = new SPLoadingDialog(getActivity() , title);
		mLoadingDialog.setCanceledOnTouchOutside(false);
		mLoadingDialog.show();
	}

	public void hideLoadingToast(){
		if(mLoadingDialog !=null){
			mLoadingDialog.dismiss();
		}
	}


	public void showConfirmDialog(String message , String title , final SPConfirmDialog.ConfirmDialogListener confirmDialogListener , final int actionType){
		SPConfirmDialog.Builder builder = new SPConfirmDialog.Builder(getActivity());
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//设置你的操作事项
				if(confirmDialogListener!=null)confirmDialogListener.clickOk(actionType);
			}
		});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}
	
	/**
	 * 
	* @Description: 初始化子类视图 
	* @param view    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public abstract void initSubView(View view);
	
	public abstract void initEvent();

	public abstract void initData();

	@Override
	public void gotoLoginPage() {
		/*if (!SPStringUtils.isEmpty(msg)){
			showToast(msg);
		}*/
		SMobileLog.i("SPBaseFragment", "gotoLoginPage : "+this);
		toLoginPage();

	}
}
