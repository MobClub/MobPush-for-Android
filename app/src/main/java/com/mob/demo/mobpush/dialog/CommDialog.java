package com.mob.demo.mobpush.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mob.demo.mobpush.utils.SizeHelper;
import com.mob.tools.gui.RoundRectLayout;
import com.mob.tools.utils.ResHelper;

public class CommDialog extends Dialog implements OnClickListener {

	private int dialogText;
	private int dialogTitle;
	private int btnText;
	private int btnLeftText;
	private int btnRightText;
	private LinearLayout linear;
	private TextView titleView;
	private TextView textView;
	private Button btn;
	private Button btnRight;
	private Button btnLeft;
	private String split;


	public CommDialog(Context context) {
		super(context);
	}

	public CommDialog(Context context, int themeid) {
		super(context, themeid);
	}

	/** 设置dialog标题 */
	public void setDialogTitle(int title){
		this.dialogTitle = title;
	}

	/** 设置dialog文本描述 */
	public void setDialogText(int text){
		this.dialogText = text;
	}

	/** 设置dialog文本描述（拼接的内容） */
	public void setDialogTextSplit(String split){
		this.split = split;
	}

	/** 设置dialog主按钮 */
	public void setDialogBtn(int btnText){
		this.btnText = btnText;
	}

	/** 设置dialog左按钮 */
	public void setDialogLeftBtn(int btnLeftText){
		this.btnLeftText = btnLeftText;
	}

	/** 设置dialog右按钮 */
	public void setDialogRightBtn(int btnRightText){
		this.btnRightText = btnRightText;
	}

	public TextView getDialogTextView() {
		return textView;
	}

	public Button getRightBtn() {
		return btnRight;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		initView();
		RoundRectLayout roundLayout = new RoundRectLayout(getContext());
		roundLayout.setRound(30.0f);
		roundLayout.addView(linear);
		setContentView(roundLayout);
	}

	private void initView() {
		SizeHelper.prepare(getContext());
		linear = new LinearLayout(getContext());
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setGravity(Gravity.CENTER);
		linear.setBackgroundColor(Color.WHITE);
		LayoutParams lp = new LayoutParams(ResHelper.getScreenWidth(getContext()) / 4 * 3, LayoutParams.WRAP_CONTENT);
		int padding = SizeHelper.fromPxWidth(20);
		linear.setLayoutParams(lp);
		if(dialogTitle != 0){
			titleView = new TextView(getContext());
			LayoutParams titleViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			titleViewParams.topMargin = padding * 2;
			titleView.setLayoutParams(titleViewParams);
			padding = SizeHelper.fromPxWidth(20);
			titleView.setGravity(Gravity.CENTER);
			titleView.setBackgroundColor(Color.WHITE);
			titleView.setText(dialogTitle);
			titleView.setTextColor(0xff333333);
			titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeHelper.fromPxWidth(26));
			titleView.setGravity(Gravity.CENTER);
			titleView.setTypeface(Typeface.DEFAULT_BOLD);
			linear.addView(titleView);
		}

		textView = new TextView(getContext());
		LayoutParams textViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		textViewParams.setMargins(0, padding, 0, padding * 2);
		if(dialogTitle == 0) {
			textViewParams.setMargins(padding, padding * 2, padding, padding * 2);
		}

		textView.setLayoutParams(textViewParams);
		textView.setGravity(Gravity.CENTER);
		if(!TextUtils.isEmpty(split) && dialogText == 0){
			textView.setText(split);
		} else if(!TextUtils.isEmpty(split) && dialogText > 0){
			textView.setText(getContext().getString(dialogText, split));
		} else if(TextUtils.isEmpty(split) && dialogText > 0){
			textView.setText(dialogText);
		}

