
package de.arvato.game;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.DeflateSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryonet.EndPoint;

// This class is a convenient place to keep things common to both the client and server.
public class Network {
	static public final String host = "localhost";
	static public final int port = 54555;

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(RegisterName.class);
		kryo.register(String[].class);
		kryo.register(UpdateNames.class);
		kryo.register(GameMovePiece.class);
//		kryo.register(short[].class);
//		kryo.register(SomeData.class, new DeflateSerializer(new FieldSerializer(kryo, SomeData.class)));
		kryo.register(PieceEnum.class);
		kryo.register(GameInitBoard.class);
		kryo.register(Piece.class);
		kryo.register(ArrayList.class, new CollectionSerializer());
		kryo.register(ServerTalk.class);
	}

	static public class RegisterName {
		public String name;
	}

	static public class UpdateNames {
		public String[] names;
	}

	static public class ServerTalk {
		public final static String PLAYERS_CONNECTED = "PC";
		public final static String PLAYER_DISCONNECTED = "PD";
		public final static String START_GAME = "SG";
		public final static String YOUR_TURN = "YT";
		public final static String ROOM_READY = "RR";
		public String text;
	}
	
	static public class GameInitBoard {
		public List<Piece> pieces;
	}
	
	static public class GameMovePiece {
		public int p1x;
		public int p1y;
		public int p2x;
		public int p2y;
		public boolean p1kill;
		public boolean p2kill;
	}
}
