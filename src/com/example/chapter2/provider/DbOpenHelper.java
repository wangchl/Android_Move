package com.example.chapter2.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

	public DbOpenHelper(Context context) {
		super(context, "book_provider.db", null, 1);
	}

	public static final String BOOK_TABLE_NAME = "book";
	public static final String USER_TABLE_NAME = "user";

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists " + BOOK_TABLE_NAME + "(_id integer primary key,name TEXT)");
		db.execSQL("create table if not exists " + USER_TABLE_NAME + "(_id integer primary key,name TEXT,sex INT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO °æ±¾¸üÐÂ

	}
}
