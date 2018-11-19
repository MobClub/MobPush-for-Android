package com.mob.demo.mobpush.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mob.demo.mobpush.utils.SizeHelper;
import com.mob.tools.utils.ResHelper;


public class DialogShell {

	private static CommDialog dialog;
	private static Context context;

	public DialogShell(Context context) {
		this.context = context;
		dialog= new CommDialog(context, android.R.style.Theme_Dialog);
	}

	public void dismiss() {
		dialog.dismiss();
	}

	/**
	 * 界面的返回按钮提示框,
	 * @return 返回确定按钮
	 */
	public Button backDialog() {
		int resId = ResHelper.getStringRes(context, "");

		if (resId > 0) {
			dialog.setDialogText(resId);
		}
		resId = ResHelper.getStringRes(context, "");
		if (resId > 0) {
			dialog.setDialogLeftBtn(resId);
		}
		resId = ResHelper.getStringRes(context, "");
		if (resId > 0) {
			dialog.setDialogRightBtn(resId);
		}
		dialog.show();
		return  dialog.getRightBtn();
	}

	/**
	 * 发送按钮提示框
	 * @param phone
	 * @return 返回确定按钮
	 */
	public Button sendDialog(String phone) {
		TextView text;
		int resId = ResHelper.getStringRes(context, "");
		if (resId > 0) {
			dialog.setDialogTitle(resId);
		}
		resId = ResHelper.getStringRes(context, "");

		if (resId > 0) {
			dialog.setDialogText(resId);

		}
		resId = ResHelper.getStringRes(context, "");
		if (resId > 0) {
			dialog.setDialogLeftBtn(resId);
		}
		resId = ResHelper.getStringRes(context, "");
		if (resId > 0) {
			dialog.setDialogRightBtn(resId);
		}
		dialog.show();
		text = dialog.getDialogTextView();
		if(text != null){
			phone = text.getText().toString() + phone;
			text.setText(phone);
		}
		return dialog.getRightBtn();
	}

	/**
	 * 智能验证是否登陆的提示框,
	 * @return 返回确定按钮
	 */
	public Button smartDialog() {
		int resId = ResHelper.getStringRes(context, "");
		if (resId > 0) {
			dialog.setDialogTitle(resId);
		}
		resId = ResHelper.getStringRes(context, "");
		if (resId > 0) {
			dialog.setDialogText(resId);
		}
		resId = ResHelper.getStringRes(context, "");
		if (resId > 0) {
			dialog.setDialogLeftBtn(resId);
		}
		resId = ResHelper.getStringRes(context, "");
		if (resId > 0) {
			dialog.setDialogRightBtn(resId);
		}
		dialog.show();
		return  dialog.getRightBtn();
	}



	/**
	 * 文本 + 主按钮的Dialog
	 * @param desId（文本资源）
	 */
	public void sureDialog(int desId) {
		if (desId > 0) {
			dialog.setDialogText(desId);
		}
		int btnId = ResHelper.getStringRes(context, "");
		if (btnId > 0) {
			dialog.setDialogBtn(btnId);
		}
		dialog.show();
	}

	/**
	 * 标题 + 文本 + 主按钮的Dialog
	 */
	public void invalidPhoneDialog() {
		int resId = ResHelper.getStringRes(context, "");
		if (resId > 0) {
			dialog.setDialogTitle(resId);
		}
		resId = ResHelper.getStringRes(context, "");
		if (resId > 0) {
			dialog.setDialogText(resId);
		}
		int btnId = ResHelper.getStringRes(context, "");
		if (btnId > 0) {
			dialog.setDialogBtn(btnId);
		}
		dialog.show();
	}

	/**
	 * 显示两秒后自动消失的Dialog,
	 * @param desId（文本资源）
	 * @param split 资源拼接的字符串，没有拼接可设置为空
	 * @return Dialog同时返回Dialog对象，可以在使用的时候监听dialog消失的动作
	 */
	public CommDialog autoDismissDialog(int desId, String split, int second) {
		if (desId > 0) {
			dialog.setDialogText(desId);
		}
		if(!TextUtils.isEmpty(split)){
			dialog.setDialogTextSplit(split);
		}
		try {
			dialog.show();
		} catch (Throwable t){}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					dialog.dismiss();
				} catch (Throwable t){}

			}
		}, second * 1000);
		return dialog;
	}

	/**
	 * 网络请求后显示的加载框
	 */
	public static Dialog getProgressDialog(Context context) {
		Dialog dialog = new Dialog(context, android.R.style.Theme_Dialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
		LinearLayout root = new LinearLayout(context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		root.setLayoutParams(params);
		root.setBackgroundColor(0x00000000);
		root.setOrientation(LinearLayout.VERTICAL);

		ProgressBar bar = new ProgressBar(context);
		LinearLayout.LayoutParams barParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		bar.setLayoutParams(barParams);
		bar.setBackgroundColor(0x00000000);
		SizeHelper.prepare(context);
		int padding = SizeHelper.fromPxWidth(20);
		bar.setPadding(padding, padding, padding, padding);
		root.addView(bar);
		if (root != null) {
			dialog.setContentView(root);
			return dialog;
		}
		return dialog;
	}

}
