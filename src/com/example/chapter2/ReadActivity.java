package com.example.chapter2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.chapter2.model.User;
import com.example.chapter2.utils.Utils;
import com.example.developart.R;

/**
 * ���ڶ���WriteActivity��д�˵�����
 */
public class ReadActivity extends Activity {

	private static final String TAG = "ReadActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		if (intent != null) {
			User user = (User) intent.getSerializableExtra("extra_user");
			Log.d(TAG, "2-->userID:" + user.userID + " userName:" + user.userName + "  isMale:" + user.isMale
					+ "  book:" + user.book.toString());
		}
		readDataToFile();
	}

	/**
	 * ��������
	 */
	private void readDataToFile() {
		File dir = new File(Utils.DIR);
		if (!dir.exists()) {
			Log.d(TAG, "�����ڸø�Ŀ¼");
		}
		File file = new File(Utils.TEMPFILE);
		if (!file.exists()) {
			Log.d(TAG, "�����ڸ��ļ�");
		}
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(file));
			User newUser = (User) ois.readObject();
			Log.d(TAG, "1-->userID:" + newUser.userID + "  userName:" + newUser.userName + "  isMale:" + newUser.isMale);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
