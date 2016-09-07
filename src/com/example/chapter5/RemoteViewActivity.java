package com.example.chapter5;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;

import com.example.developart.R;

public class RemoteViewActivity extends Activity {

	private Button btnRemoteView, btnRemoteView2;

	private static int sId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remote_view);
		initView();
		initonClickListener();
	}

	private void initView() {
		btnRemoteView = (Button) findViewById(R.id.btn_remoteView);
		btnRemoteView2 = (Button) findViewById(R.id.btn_remoteView2);

	}

	private void initonClickListener() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_remoteView:
					initNotification();
					break;
				case R.id.btn_remoteView2:
					initNotification2();
					break;
				default:
					break;
				}
			}
		};
		btnRemoteView.setOnClickListener(onClickListener);
		btnRemoteView2.setOnClickListener(onClickListener);
	}

	/**
	 * 系统自带通知栏
	 */
	@SuppressWarnings("deprecation")
	private void initNotification() {
		sId++;
		Notification notification = new Notification();
		notification.icon = R.drawable.ic_launcher;
		notification.tickerText = "您有新的消息,请注意查收!";
		notification.when = System.currentTimeMillis();
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		Intent intent = new Intent(this, DemoActivity_1.class);
		intent.putExtra("SZLD", sId + "");
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(this, "标题", "我是内容", pendingIntent);
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(sId, notification);
	}

	/**
	 * 自定义通知栏
	 */
	private void initNotification2() {
		sId++;
		Notification notification = new Notification();
		notification.icon = R.drawable.ic_launcher;
		notification.tickerText = "您有新的消息,请注意查收!";
		notification.when = System.currentTimeMillis();
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		Intent intent = new Intent(this, DemoActivity_2.class);
		intent.putExtra("SZLD", sId + "");
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
		remoteViews.setTextViewText(R.id.msg, "大家好");
		remoteViews.setImageViewResource(R.id.icon, R.drawable.ic_launcher);
		PendingIntent openActivity2PendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				DemoActivity_2.class), PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.open_activity2, openActivity2PendingIntent);
		notification.contentView = remoteViews;
		notification.contentIntent = pendingIntent;
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(sId, notification);
	}
}