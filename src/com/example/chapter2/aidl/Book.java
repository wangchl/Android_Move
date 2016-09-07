package com.example.chapter2.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

	/** ͼ��ID */
	public int bookId;
	/** ͼ������ */
	public String bookName;

	private Book(Parcel in) {
		bookId = in.readInt();
		bookName = in.readString();
	}

	public Book() {
		;
	}

	public Book(int bookId, String bookName) {
		super();
		this.bookId = bookId;
		this.bookName = bookName;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(bookId);
		out.writeString(bookName);

	}

	/**
	 * ��дCreator
	 */
	public static final Creator<Book> CREATOR = new Parcelable.Creator<Book>() {

		@Override
		public Book createFromParcel(Parcel in) {
			return new Book(in);
		}

		@Override
		public Book[] newArray(int size) {
			return new Book[size];
		}
	};

	@Override
	public String toString() {
		return String.format("[bookId:%s, bookName:%s]", bookId, bookName);
	}
}
