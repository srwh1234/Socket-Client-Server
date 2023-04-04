package main.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import main.GeneralThreadPool;
import main.StaticUtils;

public class ServerSocketThread extends Thread {

	private int port;

	private ServerSocket serverSocket;

	public ServerSocketThread(final int port) throws IOException {
		try {
			this.port = port;
			this.serverSocket = new ServerSocket(port);

		} catch (final IOException e) {
			System.out.println(String.format("Port:%d 已經使用中.", port));
			throw e;
		}
	}

	@Override
	public void run() {
		try {
			System.out.println(String.format("Port:%d 開始等待連線中.", port));

			while (serverSocket != null) {
				Socket socket = null;
				try {
					socket = serverSocket.accept();

					if (socket == null) {
						continue;
					}

					// 建立後 丟給執行緒處理資料傳輸
					GeneralThreadPool.get().execute(new ClientThread(socket));

				} catch (final Exception e) {
					//
				}
			}

		} catch (final Exception e) {
			e.printStackTrace();

		} finally {
			StaticUtils.close(serverSocket);
			System.out.println(String.format("Port:%d 結束監聽.", port));
		}
	}
}
