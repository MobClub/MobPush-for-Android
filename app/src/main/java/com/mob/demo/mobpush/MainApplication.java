package com.mob.demo.mobpush;

import com.mob.MobApplication;
import com.mob.pushsdk.MobPush;

public class MainApplication extends MobApplication {

	public void onCreate() {
		super.onCreate();
		//设置晚上11点到早上8点为静默时间
		MobPush.setSilenceTime(23, 0, 8, 0);
		//设置通知消息自定义通知栏显示样式
		MobPush.setCustomNotification(new MyNotification());
//		MobPush.setCustomNotification(null);//不调用或者调用设置为null时，则使用默认设置
		//设置消息接收器
		MobPush.addPushReceiver(new MyPushReceiver());
	}
}
