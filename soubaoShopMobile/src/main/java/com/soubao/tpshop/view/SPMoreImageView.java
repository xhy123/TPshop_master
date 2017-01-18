package com.soubao.tpshop.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SPMoreImageView extends ImageView {

	public SPMoreImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Path clipPath = new Path();
		int w = this.getWidth();
		clipPath.addCircle((float)((getRight()-getLeft()) / 2),((float)((getBottom()-getTop())) / 2), (float)(w / 2),Path.Direction.CCW); 
		canvas.clipPath(clipPath);
		super.onDraw(canvas);
	}
}
