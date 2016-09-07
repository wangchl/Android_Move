package com.example.chapter2.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.developart.R;

/**
 * 客户端
 */
public class TCPClientActivity extends Activity {

	private Button btnSend;
	private EditText editMsg;
	private TextView txtContainer;
	private PrintWriter mPrintWriter;
	private Socket mClientSocket;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				btnSend.setEnabled(true);
				break;
			case 2:
				txtContainer.setText(txtContainer.getText() + (String) msg.obj);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);
		// 查找ID
		initFindViewById();
		// 事件监听
		initClickListener();
		// 创建意图
		Intent intent = new Intent(this, TCPServerService.class);
		startService(intent);
		// 发送链接
		new Thread() {

			@Override
			public void run() {
				connectService();
			}
		}.start();
	}

	/**
	 * 查找ID
	 */
	private void initFindViewById() {
		btnSend = (Button) findViewById(R.id.btn_send);
		txtContainer = (TextView) findViewById(R.id.txt_container);
		editMsg = (EditText) findViewById(R.id.edit_msg);
	}

	/**
	 * 事件监听
	 */
	private void initClickListener() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_send:
					String message = editMsg.getText().toString();
					if (!TextUtils.isEmpty(message) && mPrintWriter != null) {
						// 给服务端发送请求
						mPrintWriter.println(message);
						editMsg.setText("");
						String time = formatDateTime(System.currentTimeMillis());
						final String showedMsg = "self " + time + ":" + message + "\n";
						txtContainer.setText(txtContainer.getText() + showedMsg);
					}
					break;
				default:
					break;
				}
			}
		};
		btnSend.setOnClickListener(onClickListener);
	}

	@SuppressLint("SimpleDateFormat")
	private String formatDateTime(long time) {
		return new SimpleDateFormat("(HH:mm:ss)").format(new Date(time));
	}

	private void connectService() {
		Socket socket = null;
		while (socket == null) {
			try {
				socket = new Socket("localhost", 8688);
				mClientSocket = socket;
				mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
						true);
				mHandler.sendEmptyMessage(1);
				System.out.println("connect server success");
			} catch (IOException e) {
				// 超时重连
				SystemClock.sleep(1000);
				System.out.println("connect tcp server failed,retry...");
			}
		}

		try {
			// 接收服务端消息
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (!TCPClientActivity.this.isFinishing()) {
				String msg = br.readLine();
				System.out.println("receive:" + msg);
				if (msg != null) {
					String time = formatDateTime(System.currentTimeMillis());
					final String showedMsg = "server " + time + ":" + msg + "\n";
					mHandler.obtainMessage(2, showedMsg).sendToTarget();
				}
			}

			System.out.println("quit...");
			// 关闭流
			mPrintWriter.close();
			br.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		if (mClientSocket != null) {
			try {
				mClientSocket.shutdownInput();
				mClientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.onDestroy();
	}
}