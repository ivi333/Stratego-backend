package de.arvato.game;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import de.arvato.game.GameServer.GameConnection;
import de.arvato.game.Network.GameInitBoard;
import de.arvato.game.Network.GameMovePiece;
import de.arvato.game.Network.ServerTalk;

public class GameRoom {

	public static final int DELAY_CHANGE_TURN_MS=200;
	
	//Store connections for both players
	GameConnection player1;
	GameConnection player2;
	
	//Board Game
	Board board;
	
	//Define next movement
	int playerTurns;
	
	//Players are ready
	boolean player1Ready;
	boolean player2Ready;
	
	//identify the room
	private int idRoom;
	
	
	public GameRoom (final GameConnection player1GameCon, final GameConnection player2GameCon, final int idRoom) {
		this.player1=player1GameCon;
		this.player2=player2GameCon;
		
		Log.info("Creating Game Room with:" + this.player1.name + " - " + this.player2.name);
		
		GameRoomListener listenerPlayer1 = new GameRoomListener(player2);
		GameRoomListener listenerPlayer2 = new GameRoomListener(player1);
		
		this.player1.addListener(listenerPlayer1);
		this.player2.addListener(listenerPlayer2);
		
		this.playerTurns = this.player1.getID();
		this.idRoom = idRoom;
		
		this.board = new Board();
		
		roomReady (this.player1);
		roomReady (this.player2);
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
				System.out.println("Game Room ServerTalk Got:" + serverTalk.text);
			} else if (object instanceof GameInitBoard) {
				GameInitBoard gameInit = (GameInitBoard) object;
				Log.debug("Game Room GameInitBoard Got:" + gameInit.toString());
				
				boolean player1Con = isPlayer1(currentPlayerConnection);
				boolean player2Con = !player1Con;
				
				if (player1Con) {
					player1Ready=true;
				} else {
					player2Ready=true;
				}
				// set the positions for the connected player
				board.setPlayerPositions(gameInit.pieces);
				board.printMatrix();
				//send positions to other player
				otherPlayerConnection.sendTCP(gameInit);
				
				// Player 1 starts the game if both connected
				if (player1Ready && player2Ready) {
					Log.debug("Server player1Ready and player2Ready");
					if (player1Con) {
						changeTurn(currentPlayerConnection);
					} else {
						changeTurn(otherPlayerConnection);
					}
				}
				
			} else if (object instanceof GameMovePiece) {
				GameMovePiece movePiece = (GameMovePiece) object;
				board.requestMovement(movePiece);
				otherPlayerConnection.sendTCP(movePiece);
				changeTurn(otherPlayerConnection);
			}
		}
		
		boolean isPlayer1 (Connection c) {
			return player1.getID() == c.getID();
		}
		
		boolean isPlayer2 (Connection c) {
			return player2.getID() == c.getID();
		}
	}
	
	public void changeTurn (Connection con) {
		Log.debug("Changing turn on thread: {}", Thread.currentThread().getName());
		ServerTalk serverTalk = new ServerTalk();
		serverTalk.text = ServerTalk.YOUR_TURN;
		con.sendTCP(serverTalk);
	}
	
	/*public void changeTurnDelay (Connection con) {
		ScheduledFuture schedule = scheduledExecutorService.schedule(new Runnable () {
			@Override
			public void run() {
				Log.debug("Changing turn on thread: {}", Thread.currentThread().getName());
				ServerTalk serverTalk = new ServerTalk();
				serverTalk.text = ServerTalk.YOUR_TURN;
				con.sendTCP(serverTalk);
			}
		}, DELAY_CHANGE_TURN_MS, TimeUnit.MILLISECONDS);
		try {
			schedule.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
			Log.error(e.getMessage(), e);
		} catch (ExecutionException e) {
			e.printStackTrace();
			Log.error(e.getMessage(), e);
		}
	}*/
	
	public void roomReady (Connection con) {
		ServerTalk serverTalk = new ServerTalk();
		serverTalk.text = ServerTalk.ROOM_READY;
		con.sendTCP(serverTalk);
	}
}
