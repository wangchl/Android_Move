package com.example.chapter2.aidl;

import com.example.chapter2.aidl.Book;

interface IOnNewBookArrivedListener{
	 void onNewBookArrived(in Book newBook);
}