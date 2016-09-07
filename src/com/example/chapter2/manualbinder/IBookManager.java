package com.example.chapter2.manualbinder;

import java.util.List;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

/**
 * 手动生成一个aidl文件
 */
public interface IBookManager extends IInterface {// 为了调用Binder机制

	// 唯一标识
	static final String DESCRIPTOR = "com.example.chapter2.manualbinder.IBookManager";

	// 指定添加图片或获取图片列表的标识
	static final int TRANSACTION_getBookList = (IBinder.FIRST_CALL_TRANSACTION + 0);
	static final int TRANSACTION_addBook = (IBinder.FIRST_CALL_TRANSACTION + 1);

	// 获取图书列表
	public List<Book> getBookList() throws RemoteException;

	// 添加图书
	public void addBook(Book book) throws RemoteException;

}
