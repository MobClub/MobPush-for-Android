package com.mob.demo.mobpush;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LinkTwoActivity extends Activity implements View.OnClickListener {
	private TextView tvTitle;
	private ImageView ivBack;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_link_two);
		findView();
		initView();
		initData();
	}

	private void findView(){
		tvTitle = findViewById(R.id.tvTitle);
		ivBack = findViewById(R.id.ivBack);
		tv = findViewById(R.id.tv);
	}

	private void initView(){
		tvTitle.setText(R.string.link_two_title);
		ivBack.setOnClickListener(this);
	}

	private void initData(){
		Intent intent = getIntent();
		Uri uri = intent.getData();
		StringBuilder sb = new StringBuilder();
		if (uri != null) {
			sb.append(" scheme:" + uri.getScheme() + "\n");
			sb.append(" host:" + uri.getHost() + "\n");
//			sb.append(" port:" + uri.getPort() + "\n");
//			sb.append(" query:" + uri.getQuery() + "\n");
		}

		//获取link界面传输的数据，取字段data数据
		Bundle bundle = intent.getExtras();
		if(bundle!= null && bundle.containsKey("data")){
			sb.append(" extras:" + bundle.get("data").toString());
		}
		tv.setText(sb);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ivBack: {
				finish();
			}
			break;
		}
	}
}
