package main.ServerPacket;

import main.ConnectThread;

/**
 * 讀取來自伺服器的加解密鑰匙封包
 * 並初始化XorCipher
 */
public class S_Key extends ServerPacketReader {

	public S_Key(final byte[] data, final ConnectThread connct) {
		super(data, connct);
	}

	@Override
	public void parse() {
		try {
			final int key = this.readD();
			System.out.println(key);

			connct.setCipher(key);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
