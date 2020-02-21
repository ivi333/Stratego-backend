package de.arvato.game;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.minlog.Log;

import de.arvato.game.Network.GameMovePiece;
import de.arvato.game.util.RandomUtil;

public class Board {
	//10x10 board
	public static final int ROWS = 10, COLS = 10;

	//store eaten pieces
	private List<Piece> p1PiecesLost;
	private List<Piece> p2PiecesLost;
	
	//the main matrix
	private Piece [] [] matrixGame;

	public Board () {
		matrixGame = new Piece [ROWS] [COLS];
		forbiddenPos ();
		p1PiecesLost=new ArrayList<Piece>();
		p2PiecesLost=new ArrayList<Piece>();
	}
	
	public Board (final Piece[] p1Positions, Piece[] p2Positions) {
		matrixGame = new Piece [ROWS] [COLS];
		forbiddenPos ();
		distributeGame (p1Positions);
		distributeGame (p2Positions);
		p1PiecesLost=new ArrayList<Piece>();
		p2PiecesLost=new ArrayList<Piece>();
	}

	private void forbiddenPos () {
		Piece forbidden = Piece.createForbidden();
		matrixGame [3][5] = forbidden;
		matrixGame [3][6] = forbidden;
		matrixGame [4][5] = forbidden;
		matrixGame [4][6] = forbidden;
		matrixGame [7][5] = forbidden;
		matrixGame [7][6] = forbidden;
		matrixGame [8][5] = forbidden;
		matrixGame [8][6] = forbidden;
	}

	private void distributeGame(Piece[] pieces) {
		for (Piece piece : pieces) {
			matrixGame[piece.x] [piece.y] = piece;
		}
	}
	
	// Public Methods here

	public void setPlayerPositions (final Piece[] pieces) {
		distributeGame(pieces);
	}
	
	public void setPlayerPositions (List<Piece> pieces) {
		Piece [] transform = new Piece [pieces.size()];
		pieces.toArray(transform);
		setPlayerPositions(transform);
	}
	
	public boolean isFree (int x, int y) {
		return matrixGame[x][y] == null;
	}
	
	//Logical goes on client side, we trust on positions
	public void requestMovement (GameMovePiece movePiece) {
		if (Log.DEBUG)
			Log.debug(String.format("Game move p1x=%d, p1y=%d, p2x=%d, p2y=%d, p1kill=%b, p2kill=%b", 
					movePiece.p1x, movePiece.p1y, movePiece.p2x, movePiece.p2y, movePiece.p1kill, movePiece.p2kill));
		
	}

	public void printMatrix () {
		for (int i=0;i<ROWS;i++) {
			for (int j=0;j<COLS;j++) {
				if (Log.DEBUG)
					Log.debug(String.format("(%d,%d)=%s", i,j, matrixGame[i][j]));
			}
		}
	}

	public static void main (String args []) {
		Log.set(Log.LEVEL_DEBUG);
		Piece[] p2Positions=RandomUtil.distributePiecesRandom(2);
		Piece[] p1Positions=RandomUtil.distributePiecesRandom(1);
		Board b = new Board(p1Positions, p2Positions);
		b.printMatrix();
	}
}



/*public boolean requestMovement (int player, int curX, int curY, int x, int y) {
// Game Logical here
Piece curPiece = matrixGame [curX] [curY];

// check cur piece is not null
if (curPiece == null) {
	System.err.println(String.format("Got Null piece on (%d,%d)", curX, curY));
	printMatrix();
	return false;
}

// check if move is allowed
if (!curPiece.isMoveAllowed()) {
	System.err.println(String.format("Deny movement to this piece (%d,%d)", curX, curY));
	printMatrix();
	return false;
}

// Get future piece pos
Piece futurePiece = matrixGame [x] [y];

// Future piece is null means is empty lets move
if (futurePiece == null) {
	return moveToFuturePos (curPiece, x, y);
} 

// Future piece is not empty. Check forbidden pos
if (futurePiece.isForbidden()) {
	System.err.println(String.format("Future pos is forbidden on (%d,%d)", curX, curY));
	printMatrix();
}

// check if future pos is busy with same player's piece
if (curPiece.samePlayer(futurePiece)) {
	System.err.println(String.format("Cannot move in a busy position on (%d,%d)", curX, curY));
	printMatrix();
}

// Future piece is opposite player
return fightWithPlayer (curPiece, futurePiece, x, y);
}*/

//private boolean fightWithPlayer(Piece curPiece, Piece futurePiece, int x, int y) {
////TODO Define FightStatus if required.
//if (futurePiece.name == PieceEnum.BANDERA) {
//	//Congratulations user won
//	return true;
//}
//// Define specials cases
//// Usual cases
//return true;
//}

//private boolean moveToFuturePos(Piece curPiece, int x, int y) {
//return true;
//}
