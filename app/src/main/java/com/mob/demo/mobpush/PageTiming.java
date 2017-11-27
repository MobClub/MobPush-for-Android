package com.mob.demo.mobpush;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.pushsdk.MobPushCallback;
import com.mob.tools.FakeActivity;

public class PageTiming extends FakeActivity implements View.OnClickListener {
	private EditText etContent;
	private TextView tvChooseOne;
	private TextView tvChooseTwo;
	private TextView tvChooseThree;
	private TextView tvChooseFour;
	private TextView tvChooseFive;

	private int currentChooseTime = 1;

	protected int onSetTheme(int resid, boolean atLaunch) {
		return super.onSetTheme(android.R.style.Theme_Light_NoTitleBar, atLaunch);
	}

	public void onCreate() {
		super.onCreate();
		activity.setContentView(R.layout.page_timing);

		TextView tvTitle = findViewById(R.id.tvTitle);
		tvTitle.setText(R.string.item_timing);
		etContent = findViewById(R.id.etContent);

		tvChooseOne = findViewById(R.id.tvChooseOne);
		tvChooseTwo = findViewById(R.id.tvChooseTwo);
		tvChooseThree = findViewById(R.id.tvChooseThree);
		tvChooseFour = findViewById(R.id.tvChooseFour);
		tvChooseFive = findViewById(R.id.tvChooseFive);
		tvChooseOne.setTag(1);
		tvChooseTwo.setTag(2);
		tvChooseThree.setTag(3);
		tvChooseFour.setTag(4);
		tvChooseFive.setTag(5);

		findViewById(R.id.ivBack).setOnClickListener(this);
		findViewById(R.id.btnTest).setOnClickListener(this);
		findViewById(R.id.tvChooseOne).setOnClickListener(this);
		findViewById(R.id.tvChooseTwo).setOnClickListener(this);
		findViewById(R.id.tvChooseThree).setOnClickListener(this);
		findViewById(R.id.tvChooseFour).setOnClickListener(this);
		findViewById(R.id.tvChooseFive).setOnClickListener(this);

		tvChooseOne.performClick();
	}

	public void onClick(View v) {
		int vId = v.getId();
		switch (vId) {
			case R.id.ivBack: {
				finish();
			}
			break;
			case R.id.btnTest: {
				String content = etContent.getText().toString();
				if (TextUtils.isEmpty(content)) {
					Toast.makeText(getContext(), R.string.toast_input_not_allowed_null, Toast.LENGTH_SHORT).show();
					return;
				}
				SimulateRequest.sendPush(3, content, currentChooseTime, new MobPushCallback<Boolean>() {
					public void onCallback(Boolean result) {
						Toast.makeText(getContext(), result ? activity.getString(R.string.toast_timing, currentChooseTime + "min")
								: activity.getString(R.string.toast_send_failed), Toast.LENGTH_SHORT).show();
					}
				});
			}
			break;
			case R.id.tvChooseOne:
			case R.id.tvChooseTwo:
			case R.id.tvChooseThree:
			case R.id.tvChooseFour:
			case R.id.tvChooseFive: {
				tvChooseOne.setSelected(false);
				tvChooseTwo.setSelected(false);
				tvChooseThree.setSelected(false);
				tvChooseFour.setSelected(false);
				tvChooseFive.setSelected(false);
				tvChooseOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				tvChooseTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				tvChooseThree.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				tvChooseFour.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				tvChooseFive.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				v.setSelected(true);
				((TextView) v).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_choose, 0);
				currentChooseTime = (Integer) v.getTag();
			}
			break;
		}
	}
}
