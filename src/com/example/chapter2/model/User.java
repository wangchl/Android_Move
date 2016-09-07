package com.example.chapter2.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.chapter2.aidl.Book;

/**
 * android提供的序列化接口. android中有Intent、Bitmap、Bundle都实现了Parcelable接口
 */
public class User implements Parcelable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 用户ID */
	public int userID;
	/** 用户名 */
	public String userName;
	/** 是否是男孩 */
	public boolean isMale;
	/** 图书对象 */
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
	 * 重写Creator
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
