package com.example.chapter2.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 服务端
 */
public class TCPServerService extends Service {

	/** 是否退出客户端请求标志位 */
	private boolean flag = false;

	private String[] mDefinedMessages = new String[] { "你好啊，哈哈", "请问你叫什么名字呀？", "今天北京天气不错啊，shy", "你知道吗？我可是可以和多个人同时聊天的哦",
			"给你讲个笑话吧：据说爱笑的人运气不会太差，不知道真假。" };

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		new Thread(new connectClient()).start();
		super.onCreate();
	}

	private class connectClient implements Runnable {

		@SuppressWarnings("resource")
		@Override
		public void run() {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(8688);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			// 接受客户端请求
			while (!flag) {
				try {
					final Socket client = serverSocket.accept();
					System.out.println("accept");
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								response(client);// 响应客户端请求
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void response(Socket socket) throws IOException {
		// 用于接收客户端的消息
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		// 用于向客户端发送消息
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		out.println("欢迎进入聊天系统");// 输出流

		while (!flag) {
			String str = in.readLine();
			System.out.println("message from client:" + str);
			if (str == null) {
				// 客户端断开连接
				break;
			}

			int i = new Random().nextInt(mDefinedMessages.length);
			String msg = mDefinedMessages[i];
			out.println(msg);
			System.out.println("send :" + msg);
		}

		// 关闭流
		out.close();
		in.close();
		socket.close();
	}

	@Override
	public void onDestroy() {
		flag = true;// 退出后不再接受客户端请求
		super.onDestroy();
	}
}
