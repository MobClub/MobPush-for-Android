package com.mob.demo.mobpush;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.UserHandle;
import android.view.View;

import com.mob.demo.mobpush.utils.PlayloadDelegate;
import com.mob.pushsdk.MobPushNotifyMessage;
import com.mob.tools.utils.Hashon;

import java.util.HashMap;
import java.util.Set;



public class SplashActivity extends Activity{
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Intent intent  = new Intent(SplashActivity.this, MainActivity.class);
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
		Bundle bundle = null;
		if (intent != null) {
			bundle = intent.getExtras();
			new PlayloadDelegate().playload(this, bundle);
		}
	}
}
