package main;

public class XorCipher {

	private final int key;

	public XorCipher(final int key) {
		this.key = key;
	}

	public void encode(final byte[] data) {
		for (int i = 0; i < data.length; i++) {
			data[i] ^= key;
		}
	}

	public void decode(final byte[] data) {
		for (int i = 0; i < data.length; i++) {
			data[i] ^= key;
		}
	}
}
