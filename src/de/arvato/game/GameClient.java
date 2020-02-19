package de.arvato.game;

import java.io.IOException;
import java.util.Date;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import de.arvato.game.Network.ServerTalk;
import de.arvato.game.Network.RegisterName;

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
				System.out.println("GameClient connected:" + connection.toString());
				RegisterName registerName = new RegisterName();
				name = "Ivan-" + new Date();
				registerName.name = name;
				client.sendTCP(registerName);
			}

			public void received (Connection connection, Object object) {
				System.out.println("GameClient received:" + connection.toString());
				
				if (object instanceof ServerTalk) {
					ServerTalk chatMessage = (ServerTalk)object;
					System.out.println("Got:" + chatMessage.text);
					
					ServerTalk clientMessage = new ServerTalk();
					clientMessage.text="Hello from client:" + connection.getID();
					client.sendTCP(clientMessage);
				}
				
//				if (object instanceof UpdateNames) {
//					UpdateNames updateNames = (UpdateNames)object;
//					chatFrame.setNames(updateNames.names);
//					return;
//				}
//
//				if (object instanceof ChatMessage) {
//					ChatMessage chatMessage = (ChatMessage)object;
//					chatFrame.addMessage(chatMessage.text);
//					return;
//				}
			}

			public void disconnected (Connection connection) {
				System.out.println("GameClient disconected:" + connection.toString());
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
