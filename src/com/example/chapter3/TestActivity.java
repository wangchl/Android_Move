package com.example.chapter3;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.developart.R;

public class TestActivity extends Activity {

	private TextView txtTestButton;
	private static final String TAG = "TestActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		txtTestButton = (TextView) findViewById(R.id.txt_testButton);
		txtTestButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mHandler.sendEmptyMessageDelayed(1, 33);
			}
		});
	}

	private int mCount = 0;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mCount++;
				if (mCount <= 30) {
					float fraction = mCount / (float) 30;
					int scrollX = (int) (fraction * 100);
					txtTestButton.scrollTo(scrollX, 0);
					mHandler.sendEmptyMessageDelayed(1, 33);
				}
				break;
			default:
				break;
			}
		}
	};
}
