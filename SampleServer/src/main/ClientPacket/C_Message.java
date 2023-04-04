package main.ClientPacket;

import main.Server.ClientThread;

/**
 * 讀取來自客戶端的訊息封包
 */
public class C_Message extends ClientPacketReader {

	public C_Message(final byte[] data, final ClientThread client) {
		super(data, client);
	}

	@Override
	public void parse() {
		try {
			final String message = this.readS();

			System.out.println( //
					String.format("From Client(%d):%s", client.getKey(), message));

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
