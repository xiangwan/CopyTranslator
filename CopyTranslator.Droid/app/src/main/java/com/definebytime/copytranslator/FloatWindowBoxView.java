package com.definebytime.copytranslator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class FloatWindowBoxView extends LinearLayout {

	/**
	 * 记录大悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录大悬浮窗的高度
	 */
	public static int viewHeight;

	public FloatWindowBoxView(final Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.float_box, this);
		View view = findViewById(R.id.tvMsg);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
		Button close = (Button) findViewById(R.id.btnClose);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyWindowManager.removeBoxWindow(context);
				MyWindowManager.createBarWindow(context);
			}
		});
	}
}
