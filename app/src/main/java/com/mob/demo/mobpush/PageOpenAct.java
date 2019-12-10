package com.mob.demo.mobpush;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.demo.mobpush.dialog.DialogShell;
import com.mob.demo.mobpush.req.SimulateRequest;
import com.mob.demo.mobpush.utils.NetWorkHelper;
import com.mob.pushsdk.MobPushCallback;
import com.mob.tools.FakeActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class PageOpenAct extends FakeActivity implements View.OnClickListener {
	private EditText etContent;
	/**
	 * 指定Link类型
	 */
	private int openType = 1;
	/**
	 * Link指定页面1
	 */
	private TextView tvLinkOne;
	/**
	 * Link指定页面2
	 */
	private TextView tvLinkTwo;

	protected int onSetTheme(int resid, boolean atLaunch) {
		return super.onSetTheme(android.R.style.Theme_Light_NoTitleBar, atLaunch);
	}

	public void onCreate() {
		super.onCreate();
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		activity.setContentView(R.layout.page_open_act);
		TextView tvTitle = findViewById(R.id.tvTitle);
		tvTitle.setText(R.string.open_act_title);
		etContent = findViewById(R.id.etContent);

		findViewById(R.id.ivBack).setOnClickListener(this);
		findViewById(R.id.btnTest).setOnClickListener(this);

		tvLinkOne = findViewById(R.id.tvAppPush);
		tvLinkTwo = findViewById(R.id.tvNotify);

		tvLinkOne.setOnClickListener(this);
		tvLinkTwo.setOnClickListener(this);
	}

	public void onClick(View v) {
		int vId = v.getId();
		switch (vId) {
			case R.id.ivBack: {
				finish();
			}
			break;
			case R.id.tvAppPush:
			case R.id.tvNotify: {
				initView();
				((TextView) v).setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_choose, 0);
				openType = Integer.valueOf((String) v.getTag());
			}
			break;
			case R.id.btnTest: {
				//通知栏显示的通知内容
				String content = etContent.getText().toString();
				if (TextUtils.isEmpty(content)) {
					Toast.makeText(getContext(), R.string.toast_input_not_allowed_null, Toast.LENGTH_SHORT).show();
					return;
				}
				//指定跳转界面的Uri
				String openScheme = null;
				switch (openType) {
					case 1: {
						openScheme = "mlink://com.mob.mobpush.linkone";
					}
					break;
					case 2: {
						openScheme = "mlink://com.mob.mobpush.linktwo";
					}
					break;
				}

				//需要传输到link指定界面的数据
				JSONObject data = new JSONObject();
				try {
					data.put("product", "MobPush");
					data.put("company", "Mob");
					data.put("target", openType == 1 ? "LinkOne" : "LinkTwo");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				SimulateRequest.sendPush(1, content, 0, null, null,openScheme, data.toString(), new MobPushCallback<Boolean>() {
					public void onCallback(Boolean result) {
						if (result) {
							new DialogShell(getContext()).autoDismissDialog(R.string.toast_notify_open_act, null, 2);
						} else if (!NetWorkHelper.netWorkCanUse(null)) {
							new DialogShell(getContext()).autoDismissDialog(R.string.error_network, null, 2);
						} else {
							new DialogShell(getContext()).autoDismissDialog(R.string.error_ukonw, null, 2);
						}
					}
				});
			}
			break;
		}

	}

	private void initView() {
		tvLinkOne.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
		tvLinkTwo.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
	}
}
