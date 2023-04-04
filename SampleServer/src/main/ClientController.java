package main;

import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import main.Server.ClientThread;

public class ClientController {

	public static ClientController get() {
		return Inner.instance;
	}

	private static class Inner {
		private static final ClientController instance = new ClientController();
	}

	private final Vector<ClientThread> clientThreads = new Vector<>();

	public void putWithPrint(final ClientThread client) {
		put(client);

		final String msg = String.format("新的連線已建立!!目前連線數%d", getClientSize());
		System.out.println(msg);
	}

	public void removeWithPrint(final ClientThread client) {
		remove(client);

		final String msg = String.format("Client(%d) 連線已關閉.目前連線數%d", //
				client.getKey(), getClientSize());
		System.out.println(msg);
	}

	public void put(final ClientThread client) {
		clientThreads.add(client);
	}

	public void remove(final ClientThread client) {
		clientThreads.remove(client);
	}

	private Collection<ClientThread> readOnlyClientThreads;

	public Collection<ClientThread> all() {
		final Collection<ClientThread> tmp = readOnlyClientThreads;
		return (tmp != null) ? tmp
				: (readOnlyClientThreads = Collections.unmodifiableCollection(
						clientThreads));
	}

	public int getClientSize() {
		return clientThreads.size();
	}
}
