package main.ServerPacket;

import main.ConnectThread;

abstract public class ServerPacketReader implements AutoCloseable {

	private int index = 1;

	protected byte[] data = null;

	protected final ConnectThread connct;

	protected ServerPacketReader(final byte[] data, final ConnectThread connct) {
		this.data = data;
		this.connct = connct;
	}

	// 用此方法進行封包分析
	abstract public void parse();

	/**
	 * 由byte[]中取回一個int
	 */
	protected int readD() {
		int result = 0;
		try {
			if (data == null) {
				return result;
			}
			if (data.length < (index + 4)) {
				return result;
			}

			result = data[index++] & 0xff;
			result |= (data[index++] << 8) & 0xff00;
			result |= (data[index++] << 16) & 0xff0000;
			result |= (data[index++] << 24) & 0xff000000;

		} catch (final Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 由byte[]中取回一個byte
	 */
	public int readC() {
		int result = 0;
		try {
			if (data == null) {
				return result;
			}
			if (data.length < (index + 1)) {
				return result;
			}
			result = data[index++] & 0xff;

		} catch (final Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 由byte[]中取回一個short
	 */
	public int readH() {
		int result = 0;
		try {
			if (data == null) {
				return result;
			}
			if (data.length < (index + 2)) {
				return result;
			}
			result = data[index++] & 0xff;
			result |= (data[index++] << 8) & 0xff00;

		} catch (final Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 由byte[]中取回一個String
	 */
	protected String readS() {
		String result = null;
		try {
			if (data == null) {
				return result;
			}
			result = new String(data, index, data.length - index, "UTF-8");
			result = result.substring(0, result.indexOf('\0'));
			index += result.getBytes("UTF-8").length + 1;

		} catch (final Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 由byte[]中取回一組byte[]
	 */
	protected byte[] readByte() {
		final byte[] result = new byte[data.length - index];
		try {
			System.arraycopy(data, index, result, 0, data.length - index);
			index = data.length;

		} catch (final Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void close() {
		data = null;
	}
}
