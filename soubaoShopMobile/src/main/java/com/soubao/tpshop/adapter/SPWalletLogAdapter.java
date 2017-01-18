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
 * Date: @date 2015年10月30日 下午10:03:56 
 * Description:  积分金额明细 dapter
 * @version V1.0
 */
package com.soubao.tpshop.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.model.person.SPWalletLog;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;


/**
 * @author 飞龙
 *
 */
public class SPWalletLogAdapter extends BaseAdapter {

	private String TAG = "SPWalletLogAdapter";

	private List<SPWalletLog> mWalletLogs ;
	private Context mContext ;

	public SPWalletLogAdapter(Context context){
		this.mContext = context;

	}
	
	public void setData(List<SPWalletLog> walletLogs){
		if(walletLogs == null)return;
		this.mWalletLogs = walletLogs;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mWalletLogs == null)return 0;
		return mWalletLogs.size();
	}

	@Override
	public Object getItem(int position) {
		if(mWalletLogs == null) return null;
		return mWalletLogs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return -1 ;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.person_walletlog_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();

			holder.titleTxtv = ((TextView) convertView.findViewById(R.id.walletlog_title_txtv));
			holder.detailTxtv = ((TextView) convertView.findViewById(R.id.walletlog_detail_txtv));
			holder.dateTxtv = ((TextView) convertView.findViewById(R.id.walletlog_date_txtv));

			  //设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
        SPWalletLog walletLog = (SPWalletLog)mWalletLogs.get(position);

		String title = "余额:"+walletLog.getUserMoney()+" 积分:"+walletLog.getPayPoints() ;

		double userMoney = 0;
		double payPoints = 0;

		if (!SPStringUtils.isEmpty(walletLog.getUserMoney())){
			userMoney = Double.valueOf(walletLog.getUserMoney());
		}

		if (!SPStringUtils.isEmpty(walletLog.getPayPoints())){
			payPoints = Double.valueOf(walletLog.getPayPoints());
		}

		int moneyColor = R.color.green;
		int pointsColor = R.color.green;

		if(userMoney >0){
			moneyColor = R.color.light_red;
		}else{
			moneyColor = R.color.green;
		}

		if(payPoints >0){
			pointsColor = R.color.light_red;
		}else{
			pointsColor = R.color.green;
		}

		int balanceStartIndex = 3;
		int balanceEndIndex = balanceStartIndex + walletLog.getUserMoney().length();

		int pointsStartIndex = balanceEndIndex + 3;
		int pointsEndIndex = title.length();

		SpannableString titleSpanStr = new SpannableString(title);
		titleSpanStr.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(moneyColor)), balanceStartIndex, balanceEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
		titleSpanStr.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(pointsColor)), pointsStartIndex, pointsEndIndex , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色

		holder.titleTxtv.setText(titleSpanStr);

		if (walletLog.getChangeTime() != null){
			String date = SPCommonUtils.getDateFullTime(Long.valueOf(walletLog.getChangeTime()));
			holder.dateTxtv.setText(date);
		}

        return convertView;
	}
	
	class ViewHolder{
		TextView titleTxtv;
		TextView detailTxtv;
		TextView dateTxtv;
	}


}
