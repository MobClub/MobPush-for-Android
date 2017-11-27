package com.mob.demo.mobpush;

import android.content.Context;

import com.mob.pushsdk.MobPushCustomMessage;
import com.mob.pushsdk.MobPushNotifyMessage;
import com.mob.pushsdk.MobPushReceiver;

/**
 * 监听消息回调
 */
public class MyPushReceiver implements MobPushReceiver {
	@Override
	public void onCustomMessageReceive(final Context context, final MobPushCustomMessage message) {
		//自定义消息回调
	}

	@Override
	public void onNotifyMessageReceive(final Context context, final MobPushNotifyMessage message) {
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
}
