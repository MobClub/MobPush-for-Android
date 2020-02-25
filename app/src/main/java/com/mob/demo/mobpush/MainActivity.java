package com.mob.demo.mobpush;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.mob.demo.mobpush.utils.PlayloadDelegate;
import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCallback;

public class MainActivity extends Activity implements View.OnClickListener {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.llAppNotify).setOnClickListener(this);
		findViewById(R.id.llNotify).setOnClickListener(this);
		findViewById(R.id.llTiming).setOnClickListener(this);
		findViewById(R.id.llLocal).setOnClickListener(this);
		findViewById(R.id.llMedia).setOnClickListener(this);
		findViewById(R.id.llOpenAct).setOnClickListener(this);

		//获取注册id
		MobPush.getRegistrationId(new MobPushCallback<String>() {
			@Override
			public void onCallback(String data) {
				System.out.println(Thread.currentThread().getId() + " RegistrationId:" + data);
			}
		});

		if (Build.VERSION.SDK_INT >= 21) {
			MobPush.setNotifyIcon(R.mipmap.default_ic_launcher);
		} else {
			MobPush.setNotifyIcon(R.mipmap.ic_launcher);
		}

		dealPushResponse(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//需要调用setIntent方法，不然后面获取到的getIntent都试上一次传的数据
		setIntent(intent);
		dealPushResponse(intent);
	}

	private void dealPushResponse(Intent intent) {
		Bundle bundle = null;
		if (intent != null) {
			bundle = intent.getExtras();
			new PlayloadDelegate().playload(this, bundle);
		}
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
}
