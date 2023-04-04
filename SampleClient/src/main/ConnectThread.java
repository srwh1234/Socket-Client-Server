package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import main.ClientPacket.ClientPacketWriter;

public class ConnectThread extends Thread {

	private final Socket socket;
	private final InputStream is;
	private final OutputStream os;
	private final PacketHandler packetHandler = new PacketHandler(this);

	// 簡單的xor
	// 實際上可分成伺服器封包與客戶端封包 用各自的加解密算法
	private XorCipher cipher;

	public void setCipher(final int key) {
		this.cipher = new XorCipher(key);
	}

	public ConnectThread(final Socket socket) throws IOException {
		this.socket = socket;
		this.is = socket.getInputStream();
		this.os = socket.getOutputStream();
	}

	@Override
	public void run() {
		try {
			while (!socket.isClosed()) {
				final byte[] data = readPacket();

				// 解密
				if (cipher != null) {
					cipher.decode(data);
				}

				// 收到封包時的處理
				packetHandler.parse(data);
			}
		} catch (final SocketException e) {
			// 如果讀取時連線關閉會發生

		} catch (final Exception e) {
			e.printStackTrace();

		} finally {
			StaticUtils.close(socket);
			System.out.println("連線結束");
		}
	}

	// 輸出加密的封包
	public void sendPakcet(final ClientPacketWriter packet) throws IOException {
		if (packet == null) {
			return;
		}

		final byte[] bytes = packet.getBytes();

		if (bytes == null || bytes.length == 0) {
			return;
		}

		// 加密
		cipher.encode(bytes);

		writePacket(bytes);
	}

	private void writePacket(final byte[] data) throws IOException {
		final int outLength = data.length + 2;
		os.write(outLength & 0xff);
		os.write((outLength >> 8) & 0xff);
		os.write(data);
		os.flush();
	}

	// 從資料流讀取封包
	private byte[] readPacket() throws IOException {
		final int hiByte = is.read();
		final int loByte = is.read();

		final int dataSize = ((loByte << 8) + hiByte) - 2;
		final byte[] data = new byte[dataSize];

		int readSize = 0;
		for (int i = 0; i != -1 && readSize < dataSize; readSize += i) {
			i = is.read(data, readSize, dataSize - readSize);
		}
		return data;
	}

}
