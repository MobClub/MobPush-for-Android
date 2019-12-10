package com.mob.demo.mobpush;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.demo.mobpush.dialog.DialogShell;
import com.mob.demo.mobpush.req.SimulateRequest;
import com.mob.demo.mobpush.utils.NetWorkHelper;
import com.mob.pushsdk.MobPushCallback;
import com.mob.tools.FakeActivity;

public class PageTiming extends FakeActivity implements View.OnClickListener {
	private EditText etContent;
	private TextView tvChooseOne;
	private TextView tvChooseTwo;
	private TextView tvChooseThree;
	private TextView tvChooseFour;
	private TextView tvChooseFive;
	private CheckBox cbDefault, cbBoom, cbTech, cbWarn;
	private String soundName = "";

	private int currentChooseTime = 1;

	protected int onSetTheme(int resid, boolean atLaunch) {
		return super.onSetTheme(android.R.style.Theme_Light_NoTitleBar, atLaunch);
	}

	public void onCreate() {
		super.onCreate();
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		activity.setContentView(R.layout.page_timing);

		TextView tvTitle = findViewById(R.id.tvTitle);
		tvTitle.setText(R.string.timing_title);
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

		cbDefault = findViewById(R.id.cb_default);
		cbBoom = findViewById(R.id.cb_boom);
		cbTech = findViewById(R.id.cb_tech);
		cbWarn = findViewById(R.id.cb_warn);

		cbDefault.setOnClickListener(this);
		cbBoom.setOnClickListener(this);
		cbTech.setOnClickListener(this);
		cbWarn.setOnClickListener(this);
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
				SimulateRequest.sendPush(3, content, currentChooseTime, null, soundName, new MobPushCallback<Boolean>() {
					public void onCallback(Boolean result) {
						if (result) {
							new DialogShell(getContext()).autoDismissDialog(R.string.toast_timing, currentChooseTime + "min", 2);
						} else if (!NetWorkHelper.netWorkCanUse(null)) {
							new DialogShell(getContext()).autoDismissDialog(R.string.error_network, null, 2);
						} else {
							new DialogShell(getContext()).autoDismissDialog(R.string.error_ukonw, null, 2);
						}
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

			case R.id.cb_default:
				resetCB();
				cbDefault.setChecked(true);
				cbDefault.setTextColor(Color.WHITE);
				soundName = "";
				break;

			case R.id.cb_boom:
				resetCB();
				cbBoom.setChecked(true);
				cbBoom.setTextColor(Color.WHITE);
				soundName = "boom";
				break;

			case R.id.cb_tech:
				resetCB();
				cbTech.setChecked(true);
				cbTech.setTextColor(Color.WHITE);
				soundName = "tech";
				break;

			case R.id.cb_warn:
				resetCB();
				cbWarn.setChecked(true);
				cbWarn.setTextColor(Color.WHITE);
				soundName = "warn";
				break;
		}
	}

	private void resetCB() {
		cbDefault.setChecked(false);
		cbBoom.setChecked(false);
		cbTech.setChecked(false);
		cbWarn.setChecked(false);

		cbDefault.setTextColor(Color.BLACK);
		cbBoom.setTextColor(Color.BLACK);
		cbTech.setTextColor(Color.BLACK);
		cbWarn.setTextColor(Color.BLACK);
	}
}
