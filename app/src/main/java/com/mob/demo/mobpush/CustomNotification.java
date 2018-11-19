package com.mob.demo.mobpush;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;

import com.mob.MobSDK;
import com.mob.pushsdk.MobPushCustomNotification;
import com.mob.pushsdk.MobPushNotifyMessage;

/**
 * Created by jychen on 2018/4/3.
 */

public class CustomNotification implements MobPushCustomNotification {

	//TODO 代码格式：常量名所有字母大写，单词间以下划线分割，待修改
	private static final String CHANNELID = "mobpush_notify";
	private static final String CHANNELNAME = "Channel";

	@Override
	public Notification getNotification(Context context, NotificationManager notificationManager, long when, String tickerText, String title,
										String content, int flag, int style, String styleContent, String[] inboxStyleContent, boolean voice, boolean shake, boolean light) {
		//TODO 此处设置点击要启动的app
		PendingIntent pi = PendingIntent.getActivity(context, 1001, new Intent(context, MainActivity.class), flag);
		//通知必须设置：小图标、标题、内容

		Notification.Builder builder = null;
		if (Build.VERSION.SDK_INT >= 26) {
			NotificationChannel notificationChannel = new NotificationChannel(CHANNELID,
					CHANNELNAME, NotificationManager.IMPORTANCE_DEFAULT);
			notificationChannel.enableLights(true); //是否在桌面icon右上角展示小红点
			notificationChannel.setLightColor(Color.GREEN); //小红点颜色
			notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
			notificationManager.createNotificationChannel(notificationChannel);
			builder = new Notification.Builder(MobSDK.getContext(), CHANNELID);
		} else{
			builder = new Notification.Builder(MobSDK.getContext());
		}

//		Notification.Builder builder = new Notification.Builder(context);
		if (android.os.Build.VERSION.SDK_INT >= 21) {
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
		if (android.os.Build.VERSION.SDK_INT >= 16) {
			if (style == MobPushNotifyMessage.STYLE_BIG_TEXT) {    //大段文本
				Notification.BigTextStyle textStyle = new Notification.BigTextStyle();
				textStyle.setBigContentTitle(title).bigText(styleContent);
				builder.setStyle(textStyle);
			} else if (style == MobPushNotifyMessage.STYLE_INBOX) {//收件箱
				Notification.InboxStyle textStyle = new Notification.InboxStyle();
				textStyle.setBigContentTitle(title);
				if (inboxStyleContent != null && inboxStyleContent.length > 0) {
					for (String item : inboxStyleContent) {
						if (item == null) {
							item = "";
						}
						textStyle.addLine(item);
					}
				}
				builder.setStyle(textStyle);
			} else if (style == MobPushNotifyMessage.STYLE_BIG_PICTURE) {//大图类型
				Notification.BigPictureStyle textStyle = new Notification.BigPictureStyle();
				Bitmap bitmap = BitmapFactory.decodeFile(styleContent);
				if (bitmap != null) {
					textStyle.setBigContentTitle(title).bigPicture(bitmap);
				}
				builder.setStyle(textStyle);
			}
		}
		return Build.VERSION.SDK_INT >= 16 ? builder.build() : builder.getNotification();
//						return getNotification(builder);
	}


}
