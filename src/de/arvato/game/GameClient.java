package de.arvato.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import de.arvato.game.GameServer.GameConnection;
import de.arvato.game.Network.GameInitBoard;
import de.arvato.game.Network.GameMovePiece;
import de.arvato.game.Network.RegisterName;
import de.arvato.game.Network.ServerTalk;

public class GameClient {

	Client client;
	String name;
	String host="localhost";


	public GameClient () {
		client = new Client();
		client.start();

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		client.addListener(new Listener() {
			
			public void connected (Connection connection) {
				//set big timeout for debugging
				if (Log.DEBUG)
					connection.setTimeout(120000);
				Log.debug("GameClient connected ID:" + connection.getID());
				RegisterName registerName = new RegisterName();
				name = "Ivan-" + new Date();
				registerName.name = name;
				Log.debug("Client sends registerName:" + registerName.name);
				client.sendTCP(registerName);				
			}

			public void received (Connection c, Object object) {
				if (object instanceof ServerTalk) {
					ServerTalk serverTalk = (ServerTalk)object;
					Log.debug("Client Got ServerTalk:" + serverTalk.text);
					//rom is ready lets init the GameBoard
					if (ServerTalk.ROOM_READY.equals(serverTalk.text)) {						
						GameInitBoard gib = new GameInitBoard();
						gib.pieces = new ArrayList<Piece>();
						Piece pn = new Piece();
						pn.name = PieceEnum.SARGENTO;
						pn.x=3;
						pn.y=4;
						gib.pieces.add(pn);
						Log.debug("Client sends GameInitBoard:" + c.getID());
						client.sendTCP(gib);
					} else if (ServerTalk.YOUR_TURN.equals(serverTalk.text)) {
						// client turn
						GameMovePiece movePiece = new GameMovePiece();
						movePiece.p1kill=false;
						movePiece.p1x=3;
						movePiece.p1y=5;
						movePiece.p2kill=true;
						movePiece.p1y=5;
						movePiece.p2y=6;
						Log.debug("Client sends GameMovePiece:" + c.getID());
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						client.sendTCP(movePiece);
					}
				} else if (object instanceof GameInitBoard) {					
					//Opponent has init the board
					GameInitBoard gameBoard = (GameInitBoard) object;					
					Log.debug("Client "  + c.getID() + " Got pieces:" + gameBoard.pieces.size());					
				} else if (object instanceof GameMovePiece) {
					GameMovePiece movePiece = (GameMovePiece) object;
					Log.debug("Client "  + c.getID() + " Got movement:" + movePiece.toString());
				}
			}

			public void disconnected (Connection connection) {
				Log.debug("GameClient disconected:" + connection.toString());
			}
		});
		
		new Thread("Connect") {
			public void run () {
				try {
					client.connect(5000, host, Network.port);
					// Server communication after connection can go here, or in Listener#connected().
				} catch (IOException ex) {
					ex.printStackTrace();
					System.exit(1);
				}
			}
		}.start();
	}

	public static void main (String[] args) throws Exception{
		Log.set(Log.LEVEL_DEBUG);
		new GameClient();
		while (true) {}
	}
}
