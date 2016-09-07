package com.example.chapter2.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessengerService extends Service {

	private static final String TAG = "MessengerService";

	@Override
	public IBinder onBind(Intent intent) {
		return messenger.getBinder();
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Log.d(TAG, "receiver client message:" + msg.getData().getString("msg"));
				Messenger client = msg.replyTo;
				Message replyMsg = Message.obtain(null, 2);
				Bundle data = new Bundle();
				data.putString("replyMsg", "恩恩，已经收到消息");
				replyMsg.setData(data);
				try {
					client.send(replyMsg);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	private final Messenger messenger = new Messenger(mHandler);

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
}
