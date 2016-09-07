package com.example.chapter2.aidl;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.developart.R;

public class BookManagerActivity extends Activity {

	private static final String TAG = "BookManagerActivity";
	private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_manager);

		Intent intent = new Intent(BookManagerActivity.this, BookManagerService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

		Button btn1 = (Button) findViewById(R.id.btn1);
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(BookManagerActivity.this, "��ť�����", Toast.LENGTH_SHORT).show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						if (mRemoteBookManager != null) {
							try {
								List<Book> bookList = mRemoteBookManager.getBookList();
								for (Book book : bookList) {
									Log.d(TAG, "ͼ����:" + book.bookId + "   ͼ������:" + book.bookName);
								}
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
		});
	}

	private IBookManager mRemoteBookManager;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MESSAGE_NEW_BOOK_ARRIVED:
				Log.d(TAG, "receiver new book:" + msg.obj);
				break;
			default:
				super.handleMessage(msg);
			}
		}

	};

	/**
	 * ���ڽ��ܷ���˷��ص�����
	 */
	IOnNewBookArrivedListener listener = new IOnNewBookArrivedListener.Stub() {

		@Override
		public void onNewBookArrived(Book newBook) throws RemoteException {
			handler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget();
		}
	};

	/**
	 * ��������
	 */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			IBookManager bookManager = IBookManager.Stub.asInterface(service);
			mRemoteBookManager = bookManager;
			try {
				// TODO ��������֪ͨ
				mRemoteBookManager.asBinder().linkToDeath(mDeathRecipient, 0);
				List<Book> list = bookManager.getBookList();
				Log.d(TAG, "query book list,list type:" + list.getClass().getCanonicalName());
				Log.d(TAG, "query book list :" + list.toString());
				Book newBook = new Book(3, "Andorid����");
				bookManager.addBook(newBook);
				Log.d(TAG, "add book:" + newBook);
				List<Book> bookList = bookManager.getBookList();
				Log.d(TAG, "query book list:" + bookList.toString());
				bookManager.registerListener(listener);
			} catch (RemoteException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mRemoteBookManager = null;
			Log.d(TAG, "onServiceDisconnected:" + Thread.currentThread().getName());
		}
	};

	private DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {

		@Override
		public void binderDied() {
			Log.d(TAG, "Thread died:" + Thread.currentThread().getName());
			if (mRemoteBookManager == null) {
				return;
			}
			mRemoteBookManager.asBinder().unlinkToDeath(mDeathRecipient, 0);// ɾ��ע�������֪ͨ
			mRemoteBookManager = null;
			// TODO ���°�Զ�̷���
		}
	};

	protected void onDestroy() {
		unbindService(mConnection);// �����
		super.onDestroy();
	};
}
