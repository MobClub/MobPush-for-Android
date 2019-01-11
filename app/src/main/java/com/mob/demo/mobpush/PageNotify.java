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

public class PageNotify extends FakeActivity implements View.OnClickListener {
	private EditText etContent;

	protected int onSetTheme(int resid, boolean atLaunch) {
		return super.onSetTheme(android.R.style.Theme_Light_NoTitleBar, atLaunch);
	}

	public void onCreate() {
		super.onCreate();
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		activity.setContentView(R.layout.page_notify);
		TextView tvTitle = findViewById(R.id.tvTitle);
		tvTitle.setText(R.string.notify_title);
		etContent = findViewById(R.id.etContent);

		findViewById(R.id.ivBack).setOnClickListener(this);
		findViewById(R.id.btnTest).setOnClickListener(this);
	}

	public void onClick(View v) {
		int vId = v.getId();
		switch (vId) {
			case R.id.ivBack: {
				finish();
			} break;
			case R.id.btnTest: {
				final String content = etContent.getText().toString();
				if (TextUtils.isEmpty(content)) {
					Toast.makeText(getContext(), R.string.toast_input_not_allowed_null, Toast.LENGTH_SHORT).show();
					return;
				}
				SimulateRequest.sendPush(1, content, 0, null, new MobPushCallback<Boolean>() {
					public void onCallback(Boolean result) {
						if(result) {
							new DialogShell(getContext()).autoDismissDialog(R.string.toast_notify,null,  2);
						} else if(!NetWorkHelper.netWorkCanUse(null)){
							new DialogShell(getContext()).autoDismissDialog(R.string.error_network, null, 2);
						} else {
							new DialogShell(getContext()).autoDismissDialog(R.string.error_ukonw, null, 2);
						}
					}
				});
			} break;
		}
	}
}
