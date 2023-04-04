package main.ClientPacket;

import main.PacketCode;

/**
 * 發送客戶端訊息給伺服器
 */
public class C_Message extends ClientPacketWriter {

	public C_Message(final String s) {
		writeC(PacketCode.C_MESSAGE);
		writeS(s);
	}
}
