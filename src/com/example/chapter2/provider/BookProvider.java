package com.example.chapter2.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class BookProvider extends ContentProvider {

	private static final String TAG = "BookProvider";

	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	private static final String AUTHORITYS = "com.example.chapter2.provider.BookProvider.provider";

	private static final int BOOK_CODE = 0;
	private static final int USER_CODE = 1;

	private SQLiteDatabase mDb;

	static {
		// 关联
		sUriMatcher.addURI(AUTHORITYS, "book", BOOK_CODE);
		sUriMatcher.addURI(AUTHORITYS, "user", USER_CODE);
	}

	@Override
	public boolean onCreate() {
		Log.d(TAG, "onCreate");
		// 初始化
		initProviderData();
		return true;
	}

	private void initProviderData() {
		DatabaseContext databaseContext = new DatabaseContext(getContext());
		mDb = databaseContext.openOrCreateDatabase("book_provider.db", 0, null, null);
		// mDb = new DbOpenHelper(getContext()).getWritableDatabase();
		mDb.execSQL("delete from book");
		mDb.execSQL("delete from user");
		// 插入语句
		mDb.execSQL("insert into book values(1,'android')");
		mDb.execSQL("insert into book values(2,'ios')");
		mDb.execSQL("insert into book values(3,'java')");
		mDb.execSQL("insert into user values(1,'zhangsan',1)");
		mDb.execSQL("insert into user values(2,'lisi',0)");
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.d(TAG, "query");
		String table = getTableName(uri);
		if (table == null) {
			throw new IllegalArgumentException("Unsupported:" + uri);
		}
		return mDb.query(table, projection, selection, selectionArgs, null, null, sortOrder);
	}

	@Override
	public String getType(Uri uri) {
		Log.d(TAG, "getType");
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(TAG, "insert");
		String table = getTableName(uri);
		if (table == null) {
			throw new IllegalArgumentException("Unsupported:" + uri);
		}
		mDb.insert(table, null, values);
		getContext().getContentResolver().notifyChange(uri, null);
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.d(TAG, "delete");
		String table = getTableName(uri);
		if (table == null) {
			throw new IllegalArgumentException("Unsupported:" + uri);
		}
		int count = mDb.delete(table, selection, selectionArgs);
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		Log.d(TAG, "update");
		String table = getTableName(uri);
		if (table == null) {
			throw new IllegalArgumentException("Unsupported:" + uri);
		}
		int row = mDb.update(table, values, selection, selectionArgs);
		if (row > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return row;
	}

	private String getTableName(Uri uri) {
		String tableName = null;
		switch (sUriMatcher.match(uri)) {
		case BOOK_CODE:
			tableName = "book";
			break;
		case USER_CODE:
			tableName = "user";
			break;
		default:
			break;
		}
		return tableName;
	}
}
