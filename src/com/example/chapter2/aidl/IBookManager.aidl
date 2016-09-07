package com.example.chapter2.aidl;

import com.example.chapter2.aidl.Book;
import  com.example.chapter2.aidl.IOnNewBookArrivedListener;

interface IBookManager{
	List<Book> getBookList();
	void addBook(in Book book);
	void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}