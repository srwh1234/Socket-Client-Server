package main;

import main.ServerPacket.S_Key;
import main.ServerPacket.S_Message;
import main.ServerPacket.ServerPacketReader;

public class PacketHandler {

	private final ConnectThread connect;

	public PacketHandler(final ConnectThread connect) {
		this.connect = connect;
	}

	// 依據PacketCode 交給對應的類別處理
	public void parse(final byte[] data) {
		ServerPacketReader packetReader = null;

		try {
			final int subCode = data[0] & 0xFF;

			switch (subCode) {
			case PacketCode.S_KEY:
				packetReader = new S_Key(data, connect);
				break;
			case PacketCode.S_MESSAGE:
				packetReader = new S_Message(data, connect);
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
