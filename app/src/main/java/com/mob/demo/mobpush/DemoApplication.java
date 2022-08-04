package com.mob.demo.mobpush;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.mob.MobApplication;
import com.mob.MobSDK;
import com.mob.OperationCallback;
import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCustomMessage;
import com.mob.pushsdk.MobPushNotifyMessage;
import com.mob.pushsdk.MobPushReceiver;

import java.util.concurrent.Executors;


public class DemoApplication extends MobApplication {
	private Handler handler;

	@Override
	public void onCreate() {
		super.onCreate();
		submitPolicyGrantResult();
		MobPush.addPushReceiver(new MobPushReceiver() {
			@Override
			public void onCustomMessageReceive(Context context, MobPushCustomMessage message) {
				//接收自定义消息(透传)
				System.out.println("MobPush onCustomMessageReceive:" + message.toString());
			}

			@Override
			public void onNotifyMessageReceive(Context context, MobPushNotifyMessage message) {
				//接收通知消
				System.out.println("MobPush onNotifyMessageReceive:" + message.toString());
				Message msg = new Message();
				msg.what = 1;
				msg.obj = "Message Receive:" + message.toString();
				handler.sendMessage(msg);

			}

			@Override
			public void onNotifyMessageOpenedReceive(Context context, MobPushNotifyMessage message) {
				//接收通知消息被点击事件
				System.out.println("MobPush onNotifyMessageOpenedReceive:" + message.toString());
				Message msg = new Message();
				msg.what = 1;
				msg.obj = "Click Message:" + message.toString();
				handler.sendMessage(msg);
			}

			@Override
			public void onTagsCallback(Context context, String[] tags, int operation, int errorCode) {
				//接收tags的增改删查操作
				System.out.println("MobPush onTagsCallback:" + operation + "  " + errorCode);
			}

			@Override
			public void onAliasCallback(Context context, String alias, int operation, int errorCode) {
				//接收alias的增改删查操作
				System.out.println("MobPush onAliasCallback:" + alias + "  " + operation + "  " + errorCode);
			}
		});

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				if (msg.what == 1) {
					Toast.makeText(MobSDK.getContext(), "回调信息\n" + (String) msg.obj, Toast.LENGTH_LONG).show();
					System.out.println("Callback Data:" + msg.obj);
				}
				return false;
			}
		});
	}

	private void submitPolicyGrantResult() {
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				if (MobSDK.isForb()) {
					//同意隐私协议
					MobSDK.submitPolicyGrantResult(true, new OperationCallback<Void>() {
						@Override
						public void onComplete(Void aVoid) {
							Log.d("Mobpush", "submitPolicyGrantResult  onComplete");
						}

						@Override
						public void onFailure(Throwable throwable) {
							Log.d("Mobpush", "submitPolicyGrantResult  onFailure");
						}
					});
				}
			}
		});

	}

}
