package com.mob.demo.mobpush;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCallback;

public class MainActivity extends Activity implements View.OnClickListener {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.llInner).setOnClickListener(this);
		findViewById(R.id.llNotify).setOnClickListener(this);
		findViewById(R.id.llTiming).setOnClickListener(this);
	}

	public void onClick(View v) {
		int vId = v.getId();
		switch (vId) {
			case R.id.llInner: {
				new PageInner().show(this, null);
			} break;
			case R.id.llNotify: {
				new PageNotify().show(this, null);
			} break;
			case R.id.llTiming: {
				new PageTiming().show(this, null);
			} break;
		}

	}
}
