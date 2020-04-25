package de.arvato.game.test;

import java.util.ArrayList;
import java.util.Date;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.KryoNetTestCase;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import de.arvato.game.GameClient;
import de.arvato.game.Network;
import de.arvato.game.Piece;
import de.arvato.game.PieceEnum;
import de.arvato.game.Network.GameInitBoard;
import de.arvato.game.Network.GameMovePiece;
import de.arvato.game.Network.RegisterName;
import de.arvato.game.Network.ServerTalk;

public class GameTest extends KryoNetTestCase {

	public void testGame() throws Exception {
		Log.set(Log.LEVEL_INFO);
//		final int threads = 200;
		final int clients = 2000;
		
		for (int i = 0; i < clients; i++) {
			Client client = new Client(16384, 8192);
//			client.getKryo().register(String[].class);
			Network.register(client);
			startEndPoint(client);
			client.addListener(new Listener() {
				
				String name;
				public void connected (Connection connection) {
					//set big timeout for debugging
					if (Log.DEBUG)
						connection.setTimeout(120000);
					Log.info("GameClient connected ID:" + connection.getID());
					RegisterName registerName = new RegisterName();
					name = "Ivan-" + new Date();
					registerName.name = name;
					Log.debug("Client sends registerName:" + registerName.name);
					client.sendTCP(registerName);				
				}

				
				int received;
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
							try {
								Thread.sleep(1500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							GameMovePiece movePiece = new GameMovePiece();
							movePiece.p1kill=false;
							movePiece.p1x=3;
							movePiece.p1y=5;
							movePiece.p2kill=true;
							movePiece.p1y=5;
							movePiece.p2y=6;
							Log.debug("Client sends GameMovePiece:" + c.getID());
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
					/*if (object instanceof String) {
						received++;
						if (received == messageCount * threads) {
							for (int i = 0; i < messageCount; i++) {
								System.out.println(Thread.currentThread().getName() + " client received message: " + i);
								connection.sendTCP("message" + i);
								try {
									Thread.sleep(50);
								} catch (InterruptedException ignored) {
								}
							}
						}
					}*/
				}
			});
			client.connect(5000, Network.host, Network.port);
		}
		
		

		
		while (true) {}
	}
}
