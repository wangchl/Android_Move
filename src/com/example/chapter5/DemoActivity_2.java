package com.example.chapter5;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.developart.R;

public class DemoActivity_2 extends Activity {

	private Button demoSecond;

	private LinearLayout remoteViewsContent;

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			RemoteViews remoteViews = intent.getParcelableExtra("com.szld");
			if (remoteViews != null) {
				updateUI(remoteViews);
			}
		}
	};

	private void updateUI(RemoteViews remoteViews) {
		int layoutID = getResources().getIdentifier("layout_notification", "layout", getPackageName());
		View view = getLayoutInflater().inflate(layoutID, remoteViewsContent, false);
		remoteViews.reapply(this, view);
		remoteViewsContent.addView(view);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo2);
		Toast.makeText(this, getIntent().getStringExtra("SZLD"), Toast.LENGTH_SHORT).show();
		initView();
		demoSecond = (Button) findViewById(R.id.demo_second);
		demoSecond.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				initNotification();
			}
		});
	}

	private void initView() {
		remoteViewsContent = (LinearLayout) findViewById(R.id.remote_views_content);
		IntentFilter filter = new IntentFilter("com.example.developart.szld");
		registerReceiver(receiver, filter);
	}

	private void initNotification() {
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
		remoteViews.setTextViewText(R.id.msg, "msg from process:" + Process.myPid());
		remoteViews.setImageViewResource(R.id.icon, R.drawable.image1);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, DemoActivity_2.class),
				PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.open_activity2, pendingIntent);
		Intent intent = new Intent("com.example.developart.szld");
		intent.putExtra("com.szld", remoteViews);
		sendBroadcast(intent);
	}
}