		textView.setTextColor(0xff333333);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeHelper.fromPxWidth(20));
		linear.addView(textView);

		if(btnText != 0 || btnLeftText != 0 || btnRightText != 0) {
			View lineHorizontal = new View(getContext());
			LayoutParams lineHorizontalParams = new LayoutParams(LayoutParams.MATCH_PARENT,
					SizeHelper.fromPxWidth(2));
			lineHorizontal.setLayoutParams(lineHorizontalParams);
			lineHorizontal.setBackgroundColor(0Xffcacaca);
			linear.addView(lineHorizontal);
		}

		if(btnText != 0) {
			btn = new Button(getContext());
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			btn.setLayoutParams(lp);
			padding = SizeHelper.fromPxWidth(20);
			btn.setPadding(0, padding, 0, padding);
			btn.setText(btnText);
			btn.setTextColor(0xff2975fe);
			btn.setTextSize(TypedValue.COMPLEX_UNIT_PX,SizeHelper.fromPxWidth(24));
			btn.setBackgroundColor(Color.WHITE);
			btn.setOnTouchListener(new View.OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						v.setBackgroundColor(0xffe8e8e8);
					} else if (event.getAction() == MotionEvent.ACTION_UP){
						v.setBackgroundColor(Color.WHITE);
					}
					return false;
				}
			});
			btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});
			linear.addView(btn, lp);
		}

		if(btnRightText != 0 && btnLeftText != 0) {
			LinearLayout btnLinear = new LinearLayout(getContext());
			btnLinear.setOrientation(LinearLayout.HORIZONTAL);
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			btnLinear.setLayoutParams(lp);

			btnLeft = new Button(getContext());
			LayoutParams btnLeftParams = new LayoutParams(0,SizeHelper.fromPxWidth(80),1);
			btnLeftParams.rightMargin = SizeHelper.fromPxWidth(1);
			btnLeft.setLayoutParams(btnLeftParams);
			padding = SizeHelper.fromPxWidth(12);
			btnLeft.setPadding(padding, padding, padding, padding);
			btnLeft.setBackgroundColor(Color.WHITE);
			btnLeft.setText(btnLeftText);
			btnLeft.setTextColor(0xff2975fe);
			btnLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,SizeHelper.fromPxWidth(24));
			btnLeft.setOnTouchListener(new View.OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						v.setBackgroundColor(0xffe8e8e8);
					} else if (event.getAction() == MotionEvent.ACTION_UP){
						v.setBackgroundColor(Color.WHITE);
					}
					return false;
				}
			});
			btnLeft.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});
			btnLinear.addView(btnLeft);

			View lineVertical = new View(getContext());
			LayoutParams lineVerticalParams = new LayoutParams(SizeHelper.fromPxWidth(2),
					LayoutParams.MATCH_PARENT);
			lineVertical.setLayoutParams(lineVerticalParams);
			lineVertical.setBackgroundColor(0Xffcacaca);
			btnLinear.addView(lineVertical);

			btnRight = new Button(getContext());
			LayoutParams btnRightParams = new LayoutParams(0,SizeHelper.fromPxWidth(80),1);
			btnRightParams.rightMargin = SizeHelper.fromPxWidth(1);
			btnRight.setLayoutParams(btnRightParams);
			btnRight.setPadding(padding, padding, padding, padding);
			btnRight.setBackgroundColor(Color.WHITE);
			btnRight.setId(ResHelper.getIdRes(getContext(), "btn_right"));
			btnRight.setText(btnRightText);
			btnRight.setTextColor(0xff2975fe);
			btnRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,SizeHelper.fromPxWidth(24));
			btnRight.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});
			btnRight.setOnTouchListener(new View.OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						v.setBackgroundColor(0xffe8e8e8);
					} else if (event.getAction() == MotionEvent.ACTION_UP){
						v.setBackgroundColor(Color.WHITE);
					}
					return false;
				}
			});
			btnLinear.addView(btnRight);

			linear.addView(btnLinear);
		}
	}

	@Override
	public void setOnDismissListener(OnDismissListener listener) {
		super.setOnDismissListener(listener);
	}

	@Override
	public void onClick(View v) {
		if(btn == v || btnLeft == v){
			dismiss();
		}
	}
}


