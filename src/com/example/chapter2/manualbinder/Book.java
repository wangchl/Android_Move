package com.example.chapter2.manualbinder;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

	/** ͼ��ID */
	public int bookId;
	/** ���� */
	public String bookName;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(bookId);
		out.writeString(bookName);
	}

	private Book(Parcel in) {
		super();
		bookId = in.readInt();
		bookName = in.readString();
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
}