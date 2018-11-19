package com.mob.demo.mobpush;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;

import com.mob.demo.mobpush.dialog.PushPopWindow;
import com.mob.demo.mobpush.web.WebViewPage;
import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCallback;
import com.mob.pushsdk.MobPushCustomMessage;
import com.mob.pushsdk.MobPushNotifyMessage;
import com.mob.pushsdk.MobPushReceiver;
import com.mob.tools.utils.Hashon;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends Activity implements View.OnClickListener, Handler.Callback {
	//这个参数不可以修改，从demo的接口发推送，只能接收这么一个字段;
	// 如果是从后台的扩展参数传，则可以随意定义；
	private final static String MOB_PUSH_DEMO_URL = "url";
	private final static String MOB_PUSH_DEMO_INTENT = "intent";
	//推送跳转指定界面获取指定界面uri的固定key字段名:
	private final static String MOB_PUSH_DEMO_LINK = "mobpush_link_k";
	private Handler handler;
	private boolean isResume = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		handler = new Handler(Looper.getMainLooper(), this);
		findViewById(R.id.llAppNotify).setOnClickListener(this);
		findViewById(R.id.llNotify).setOnClickListener(this);
		findViewById(R.id.llTiming).setOnClickListener(this);
		findViewById(R.id.llLocal).setOnClickListener(this);
		findViewById(R.id.llMedia).setOnClickListener(this);
		findViewById(R.id.llOpenAct).setOnClickListener(this);

		MobPush.addPushReceiver(new MobPushReceiver() {
			@Override
			public void onCustomMessageReceive(Context context, MobPushCustomMessage message) {
				//接收自定义消息(透传)
				System.out.println("onCustomMessageReceive:" + message.toString());
				if (message != null && isResume) {
					new PushPopWindow(MainActivity.this, message.getContent()).show();
				}
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

		dealPushResponse(getIntent());

		//获取注册id
		MobPush.getRegistrationId(new MobPushCallback<String>() {
			@Override
			public void onCallback(String data) {
				System.out.println(Thread.currentThread().getId() + " RegistrationId:" + data);
			}
		});

		MobPush.setAlias("androidios");

		MobPush.addTags(new String[]{"androidios", "android"});

		if (Build.VERSION.SDK_INT >= 21) {
			MobPush.setNotifyIcon(R.mipmap.mobpush_notification_icon);
		} else {
			MobPush.setNotifyIcon(R.mipmap.ic_launcher);
		}

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
						//通过MobPush通道、华为、小米、魅族、FCM通道（应用在前台时接收的推送），
						// 点击通知则启动应用附加数据再次进行判断处理
						//非推送link跳转，而是通过默认进入app入口页，通过msg获取传过来的数据
						MobPushNotifyMessage notifyMessage = (MobPushNotifyMessage) bundle.getSerializable(key);
						if(notifyMessage == null){
							return;
						}
						HashMap<String, String> params = notifyMessage.getExtrasMap();
						if (params != null && params.containsKey(MOB_PUSH_DEMO_URL)) {
							openUrl(params);
						} else if (params != null && params.containsKey(MOB_PUSH_DEMO_INTENT)) {
							openPage(params);
						} else if (params != null && params.containsKey(MOB_PUSH_DEMO_LINK)) {
							openAct(params);
						}
					} else if (key.equals("data")) {
						//通过推送link跳转获得数据可通过data取出传过来的数据，而不是用msg
						String data = (String) bundle.get("data");
						System.out.println("link data:" + data);
					} else if (key.equals("pluginExtra")) {
						//OPPO通道 点击通知启动应用获取附加字段，通过判断pluginExtra来处理业务
						String v = String.valueOf(bundle.get(key));
						System.out.println("receiver extra data：key:" + key + ", value:" + v);
						HashMap<String, String> map = new Hashon().fromJson(v);
						if (map != null && map.containsKey(MOB_PUSH_DEMO_URL)) {
							openUrl(map);
						} else if (map != null && map.containsKey(MOB_PUSH_DEMO_INTENT)) {
							openPage(map);
						} else if (map != null && map.containsKey(MOB_PUSH_DEMO_LINK)) {
							openAct(map);
						}
					} else if (key.equals(MOB_PUSH_DEMO_URL)) {
						//FCM 当应用在后台时，通知是Google进行处理的而MobPush无法控制，点击通知启动应用，通过判断参数来处理业务
						String v = String.valueOf(bundle.get(key));
						System.out.println("FCM channel receiver extra data：key:" + key + ", value:" + v);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put(key, v);
						openUrl(map);
					} else if (key.equals(MOB_PUSH_DEMO_LINK)) {
						//FCM 当应用在后台时，通知是Google进行处理的而MobPush无法控制，点击通知启动应用，通过判断参数来处理业务
						String v = String.valueOf(bundle.get(key));
						System.out.println("FCM channel receiver extra data：key:" + key + ", value:" + v);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put(key, v);
						openAct(map);
					}
				}
			}
		}
	}

	private void openUrl(HashMap<String, String> params) {
		String url;
		if (!TextUtils.isEmpty(params.get(MOB_PUSH_DEMO_URL))) {
			url = params.get(MOB_PUSH_DEMO_URL);
		} else {
			url = "http://m.mob.com";
		}
		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			url = "http://" + url;
		}
		System.out.println("url:" + url);
		WebViewPage webViewPage = new WebViewPage();
		webViewPage.setJumpUrl(url);
		webViewPage.show(this, null);
	}

	private void openPage(HashMap<String, String> params) {
		//Test Code
		Intent intent = new Intent(this, JumpActivity.class);
		intent.putExtra("key1", "value1");
		intent.putExtra("key2", "value2");
		intent.putExtra("key3", "value3");
		//#Intent;component=com.mob.demo.mobpush/.JumpActivity;S.key1=value1;S.key2=value2;S.key3=value3;end

		String uri;
		if (!TextUtils.isEmpty(params.get(MOB_PUSH_DEMO_INTENT))) {
			uri = params.get(MOB_PUSH_DEMO_INTENT);
			try {
				startActivity(Intent.parseUri(uri, 0));
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	private void openAct(HashMap<String, String> params) {
		String uri = params.containsKey("mobpush_link_k") ? params.get("mobpush_link_k") : "";
		if (TextUtils.isEmpty(uri)) {
			return;
		}
		Intent intent = new Intent(null, Uri.parse(uri));
		startActivity(intent);
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
			break;

			case R.id.llOpenAct: {
				new PageOpenAct().show(this, null);
			}
			break;
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == 1) {
			//当其它dialog未关闭的时候，再次显示dialog，会造成其他dialog无法dismiss的现象，建议使用toast
//			if(PushDeviceHelper.getInstance().isNotificationEnabled()) {
//				Toast.makeText(MainActivity.this, "回调信息\n" + (String) msg.OBJ, Toast.LENGTH_SHORT).show();
//			} else {//当做比通知栏后，toast是无法显示的
//				new DialogShell(MainActivity.this).autoDismissDialog(0, "回调信息\n" + (String)msg.OBJ, 2);
//			}
			System.out.println("Callback Data:" + msg.obj);
		}
		return false;
	}

	private void diyNotify() {
//		MobPush.setCustomNotification(new CustomNotification());
	}

	@Override
	protected void onResume() {
		super.onResume();
		isResume = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		isResume = false;
	}
}
