package com.mob.demo.mobpush;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCallback;
import com.mob.pushsdk.MobPushCustomMessage;
import com.mob.pushsdk.MobPushNotifyMessage;
import com.mob.pushsdk.MobPushReceiver;
import com.mob.tools.FakeActivity;

public class PageInner extends FakeActivity implements View.OnClickListener {
	private MobPushReceiver receiver;
	private EditText etContent;

	protected int onSetTheme(int resid, boolean atLaunch) {
		return super.onSetTheme(android.R.style.Theme_Light_NoTitleBar, atLaunch);
	}

	public void onCreate() {
		super.onCreate();
		activity.setContentView(R.layout.page_inner);

		TextView tvTitle = findViewById(R.id.tvTitle);
		tvTitle.setText(R.string.item_inner);
		etContent = findViewById(R.id.etContent);

		findViewById(R.id.ivBack).setOnClickListener(this);
		findViewById(R.id.btnTest).setOnClickListener(this);

		receiver = new MobPushReceiver() {
			public void onCustomMessageReceive(final Context context, final MobPushCustomMessage message) {
				if (message != null) {
					new PushPopWindow(activity, message.getContent()).show();
				}
			}

			@Override
			public void onNotifyMessageReceive(Context context, MobPushNotifyMessage message) {

			}

			@Override
			public void onNotifyMessageOpenedReceive(Context context, MobPushNotifyMessage message) {

			}

			@Override
			public void onTagsCallback(Context context, String[] tags, int operation, int errorCode) {

			}

			@Override
			public void onAliasCallback(Context context, String alias, int operation, int errorCode) {

			}
		};
		MobPush.addPushReceiver(receiver);
	}

	public void onClick(View v) {
		int vId = v.getId();
		switch (vId) {
			case R.id.ivBack: {
				finish();
			} break;
			case R.id.btnTest: {
				String content = etContent.getText().toString();
				if (TextUtils.isEmpty(content)) {
					Toast.makeText(getContext(), R.string.toast_input_not_allowed_null, Toast.LENGTH_SHORT).show();
					return;
				}
				SimulateRequest.sendPush(2, content, 0, new MobPushCallback<Boolean>() {
					public void onCallback(Boolean result) {
						Toast.makeText(getContext(), result ? R.string.toast_send_success : R.string.toast_send_failed, Toast.LENGTH_SHORT).show();
					}
				});
			} break;
		}
	}

	public void onDestroy() {
		MobPush.removePushReceiver(receiver);
	}
}
