package de.arvato.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import de.arvato.game.GameServer.GameConnection;
import de.arvato.game.Network.ServerTalk;

public class GameRoom {

	GameConnection player1;
	GameConnection player2;
	
	//Define the board
	String [][] boardMatrix;
	
	//Define next movement
	int playerTurns;
	
	boolean player1Connected;
	boolean player2Connected;
	
	private int idRoom;
	
	public GameRoom (final GameConnection player1, final GameConnection player2, final int idRoom) {
		Log.info("Creating Game Room with:" + player1.name + " - " + player2.name);
		
		GameRoomListener listenerPlayer1 = new GameRoomListener(player2);
		GameRoomListener listenerPlayer2 = new GameRoomListener(player1);
		
		player1.addListener(listenerPlayer1);
		player2.addListener(listenerPlayer2);
		
		playerTurns = player1.getID();
		this.idRoom = idRoom;
				
	}
	
	class GameRoomListener extends Listener {
		GameConnection otherPlayerConnection;
		
		public GameRoomListener (GameConnection player) {
			this.otherPlayerConnection = player;
		}
		
		@Override
		public void connected(Connection connection) {
		}

		@Override
		public void disconnected(Connection c) {
			GameConnection currentPlayerConnection = (GameConnection)c;
			if (currentPlayerConnection.name == null) return;
			
			// send disconnect to Other player
			ServerTalk serverTalk = new ServerTalk();
			serverTalk.text = ServerTalk.PLAYER_DISCONNECTED;
			otherPlayerConnection.sendTCP(serverTalk);
			//Anything else? Destroy the room?
		}

		@Override
		public void received(Connection c, Object object) {
			//Game Logic Here
			GameConnection currentPlayerConnection = (GameConnection)c;
			if (currentPlayerConnection.name == null) return;
			
			if (object instanceof ServerTalk) {
				ServerTalk serverTalk = (ServerTalk)object;
				System.out.println("Player Got:" + serverTalk.text);
			}
		}
		
		boolean isPlayer1 (Connection c) {
			return player1.getID() == c.getID();
		}
		
		boolean isPlayer2 (Connection c) {
			return player2.getID() == c.getID();
		}
	}
}


