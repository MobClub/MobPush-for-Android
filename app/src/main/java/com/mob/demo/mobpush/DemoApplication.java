package com.mob.demo.mobpush;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.mob.MobApplication;
import com.mob.MobSDK;
import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCustomMessage;
import com.mob.pushsdk.MobPushNotifyMessage;
import com.mob.pushsdk.MobPushReceiver;

import java.util.List;


public class DemoApplication extends MobApplication {
	private Handler handler;

	@Override
	public void onCreate() {
		super.onCreate();
		//防止多进程注册多次  可以在MainActivity或者其他页面注册MobPushReceiver
		String processName = getProcessName(this);
		if (getPackageName().equals(processName)) {
			MobPush.addPushReceiver(new MobPushReceiver() {
				@Override
				public void onCustomMessageReceive(Context context, MobPushCustomMessage message) {
					//接收自定义消息(透传)
					System.out.println("onCustomMessageReceive:" + message.toString());
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
					System.out.println("onTagsCallback:" + operation + "  " + errorCode);
				}

				@Override
				public void onAliasCallback(Context context, String alias, int operation, int errorCode) {
					//接收alias的增改删查操作
					System.out.println("onAliasCallback:" + alias + "  " + operation + "  " + errorCode);
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
	}

	private String getProcessName(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
		if (runningApps == null) {
			return null;
		}
		for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
			if (proInfo.pid == android.os.Process.myPid()) {
				if (proInfo.processName != null) {
					return proInfo.processName;
				}
			}
		}
		return null;
	}
}
