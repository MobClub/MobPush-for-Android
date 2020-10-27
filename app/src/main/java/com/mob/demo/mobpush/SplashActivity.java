package com.mob.demo.mobpush;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.mob.MobSDK;
import com.mob.pushsdk.MobPushUtils;

import org.json.JSONArray;


public class SplashActivity extends Activity {
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_splash);
		handler.sendEmptyMessageDelayed(0, 3000);

		dealPushResponse(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		dealPushResponse(getIntent());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeMessages(0);
	}

	private void dealPushResponse(Intent intent) {
		if (intent != null) {
			//获取厂商打开首页点击数据
			JSONArray jsonArray = MobPushUtils.parseMainPluginPushIntent(intent);
			if (jsonArray.length() > 0) {
				Toast.makeText(MobSDK.getContext(), "点击数据：\n" + jsonArray.toString(), Toast.LENGTH_LONG).show();
				System.out.println("parseMainPluginPushIntent:" + jsonArray);
			}
		}
	}
}
