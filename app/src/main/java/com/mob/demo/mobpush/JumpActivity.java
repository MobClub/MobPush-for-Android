package com.mob.demo.mobpush;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.Set;

/**
 * Created by jychen on 2018/4/4.
 */

public class JumpActivity extends Activity {

	private LinearLayout mainLayout;
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		setContentView(mainLayout);
		try {
			initData(getIntent());
		} catch (Throwable t){
			t.printStackTrace();
		}
	}

	private void initView(){
		mainLayout = new LinearLayout(this);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mainLayout.setLayoutParams(layoutParams);

		textView = new TextView(this);
		textView.setPadding(20, 50, 20, 50);
		textView.setTextSize(20);
		textView.setGravity(Gravity.CENTER);
		layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(20, 0, 20, 0);
		layoutParams.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
		mainLayout.addView(textView, layoutParams);
	}

	private void initData(Intent intent){
		String data = "此处显示回传的数据：\n";
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				Set<String> set = bundle.keySet();
				for(String key : set){
					Object obj = bundle.get(key);
					if(obj instanceof String){
						data = data + "key:" + key + "   value:" + bundle.getString(key) + "\n";
					} else if(obj instanceof Integer){
						data = data + "key:" + key + "   value:" + bundle.getInt(key) + "\n";
					} else if(obj instanceof Float){
						data = data + "key:" + key + "   value:" + bundle.getFloat(key) + "\n";
					} else if(obj instanceof Double){
						data = data + "key:" + key + "   value:" + bundle.getDouble(key) + "\n";
					} else if(obj instanceof Long){
						data = data + "key:" + key + "   value:" + bundle.getLong(key) + "\n";
					} else if(obj instanceof Short){
						data = data + "key:" + key + "   value:" + bundle.getShort(key) + "\n";
					} else if(obj instanceof Byte){
						data = data + "key:" + key + "   value:" + bundle.getByte(key) + "\n";
					} else if(obj instanceof Boolean){
						data = data + "key:" + key + "   value:" + bundle.getBoolean(key) + "\n";
					}
				}
			}
		}
		textView.setText(data);
	}
}
