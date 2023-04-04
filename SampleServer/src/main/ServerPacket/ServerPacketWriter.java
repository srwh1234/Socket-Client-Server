package main.ServerPacket;

import java.io.ByteArrayOutputStream;

abstract public class ServerPacketWriter {
	protected ByteArrayOutputStream bao = new ByteArrayOutputStream();

	protected void writeC(final int value) {
		bao.write(value & 0xff);
	}

	protected void writeH(final int value) {
		bao.write(value & 0xff);
		bao.write(value >> 8 & 0xff);
	}

	protected void writeD(final int value) {
		bao.write(value & 0xff);
		bao.write(value >> 8 & 0xff);
		bao.write(value >> 16 & 0xff);
		bao.write(value >> 24 & 0xff);
	}

	protected void writeS(final String s) {
		try {
			if (s != null) {
				bao.write(s.getBytes("UTF-8"));
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
		bao.write(0x00);
	}

	protected void writeByte(final byte[] bytes) {
		try {
			if (bytes != null) {
				bao.write(bytes);
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
		bao.write(0x00);
	}

	/**
	 * 不足8組 補滿8組BYTE
	 */
	public byte[] getBytes() {
		final int padding = bao.size() % 8;
		if (padding != 0) {
			for (int i = padding; i < 8; i++) {
				writeC(0x00);
			}
		}
		return bao.toByteArray();
	}

}
