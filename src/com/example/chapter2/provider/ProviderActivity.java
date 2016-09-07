package com.example.chapter2.provider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.chapter2.aidl.Book;
import com.example.chapter2.model.User;
import com.example.developart.R;

public class ProviderActivity extends Activity {

	private static final String TAG = "ProviderActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_provider);

		Uri bookUri = Uri.parse("content://com.example.chapter2.provider.BookProvider.provider/book");
		// 添加图书
		ContentValues contentValues = new ContentValues();
		contentValues.put("_id", 4);
		contentValues.put("name", "java编程思想");
		getContentResolver().insert(bookUri, contentValues);
		// 查找图书
		Cursor bookCursor = getContentResolver().query(bookUri, new String[] { "_id", "name" }, null, null, null);
		while (bookCursor.moveToNext()) {
			Book book = new Book();
			book.bookId = bookCursor.getInt(0);
			book.bookName = bookCursor.getString(1);
			Log.d(TAG, book.toString());
		}
		bookCursor.close();

		// 添加用户
		Uri userUri = Uri.parse("content://com.example.chapter2.provider.BookProvider.provider/user");
		ContentValues values = new ContentValues();
		values.put("_id", 3);
		values.put("name", "laowang");
		values.put("sex", 1);
		getContentResolver().insert(userUri, values);
		// 查找图书
		Cursor userCursor = getContentResolver()
				.query(userUri, new String[] { "_id", "name", "sex" }, null, null, null);
		User user = new User();
		while (userCursor.moveToNext()) {
			user.userID = userCursor.getInt(0);
			user.userName = userCursor.getString(1);
			user.isMale = userCursor.getInt(2) == 1;
			Log.d(TAG, user.toString());
		}
		userCursor.close();
	}
}
