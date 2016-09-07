package com.example.chapter2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.chapter2.aidl.Book;
import com.example.chapter2.model.User;
import com.example.chapter2.utils.Utils;
import com.example.developart.R;

/**
 * 证明不同进程间可以对共享文件进行读写操作.原因是安卓是基于Linux系统,使其对读写没有任何限制.
 */
public class WriteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write);

		Button btnWrite = (Button) findViewById(R.id.btn_write);
		btnWrite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WriteActivity.this, ReadActivity.class);
				User user = new User(0, "jake", true);
				user.book = new Book();
				intent.putExtra("extra_user", (Serializable) user);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		writeDataToFile();
		super.onResume();
	}

	/**
	 * 写入数据
	 */
	private void writeDataToFile() {
		User user = new User(1, "hello world", false);
		File dir = new File(Utils.DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(Utils.TEMPFILE);
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(new FileOutputStream(file));
			os.writeObject(user);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
