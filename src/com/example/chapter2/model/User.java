package com.example.chapter2.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.chapter2.aidl.Book;

/**
 * android�ṩ�����л��ӿ�. android����Intent��Bitmap��Bundle��ʵ����Parcelable�ӿ�
 */
public class User implements Parcelable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** �û�ID */
	public int userID;
	/** �û��� */
	public String userName;
	/** �Ƿ����к� */
	public boolean isMale;
	/** ͼ����� */
	public Book book;

	public User() {
		super();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(userID);
		out.writeString(userName);
		out.writeInt(isMale ? 1 : 0);
		out.writeParcelable(book, 0);
	}

	public User(int userID, String userName, boolean isMale) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.isMale = isMale;
	}

	private User(Parcel in) {
		super();
		userID = in.readInt();
		userName = in.readString();
		isMale = in.readInt() == 1;
		book = in.readParcelable(Thread.currentThread().getContextClassLoader());
	}

	/**
	 * ��дCreator
	 */
	public static final Creator<User> CREATOR = new Parcelable.Creator<User>() {

		@Override
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

	@Override
	public String toString() {
		return String.format("User:{userId:%s, userName:%s, isMale:%s}, with child:{%s}", userID, userName, isMale,
				book);
	}
}
