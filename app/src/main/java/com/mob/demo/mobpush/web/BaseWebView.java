package com.mob.demo.mobpush.web;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class BaseWebView extends WebView {

	public BaseWebView(Context context) {
		super(context);
		initSettings();
	}

	public BaseWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSettings();
	}

	public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initSettings();
		if (Build.VERSION.SDK_INT >= 11) {
			removeJavascriptInterface("searchBoxJavaBridge_");
			removeJavascriptInterface("accessibility");
			removeJavascriptInterface("accessibilityTraversal");
		}
	}

	private void initSettings() {
		WebSettings settings = getSettings();
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setUseWideViewPort(true);
		settings.setJavaScriptEnabled(true);
		setVerticalScrollBarEnabled(false);
		setHorizontalScrollBarEnabled(false);
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
	}

}