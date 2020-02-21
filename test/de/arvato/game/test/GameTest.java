package de.arvato.game.test;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;

import de.arvato.game.GameClient;

public class GameTest {

	public static void main (String args[]) throws Exception {
		Log.set(Log.LEVEL_DEBUG);
		final int threads = 2;

		for (int i = 0; i < threads; i++) {
			new Thread() {
				public void run () {
					GameClient client = new GameClient();
				}
			}.start();
		}
		while (true) {}
	}
}
