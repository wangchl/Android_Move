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
 * �����
 */
public class TCPServerService extends Service {

	/** �Ƿ��˳��ͻ��������־λ */
	private boolean flag = false;

	private String[] mDefinedMessages = new String[] { "��ð�������", "�������ʲô����ѽ��", "���챱������������shy", "��֪�����ҿ��ǿ��ԺͶ����ͬʱ�����Ŷ",
			"���㽲��Ц���ɣ���˵��Ц������������̫���֪����١�" };

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

			// ���ܿͻ�������
			while (!flag) {
				try {
					final Socket client = serverSocket.accept();
					System.out.println("accept");
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								response(client);// ��Ӧ�ͻ�������
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
		// ���ڽ��տͻ��˵���Ϣ
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		// ������ͻ��˷�����Ϣ
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		out.println("��ӭ��������ϵͳ");// �����

		while (!flag) {
			String str = in.readLine();
			System.out.println("message from client:" + str);
			if (str == null) {
				// �ͻ��˶Ͽ�����
				break;
			}

			int i = new Random().nextInt(mDefinedMessages.length);
			String msg = mDefinedMessages[i];
			out.println(msg);
			System.out.println("send :" + msg);
		}

		// �ر���
		out.close();
		in.close();
		socket.close();
	}

	@Override
	public void onDestroy() {
		flag = true;// �˳����ٽ��ܿͻ�������
		super.onDestroy();
	}
}
