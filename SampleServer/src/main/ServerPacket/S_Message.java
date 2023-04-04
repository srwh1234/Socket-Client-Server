package main.ServerPacket;

import main.PacketCode;

/**
 * 發送訊息給客戶端
 */
public class S_Message extends ServerPacketWriter {

	public S_Message(final String s) {
		writeC(PacketCode.S_MESSAGE);
		writeS(s);
	}
}
