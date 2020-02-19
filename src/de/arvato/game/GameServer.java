package de.arvato.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import de.arvato.game.Network.ServerTalk;
import de.arvato.game.Network.RegisterName;

public class GameServer extends AbstractGameServer {

	int numberPlayers;
	List<GameConnection> playerList;
	int gameRoom;
	
	public GameServer () throws IOException {
		numberPlayers=0;
		gameRoom=0;
		playerList = new ArrayList<GameConnection>();
		this.server = new Server() {
			protected Connection newConnection () {
				// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				return new GameConnection();
			}
		};
		
		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(server);

		server.addListener(new Listener() {
			@Override
			public void connected(Connection connection) {
				System.out.println("SERVER connected:" + connection.toString());
			}

			@Override
			public void disconnected(Connection connection) {
				System.out.println("SERVER disconnected:" + connection.toString());
			}

			@Override
			public void received(Connection c, Object object) {
				// We know all connections for this server are actually ChatConnections.
				GameConnection connection = (GameConnection)c;
				if (object instanceof RegisterName) {
					// Ignore the object if a client has already registered a name. This is
					// impossible with our client, but a hacker could send messages at any time.
					if (connection.name != null) return;
					// Ignore the object if the name is invalid.
					String name = ((RegisterName)object).name;
					if (name == null) return;
					name = name.trim();
					if (name.length() == 0) return;
					// Store the name on the connection.
					connection.name = name;
					// Send a "connected" message to everyone except the new client.
//					ChatMessage chatMessage = new ChatMessage();
//					chatMessage.text = name + " connected.";
//					server.sendToAllExceptTCP(connection.getID(), chatMessage);
					
					// Send everyone a new list of connection names.
//					updateNames();

					newGame (connection);
					
					return;
				}
				
				if (object instanceof ServerTalk) {
					if (connection.name == null) return;
					ServerTalk chatMessage = (ServerTalk)object;
					System.out.println("Game Server Got:" + chatMessage.text);
				}
			}
			
		});
		
		server.bind(Network.port);
		server.start();

	}
	
	public synchronized void newGame (GameConnection connection) {
		playerList.add(connection);
		numberPlayers++;
		System.out.println("newGame number of players:" + numberPlayers);
		if (numberPlayers == 2) {
			// new Game. Create new Thread for each GameRoom?
			new GameRoom(playerList.get(0), playerList.get(1), gameRoom++);
			
			ServerTalk message = new ServerTalk();
			message.text ="Players are connected";
			
//			server.sendToTCP(playerList.get(0).getID(), message);
//			server.sendToTCP(playerList.get(1).getID(), message);

			this.sendPlayersConnected(playerList.get(0).getID(), playerList.get(1).getID());
			
			// Clear waiting list
			playerList.clear();
			numberPlayers=0;
		} 
	}


	// This holds per connection state.
	static class GameConnection extends Connection {
		public String name;
	}

	public static void main (String[] args) throws IOException {
		new GameServer();
	}

}
