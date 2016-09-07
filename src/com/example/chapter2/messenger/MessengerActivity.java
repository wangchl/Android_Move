package com.example.chapter2.messenger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.example.developart.R;

public class MessengerActivity extends Activity {

	private static final String TAG = "MessengerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messenger);
		Intent intent = new Intent("com.example.chapter2.messenger.MessengerService.messenger");
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	/**
	 * 服务连接
	 */
	ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Messenger mService = new Messenger(service);
			Message msg = Message.obtain(null, 1);
			Bundle data = new Bundle();
			data.putString("msg", "hello,this is client.");
			msg.setData(data);
			try {
				mService.send(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			msg.replyTo = new Messenger(new Handler() {

				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case 2:
						Log.d(TAG, msg.getData().getString("replyMsg"));
						break;
					default:
						break;
					}
					super.handleMessage(msg);
				}
			});
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	};

	@Override
	protected void onDestroy() {
		unbindService(mConnection);
		super.onDestroy();
	}
}
