package com.soubao.tpshop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.soubao.tpshop.R;

public class SPBorderImageView extends ImageView {

	private int color;

	public SPBorderImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		/** 获取自定义属性 titleText */
		TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BorderImageView, 0, 0);
		color = typeArray.getColor(R.styleable.BorderImageView_borderColor, context.getResources().getColor(R.color.separator_line));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//画边框
		Rect rec=canvas.getClipBounds();
		rec.bottom--;
		rec.right--;
		Paint paint=new Paint();
		paint.setColor(color);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(rec, paint);
	}
}
