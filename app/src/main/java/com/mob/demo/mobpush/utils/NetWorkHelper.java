package com.mob.demo.mobpush.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mob.MobSDK;

public class NetWorkHelper {

	/*
	 * 根据请求返回的异常信息（如果没有异常信息，则传null），判断当前网络是否可用
	 */
	public static boolean netWorkCanUse(Throwable t){
		// 获得网络状态管理器
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) MobSDK.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivityManager == null) {
				return false;
			} else {
				// 建立网络数组
				NetworkInfo[] netInfo = connectivityManager.getAllNetworkInfo();
				if (netInfo != null && netInfo.length > 0) {
					for (int i = 0; i < netInfo.length; i++) {
						// 判断获得的网络状态是否是处于连接状态
						if (netInfo[i].getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
					}
				}
			}
		} catch (Throwable e){
			return false;
		}
		return true;
	}
}
