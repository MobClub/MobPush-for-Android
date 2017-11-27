package com.mob.demo.mobpush;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PushPopWindow extends PopupWindow {
	public PushPopWindow(Context context, String message) {
		init(context, message);
	}

	private void init(Context context, String message) {
		//设置宽与高
		setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		setHeight(WindowManager.LayoutParams.MATCH_PARENT);
		setAnimationStyle(android.R.style.Animation_Dialog);
		//设置背景只有设置了这个才可以点击外边和BACK消失
		setBackgroundDrawable(new ColorDrawable());
		//设置可以获取集点
		setFocusable(true);
		//设置点击外边可以消失
		setOutsideTouchable(true);
		//设置可以触摸
		setTouchable(true);

		View contentView = LayoutInflater.from(context).inflate(R.layout.pop_push_message, null);
		contentView.findViewById(R.id.btnKnown).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});

		setContentView(contentView);
		TextView tvContent = (TextView) contentView.findViewById(R.id.tvContent);
		tvContent.setText(message);
	}

	public void show() {
		showAtLocation(getContentView(), Gravity.CENTER, 0, 0);
	}
}
