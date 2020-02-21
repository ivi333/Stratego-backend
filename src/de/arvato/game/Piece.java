package de.arvato.game;

import com.esotericsoftware.kryo.serializers.FieldSerializer;

public class Piece {
	public int player;
	public int x,y;
	public PieceEnum name;
	
	enum Movement {
		UP, DOWN, LEFT, RIGHT; 
	}
	
	public Piece () {}
	
	public Piece (final int player, PieceEnum name) {
		this.x=-1;
		this.y=-1;	
		this.player = player;
		this.name = name;
	}

	// For the mock operation
	public void jumpTo (final int x1, final int y1) {
		this.x = x1;
		this.y = y1;
	}
	
	/*public void move (Movement m) {
		switch (m) {
			case UP:
				y++;
				break;
			case DOWN:
				y--;
				break;
			case LEFT:
				x--;
				break;
			case RIGHT:
				x++;
				break;
			default:
				return;
		}
	}*/

	/*public boolean samePlayer (Piece p) {
		return this.player == p.player;
	}
	
	public boolean isForbidden () {
		return this.name == PieceEnum.FORBIDDEN;
	}
	
	public boolean isMoveAllowed () {
		boolean b = 
				this.name.equals(PieceEnum.BOMBA) ||
 				this.name.equals(PieceEnum.BANDERA);
		return !isForbidden() && !b;
	}*/
	
	static Piece createForbidden () {
		return new Piece(-1, PieceEnum.FORBIDDEN);
	}
	
	@Override
	public String toString() {
		return "Piece [x=" + x + ", y=" + y + ", player=" + player + ", name=" + name + "]";
	}
	
	public static void main (String args[]) {
		Piece p = new Piece(1, PieceEnum.MARISCAL);
		System.out.println(p);
	}	
}
