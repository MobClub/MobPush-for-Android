package com.mob.demo.mobpush;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import com.mob.pushsdk.MobPushCustomNotification;

/**
 * 自定义通知栏显示
 */
public class MyNotification implements MobPushCustomNotification {
	public MyNotification() {

	}

	@Override
	public Notification getNotification(Context context, long when, String tickerText, String title, String content, int flag, boolean voice,
				boolean shake, boolean light) {
		return getMyNotification(context, when, tickerText, title, content, flag, voice, shake, light);
	}

	private Notification getMyNotification(Context context, long when, String tickerText, String title, String content, int flag,
				boolean voice, boolean shake, boolean light) {
		PendingIntent pi = PendingIntent.getActivity(context, 1001, new Intent(context, MainActivity.class), flag);
		//通知必须设置：小图标、标题、内容
		Notification.Builder builder = new Notification.Builder(context);
		if (android.os.Build.VERSION.SDK_INT >= 21){
			builder.setSmallIcon(R.mipmap.mobpush_notification_icon);
		} else {
			builder.setSmallIcon(R.mipmap.ic_launcher);
		}
		if (TextUtils.isEmpty(title)) {
			builder.setContentTitle(context.getString(R.string.app_name));
		} else {
			builder.setContentTitle(title);
		}
		builder.setContentText(content);
		builder.setTicker(tickerText);
		builder.setWhen(when);
		if (Build.VERSION.SDK_INT >= 21) {
			builder.setColor(0x00000000);
		}
		builder.setContentIntent(pi);
		builder.setAutoCancel(true);
		if (voice && shake && light) {
			builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
		} else if (voice && shake) {
			builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
		} else if (voice && light) {
			builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
		} else if (shake && light) {
			builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
		} else if (voice) {
			builder.setDefaults(Notification.DEFAULT_SOUND);
		} else if (shake) {
			builder.setDefaults(Notification.DEFAULT_VIBRATE);
		} else {
			builder.setSound(null);
			builder.setVibrate(null);
			if (light) {
				builder.setDefaults(Notification.DEFAULT_LIGHTS);
			} else {
				builder.setLights(0, 0, 0);
			}
		}
		return getNotification(builder);
	}

	private Notification getNotification(Notification.Builder builder) {
		return Build.VERSION.SDK_INT >= 16 ? builder.build() : builder.getNotification();
	}
}
