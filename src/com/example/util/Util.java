package com.example.util;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

 
public class Util {
 
	// To hiding the keyboard from the view
	public static void hideKeyboard(Activity activity) {
		try {
			InputMethodManager inputManager = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
		}
	}

	public static void showKeyboard(Activity activity, EditText et) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(activity.INPUT_METHOD_SERVICE);
		imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
	}

	// To check the internet connection
	public static boolean haveInternet(Context thisActivity) {
		NetworkInfo info = ((ConnectivityManager) thisActivity
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			return true;
		}
		return true;
	}

 
 
	public static String getJsonObjectValue(JSONObject jsObj, String param) {
		String value = "";
		if (jsObj.has(param)) {
			try {
				value = jsObj.getString(param);
				if (value.equals("null") || TextUtils.isEmpty(value)) {
					value = "";
				}
			} catch (JSONException e) {
			}
		}

		return value;

	}

	public static String getJsonObjectValueForInteger(JSONObject jsObj,
			String param) {
		String value = "0";
		if (jsObj.has(param)) {
			try {
				value = jsObj.getString(param);
				if (value.equals("null") || TextUtils.isEmpty(value)) {
					value = "0";
				}
			} catch (JSONException e) {
			}
		}

		return value;

	}

	 
	public static void showActivityNotFinishedWithoutFlag(
			Activity thisActivity, Class<?> To) {
		Intent intent = new Intent(thisActivity, To);
		thisActivity.startActivity(intent);
	}

	 
 
}
