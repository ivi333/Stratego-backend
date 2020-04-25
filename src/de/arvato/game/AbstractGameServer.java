package de.arvato.game;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import de.arvato.game.Network.ServerTalk;

public abstract class AbstractGameServer {

	public AbstractGameServer () {
		Log.set(Log.LEVEL_INFO);
	}
	
	protected Server server;

	public void sendPlayersConnected (int ...connectionIds) {
		for (final int c : connectionIds) 
			server.sendToTCP(c, ServerTalk.PLAYERS_CONNECTED);
	}
	
}
