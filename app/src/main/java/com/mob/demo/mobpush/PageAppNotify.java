package com.mob.demo.mobpush;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.demo.mobpush.dialog.DialogShell;
import com.mob.demo.mobpush.dialog.PushPopWindow;
import com.mob.demo.mobpush.req.SimulateRequest;
import com.mob.demo.mobpush.utils.NetWorkHelper;
import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCallback;
import com.mob.pushsdk.MobPushCustomMessage;
import com.mob.pushsdk.MobPushNotifyMessage;
import com.mob.pushsdk.MobPushReceiver;
import com.mob.tools.FakeActivity;

public class PageAppNotify extends FakeActivity implements View.OnClickListener {
	private MobPushReceiver receiver;
	private EditText etContent;

	protected int onSetTheme(int resid, boolean atLaunch) {
		return super.onSetTheme(android.R.style.Theme_Light_NoTitleBar, atLaunch);
	}

	public void onCreate() {
		super.onCreate();
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		activity.setContentView(R.layout.page_app_notify);

		TextView tvTitle = findViewById(R.id.tvTitle);
		tvTitle.setText(R.string.app_notify_title);
		etContent = findViewById(R.id.etContent);

		findViewById(R.id.ivBack).setOnClickListener(this);
		findViewById(R.id.btnTest).setOnClickListener(this);

		receiver = new MobPushReceiver() {
			public void onCustomMessageReceive(final Context context, final MobPushCustomMessage message) {
				//自定义消息回调
				if (message != null) {
					new PushPopWindow(activity, message.getContent()).show();
				}
			}

			@Override
			public void onNotifyMessageReceive(Context context, MobPushNotifyMessage message) {
				//通知回调
			}

			@Override
			public void onNotifyMessageOpenedReceive(Context context, MobPushNotifyMessage message) {
				//默认通知栏通知的点击事件回调
			}

			@Override
			public void onTagsCallback(Context context, String[] tags, int operation, int errorCode) {
				//标签增删查操作回调
			}

			@Override
			public void onAliasCallback(Context context, String alias, int operation, int errorCode) {
				//别名增删查操作回调
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
					content = getContext().getResources().getString(R.string.inner_input_hint);
				}
				Toast.makeText(getContext(), R.string.toast_send_success, Toast.LENGTH_SHORT).show();
//				new DialogShell(getContext()).autoDismissDialog(R.string.toast_send_success, null, 2);
				SimulateRequest.sendPush(2, content, 0, null, new MobPushCallback<Boolean>() {
					public void onCallback(Boolean result) {
						if(!result) {
							if (!NetWorkHelper.netWorkCanUse(null)) {
								new DialogShell(getContext()).autoDismissDialog(R.string.error_network, null, 2);
							} else {
								new DialogShell(getContext()).autoDismissDialog(R.string.error_ukonw, null, 2);
							}
						}
					}
				});
			} break;
		}
	}

	public void onDestroy() {
		MobPush.removePushReceiver(receiver);
	}
}
