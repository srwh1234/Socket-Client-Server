package main.ServerPacket;

import main.PacketCode;

/**
 * 發送加解密要用的鑰匙給客戶端
 */
public class S_Key extends ServerPacketWriter {

	public S_Key(final int key) {
		writeC(PacketCode.S_KEY);
		writeD(key);
	}
}
