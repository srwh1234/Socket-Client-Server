package main;

import java.net.Socket;
import java.util.Scanner;

import main.ClientPacket.C_Message;

public class App {

	public static void main(final String[] args) {
		System.out.println("這是客戶端");

		// 我的命令提示字元的編碼是BIG5...
		final Scanner sc = new Scanner(System.in, "BIG5");

		final String serverHostname = "localhost";
		final int serverPort = 2000;
		try {
			final Socket socket = new Socket(serverHostname, serverPort);
			final ConnectThread connect = new ConnectThread(socket);
			connect.start();

			while (true) {

				final String message = sc.nextLine();

				// 發送封包給伺服器
				connect.sendPakcet(new C_Message(message));

				System.out.println("To Server:" + message);
			}

		} catch (final Exception e) {
			e.printStackTrace();

		} finally {
			StaticUtils.close(sc);
		}
	}

}
