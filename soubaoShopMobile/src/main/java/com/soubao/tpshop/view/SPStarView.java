package com.soubao.tpshop.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soubao.tpshop.R;

import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;

public class SPStarView extends RelativeLayout implements View.OnClickListener {

	private Context mContext;
	private Button star1Btn;
	private Button star2Btn;
	private Button star3Btn;
	private Button star4Btn;
	private Button star5Btn;
	private boolean isResponseClickListener;
	List<Button> starList;
	private int rank; //星星数量
	
	public SPStarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		starList = new ArrayList<Button>();
		View parent = LayoutInflater.from(mContext).inflate(R.layout.star_view, this, true);
		star1Btn = (Button) parent.findViewById(R.id.star1_btn);
		star1Btn.setOnClickListener(this);
		starList.add(star1Btn);

		star2Btn = (Button) parent.findViewById(R.id.star2_btn);
		star2Btn.setOnClickListener(this);
		starList.add(star2Btn);

		star3Btn = (Button) parent.findViewById(R.id.star3_btn);
		star3Btn.setOnClickListener(this);
		starList.add(star3Btn);

		star4Btn = (Button) parent.findViewById(R.id.star4_btn);
		star4Btn.setOnClickListener(this);
		starList.add(star4Btn);

		star5Btn = (Button) parent.findViewById(R.id.star5_btn);
		star5Btn.setOnClickListener(this);
		starList.add(star5Btn);

		isResponseClickListener = true;
	}


	@Override
	public void onClick(View v) {

		if (!isResponseClickListener)return;

		switch (v.getId()){
			case R.id.star1_btn:
				rank = 0;
				break;
			case R.id.star2_btn:
				rank = 1;
				break;
			case R.id.star3_btn:
				rank = 2;
				break;
			case R.id.star4_btn:
				rank = 3;
				break;
			case R.id.star5_btn:
				rank = 4;
				break;
		}
		checkStart(rank);
	}

	public int getRank(){
		return rank;
	}

	/**
	 * 选中star的索引
	 * @param
	 */
	public void checkStart(int rank){

		for (int i = 0; i<5 ; i++){
			Button starBtn = starList.get(i);
			if (i <= rank){
				starBtn.setBackgroundResource(R.drawable.icon_start_checked_normal);
			}else{
				starBtn.setBackgroundResource(R.drawable.icon_start_uncheck_normal);
			}
		}
	}

	public void setIsResponseClickListener(boolean isResponser){
		this.isResponseClickListener = isResponser;
	}

}
