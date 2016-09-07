package com.example.chapter2.manualbinder;

import java.util.List;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

/**
 * �ֶ�����һ��aidl�ļ�
 */
public interface IBookManager extends IInterface {// Ϊ�˵���Binder����

	// Ψһ��ʶ
	static final String DESCRIPTOR = "com.example.chapter2.manualbinder.IBookManager";

	// ָ�����ͼƬ���ȡͼƬ�б�ı�ʶ
	static final int TRANSACTION_getBookList = (IBinder.FIRST_CALL_TRANSACTION + 0);
	static final int TRANSACTION_addBook = (IBinder.FIRST_CALL_TRANSACTION + 1);

	// ��ȡͼ���б�
	public List<Book> getBookList() throws RemoteException;

	// ���ͼ��
	public void addBook(Book book) throws RemoteException;

}
