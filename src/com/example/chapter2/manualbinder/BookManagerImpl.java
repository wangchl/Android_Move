package com.example.chapter2.manualbinder;

import java.util.List;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public class BookManagerImpl extends Binder implements IBookManager {

	/** Construct the stub at attach it to the interface. */
	public BookManagerImpl() {
		this.attachInterface(this, DESCRIPTOR);
	}

	/**
	 * Cast an IBinder object into an com.example.chapter2.aidl.IBookManager
	 * interface, generating a proxy if needed.
	 */
	public static IBookManager asInterface(IBinder obj) {
		if ((obj == null)) {
			return null;
		}
		IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
		if (((iin != null) && (iin instanceof IBookManager))) {
			return ((IBookManager) iin);
		}
		return new BookManagerImpl.Proxy(obj);
	}

	@Override
	public IBinder asBinder() {
		return this;
	}

	@Override
	public List<Book> getBookList() throws RemoteException {
		// TODO 待实现
		return null;
	}

	@Override
	public void addBook(Book book) throws RemoteException {
		// TODO 待实现
	}

	@Override
	public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
		switch (code) {
		case INTERFACE_TRANSACTION: {
			reply.writeString(DESCRIPTOR);
			return true;
		}
		case TRANSACTION_getBookList: {
			data.enforceInterface(DESCRIPTOR);
			List<Book> _result = this.getBookList();
			reply.writeNoException();
			reply.writeTypedList(_result);
			return true;
		}
		case TRANSACTION_addBook: {
			data.enforceInterface(DESCRIPTOR);
			Book _arg0;
			if ((0 != data.readInt())) {
				_arg0 = Book.CREATOR.createFromParcel(data);
			} else {
				_arg0 = null;
			}
			this.addBook(_arg0);
			reply.writeNoException();
			return true;
		}
		}
		return super.onTransact(code, data, reply, flags);
	}

	private static class Proxy implements IBookManager {
		private IBinder mRemote;

		Proxy(IBinder remote) {
			mRemote = remote;
		}

		@Override
		public IBinder asBinder() {
			return mRemote;
		}

		public java.lang.String getInterfaceDescriptor() {
			return DESCRIPTOR;
		}

		@Override
		public List<Book> getBookList() throws RemoteException {
			Parcel _data = Parcel.obtain();
			Parcel _reply = Parcel.obtain();
			List<Book> _result;
			try {
				_data.writeInterfaceToken(DESCRIPTOR);
				mRemote.transact(BookManagerImpl.TRANSACTION_getBookList, _data, _reply, 0);
				_reply.readException();
				_result = _reply.createTypedArrayList(Book.CREATOR);
			} finally {
				_reply.recycle();
				_data.recycle();
			}
			return _result;
		}

		@Override
		public void addBook(Book book) throws RemoteException {
			Parcel _data = Parcel.obtain();
			Parcel _reply = Parcel.obtain();
			try {
				_data.writeInterfaceToken(DESCRIPTOR);
				if ((book != null)) {
					_data.writeInt(1);
					book.writeToParcel(_data, 0);
				} else {
					_data.writeInt(0);
				}
				mRemote.transact(BookManagerImpl.TRANSACTION_addBook, _data, _reply, 0);
				_reply.readException();
			} finally {
				_reply.recycle();
				_data.recycle();
			}
		}
	}
}
