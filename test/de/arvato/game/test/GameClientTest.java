package de.arvato.game.test;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.KryoNetTestCase;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import de.arvato.game.GameClient;
import de.arvato.game.Network;

public class GameClientTest  {

	public static void main(String args[]) throws Exception {
		Log.set(Log.LEVEL_DEBUG);
		final int threads = 4;
		final int clients = 200;
		
		
		for (int i = 0; i < threads; i++) {
			try {
				Thread.sleep(400);
			} catch (InterruptedException ignored) {
			}
			
			new Thread() {
				public void run () {
					GameClient client = new GameClient();
					
				}
			}.start();
		}
		

		
		while (true) {}
	}
}
