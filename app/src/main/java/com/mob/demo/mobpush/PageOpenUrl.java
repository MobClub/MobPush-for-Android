package com.mob.demo.mobpush;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.demo.mobpush.dialog.DialogShell;
import com.mob.demo.mobpush.req.SimulateRequest;
import com.mob.demo.mobpush.utils.NetWorkHelper;
import com.mob.pushsdk.MobPushCallback;
import com.mob.tools.FakeActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jychen on 2018/3/29.
 */

public class PageOpenUrl extends FakeActivity implements View.OnClickListener{

	//这个参数不可以修改，从demo的接口发推送，只能接收这么一个字段，其他扩展需要在后台下发
	private final static String MOB_PUSH_DEMO_URL = "url";
	private EditText etUrl;
	private EditText etContent;

	protected int onSetTheme(int resid, boolean atLaunch) {
		return super.onSetTheme(android.R.style.Theme_Light_NoTitleBar, atLaunch);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		activity.setContentView(R.layout.page_open_url);
		TextView tvTitle = findViewById(R.id.tvTitle);
		tvTitle.setText(R.string.item_media);

		findViewById(R.id.ivBack).setOnClickListener(this);
		findViewById(R.id.btnTest).setOnClickListener(this);
		etUrl = (EditText) findViewById(R.id.url);
		etContent = (EditText) findViewById(R.id.etContent);
	}

	@Override
	public void onClick(View v) {
		int vId = v.getId();
		switch (vId) {
			case R.id.ivBack: {
				finish();
			} break;
			case R.id.btnTest: {
				String content = etContent.getText().toString();
				String url = etUrl.getText().toString();
				if(TextUtils.isEmpty(url)){
					url = "http://m.mob.com";
				}
				JSONObject object = new JSONObject();
				try {
					object.put(MOB_PUSH_DEMO_URL, url);//URLEncoder.encode(url, "utf-8")
				} catch (JSONException e) {
					e.printStackTrace();
				}

				String extras = object.toString();
				if (TextUtils.isEmpty(content)) {
					Toast.makeText(getContext(), R.string.toast_input_not_allowed_null, Toast.LENGTH_SHORT).show();
//					new DialogShell(getContext()).autoDismissDialog(R.string.toast_input_not_allowed_null, null, 2);
					return;
				}
				SimulateRequest.sendPush(1, content, 0, extras.toString(), new MobPushCallback<Boolean>() {
					public void onCallback(Boolean result) {
//						Toast.makeText(getContext(), result ? R.string.toast_notify : R.string.toast_send_failed, Toast.LENGTH_SHORT).show();
						if(result) {
							new DialogShell(getContext()).autoDismissDialog(R.string.toast_app_notify, null, 2);
						} else if(!NetWorkHelper.netWorkCanUse(null)){
							new DialogShell(getContext()).autoDismissDialog(R.string.error_network, null, 2);
						} else {
							new DialogShell(getContext()).autoDismissDialog(R.string.error_ukonw, null, 2);
						}
					}
				});
			} break;
		}
	}
}
