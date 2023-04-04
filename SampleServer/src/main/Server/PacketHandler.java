package main.Server;

import main.PacketCode;
import main.StaticUtils;
import main.ClientPacket.C_Message;
import main.ClientPacket.ClientPacketReader;

public class PacketHandler {

	private final ClientThread client;

	public PacketHandler(final ClientThread client) {
		this.client = client;
	}

	// 依據PacketCode 交給對應的類別處理
	public void parse(final byte[] data) {
		ClientPacketReader packetReader = null;

		try {
			final int subCode = data[0] & 0xFF;

			switch (subCode) {
			case PacketCode.C_MESSAGE:
				packetReader = new C_Message(data, client);
				break;
			}

			if (packetReader != null) {
				packetReader.parse();
			}

		} catch (final Exception e) {
			e.printStackTrace();

		} finally {
			StaticUtils.close(packetReader);
		}
	}
}
