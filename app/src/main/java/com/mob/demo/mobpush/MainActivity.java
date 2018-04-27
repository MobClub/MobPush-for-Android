package com.mob.demo.mobpush;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;

import com.mob.demo.mobpush.web.WebViewPage;
import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCallback;
import com.mob.pushsdk.MobPushCustomMessage;
import com.mob.pushsdk.MobPushNotifyMessage;
import com.mob.pushsdk.MobPushReceiver;

import java.util.HashMap;
import java.util.Set;

public class MainActivity extends Activity implements View.OnClickListener, Handler.Callback{

	//这个参数不可以修改，从demo的接口发推送，只能接收这么一个字段;
	// 如果是从后台的扩展参数传，则可以随意定义；
	private final static String MOB_PUSH_DEMO_URL = "url";//跳转到指定页面的key（扩展字段里面）
	private final static String MOB_PUSH_DEMO_INTENT = "intent";//跳转到指定页面的key（扩展字段里面）
	private Handler handler;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		handler = new Handler(Looper.getMainLooper(), this);
		findViewById(R.id.llAppNotify).setOnClickListener(this);
		findViewById(R.id.llNotify).setOnClickListener(this);
		findViewById(R.id.llTiming).setOnClickListener(this);
		findViewById(R.id.llLocal).setOnClickListener(this);
		findViewById(R.id.llMedia).setOnClickListener(this);

		MobPush.addPushReceiver(new MobPushReceiver() {
			@Override
			public void onCustomMessageReceive(Context context, MobPushCustomMessage message) {
				//接收自定义消息(透传)
				System.out.println("MobPush onCustomMessageReceive:" + message.toString());
			}
			@Override
			public void onNotifyMessageReceive(Context context, MobPushNotifyMessage message) {
				//接收通知消息
				System.out.println("MobPush onNotifyMessageReceive:" +  message.toString());
				Message msg = new Message();
				msg.what = 1;
				msg.obj = "消息到达：" + message.toString();
				handler.sendMessage(msg);
			}

			@Override
			public void onNotifyMessageOpenedReceive(Context context, MobPushNotifyMessage message) {
				//接收通知消息被点击事件
				System.out.println("MobPush onNotifyMessageOpenedReceive:" + message.toString());
				Message msg = new Message();
				msg.what = 1;
				msg.obj = "点击消息：" + message.toString();
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
				System.out.println("MobPush onAliasCallback:" +  alias + "  " + operation + "  " + errorCode);
			}
		});
		dealPushResponse(getIntent());
		//获取注册id示例代码
//		MobPush.getRegistrationId(new MobPushCallback<String>() {
//			@Override
//			public void onCallback(String data) {
//				System.out.println(Thread.currentThread().getId() + " RegistrationId:" + data);
//			}
//		});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		dealPushResponse(intent);
		//需要调用setIntent方法，不然后面获取到的getIntent都试上一次传的数据
		setIntent(intent);
	}

	private void dealPushResponse(Intent intent) {
		Bundle bundle = null;
		if (intent != null) {
			bundle = intent.getExtras();
			if (bundle != null) {
				Set<String> keySet = bundle.keySet();
				for (String key : keySet) {
					if (key.equals("msg")) {
						MobPushNotifyMessage notifyMessage = (MobPushNotifyMessage) bundle.get(key);
						HashMap<String, String> params = notifyMessage.getExtrasMap();
						if (params != null && params.containsKey(MOB_PUSH_DEMO_URL)) {
							openUrl(params);
						} else if(params != null && params.containsKey(MOB_PUSH_DEMO_INTENT)){
							openPage(params);
						}
					}
				}
			}
		}
	}

	private void openUrl(HashMap<String, String> params){
		String url;
		if(!TextUtils.isEmpty(params.get(MOB_PUSH_DEMO_URL))) {
			url = params.get(MOB_PUSH_DEMO_URL);
		} else {
			url = "http://m.mob.com";
		}
		if(!url.startsWith("http://") && !url.startsWith("https://")){
			url = "http://" + url;
		}
		System.out.println("url:" + url);
		WebViewPage webViewPage = new WebViewPage();
		webViewPage.setJumpUrl(url);
		webViewPage.show(this, null);
	}

	/**
	 * 跳转到指定页面的示例
	 * @param params
	 */
	private void openPage(HashMap<String, String> params){
		//Test Code
		Intent intent = new Intent(this, JumpActivity.class);
		intent.putExtra("key1", "value1");
		intent.putExtra("key2", "value2");
		intent.putExtra("key3", "value3");
		//#Intent;component=com.mob.demo.mobpush/.JumpActivity;S.key1=value1;S.key2=value2;S.key3=value3;end

		String uri;
		if(!TextUtils.isEmpty(params.get(MOB_PUSH_DEMO_INTENT))) {
			uri = params.get(MOB_PUSH_DEMO_INTENT);
			try{
				startActivity(Intent.parseUri(uri, 0));
			} catch (Throwable t){
				t.printStackTrace();
			}
		}
	}

	@Override
	public void onContextMenuClosed(Menu menu) {
		super.onContextMenuClosed(menu);
	}

	public void onClick(View v) {
		int vId = v.getId();
		switch (vId) {
			case R.id.llAppNotify: {
				new PageAppNotify().show(this, null);
			}
			break;
			case R.id.llNotify: {
				new PageNotify().show(this, null);
			}
			break;
			case R.id.llTiming: {
				new PageTiming().show(this, null);
			}
			break;
			case R.id.llLocal: {
				new PageLocal().show(this, null);
			}
			break;
			case R.id.llMedia: {
				new PageOpenUrl().show(this, null);
			}
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		if(msg.what == 1){
			//当其它dialog未关闭的时候，再次显示dialog，会造成其他dialog无法dismiss的现象，建议使用toast
//			if(PushDeviceHelper.getInstance().isNotificationEnabled()) {
//				Toast.makeText(MainActivity.this, "回调信息\n" + (String) msg.obj, Toast.LENGTH_SHORT).show();
//			} else {//当关闭通知栏后，toast是无法显示的
//				new DialogShell(MainActivity.this).autoDismissDialog(0, "回调信息\n" + (String)msg.obj, 2);
//			}
			System.out.println("回调信息\n" + (String)msg.obj);
		}
		return false;
	}

	/**
	 * 自定义通知栏
	 */
	private void diyNotify(){
		MobPush.setCustomNotification(new CustomNotification());
	}
}
