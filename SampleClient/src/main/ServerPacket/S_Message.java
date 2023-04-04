package main.ServerPacket;

import main.ConnectThread;

/**
 * 讀取來自伺服器的訊息封包
 */
public class S_Message extends ServerPacketReader {
	public S_Message(final byte[] data, final ConnectThread connct) {
		super(data, connct);
	}

	@Override
	public void parse() {
		try {
			final String message = this.readS();

			System.out.println(String.format("From Server:%s", message));

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
