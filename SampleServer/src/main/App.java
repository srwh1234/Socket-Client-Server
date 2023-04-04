package main;

import java.util.Scanner;

import main.Server.ClientThread;
import main.Server.ServerSocketThread;
import main.ServerPacket.S_Message;
import main.ServerPacket.ServerPacketWriter;

public class App {

	public static void main(final String[] args) {

		// 我的命令提示字元的編碼是BIG5...
		final Scanner sc = new Scanner(System.in, "BIG5");
		try {
			System.out.println("這是伺服端");

			// 監聽通訊阜2000
			GeneralThreadPool.get().execute(new ServerSocketThread(2000));

			while (true) {
				final String message = sc.nextLine();

				// 發送封包給全部客戶端
				final ServerPacketWriter packet = new S_Message(message);
				for (final ClientThread client : ClientController.get().all()) {
					client.sendPakcet(packet);
				}
				System.out.println("To All Clients:" + message);
			}

		} catch (final Exception e) {
			e.printStackTrace();

		} finally {
			StaticUtils.close(sc);
		}
	}

}
