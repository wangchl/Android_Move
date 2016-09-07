package com.example.chapter2.aidl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

public class BookManagerService extends Service {

	private static final String TAG = "BookManagerService";

	private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

	private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();
	// 远程回调接口
	RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<IOnNewBookArrivedListener>();

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private Binder mBinder = new IBookManager.Stub() {

		@Override
		public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
			boolean success = mListenerList.unregister(listener);
			if (success) {
				Log.d(TAG, "unregister success");
			} else {
				Log.d(TAG, "not found,can not unregister");
			}
			final int N = mListenerList.beginBroadcast();
			mListenerList.finishBroadcast();
			Log.d(TAG, "unregisterListener,current size:" + N);
		}

		@Override
		public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
			// 注册远程回调监听器
			mListenerList.register(listener);
			final int N = mListenerList.beginBroadcast();
			mListenerList.finishBroadcast();
			Log.d(TAG, "registerListener,current size:" + N);
		}

		@Override
		public List<Book> getBookList() throws RemoteException {
			SystemClock.sleep(5000);
			return mBookList;
		}

		@Override
		public void addBook(Book book) throws RemoteException {
			mBookList.add(book);
		}

		@Override
		public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags)
				throws RemoteException {// 返回ture则可以回调,返回false不能回调
			// 设置权限
			int check = checkCallingOrSelfPermission("com.example.chapter2.aidl.ACCESS_BOOK_SERVICE");
			Log.d(TAG, "check=" + check);
			if (check == PackageManager.PERMISSION_DENIED) {
				return false;
			}
			String packageName = null;
			String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
			if (packages != null && packages.length > 0) {
				packageName = packages[0];
			}
			Log.d(TAG, "onTransact:" + packageName);
			if (!packageName.startsWith("com.example")) {
				return false;
			}
			return super.onTransact(code, data, reply, flags);
		};

	};

	@Override
	public void onCreate() {
		super.onCreate();
		mBookList.add(new Book(1, "Andorid"));
		mBookList.add(new Book(2, "IOS"));
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (!mIsServiceDestoryed.get()) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					int bookID = mBookList.size() + 1;
					Book newBook = new Book(bookID, "new Book#" + bookID);
					try {
						onNewBookArrived(newBook);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	};

	/**
	 * 发送广播通知客户端,新的图书到了
	 * 
	 * @param newBook
	 */
	private void onNewBookArrived(Book book) throws RemoteException {
		mBookList.add(book);
		final int N = mListenerList.beginBroadcast();
		for (int i = 0; i < N; i++) {
			IOnNewBookArrivedListener l = mListenerList.getBroadcastItem(i);
			if (l != null) {
				try {
					l.onNewBookArrived(book);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
		mListenerList.finishBroadcast();
	}
}
