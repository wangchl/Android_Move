package com.example.developart;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 序列化把数据写入
 * 
 */
public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState != null) {
			String test = savedInstanceState.getString("extra_test");
			Log.d(TAG, "test:" + test);
		}

		Button btnMain = (Button) findViewById(R.id.btn_main);
		btnMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent("com.example.developart.c");
				// intent.setClass(MainActivity.this, SecondActivity.class);
				intent.putExtra("time", System.currentTimeMillis());
				intent.addCategory("com.example.developart.c");
				intent.setDataAndType(Uri.parse("file://abc"), "text/plain");
				startActivity(intent);

			}
		});
		Log.d(TAG, "onCreate");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d(TAG, "onNewIntent, time=" + intent.getLongExtra("time", 0));
	}

	@Override
	protected void onRestart() {
		Log.d(TAG, "onRestart");
		super.onRestart();
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onStart();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d(TAG, "onConfigurationChanged, newOrientation:" + newConfig.orientation);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(TAG, "onSaveInstanceState");
		outState.putString("extra_test", "test");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Log.d(TAG, "onRestoreInstanceState");
		String test = savedInstanceState.getString("extra_test");
		Log.d(TAG, "[onRestoreInstanceState]restore extra_test:" + test);
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}
}
