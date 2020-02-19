package de.arvato.game;

import java.util.ArrayList;
import java.util.List;

import de.arvato.game.util.RandomUtil;

public class Board {
	//10x10 board
	public static final int ROWS = 10, COLS = 10;

	//store eaten pieces
	private List<Piece> p1PiecesLost;
	private List<Piece> p2PiecesLost;
	
	//the main matrix
	private Piece [] [] matrixGame;

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
	
	public boolean isFree (int x, int y) {
		return matrixGame[x][y] == null;
	}
	
	public boolean requestMovement (int player, int curX, int curY, int x, int y) {
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
	}
	
	private boolean fightWithPlayer(Piece curPiece, Piece futurePiece, int x, int y) {
		//TODO Define FightStatus if required.
		if (futurePiece.name == PieceEnum.BANDERA) {
			//Congratulations user won
			return true;
		}

		// Define specials cases
		
		
		// Usual cases
		
		
		
		return true;
	}

	private boolean moveToFuturePos(Piece curPiece, int x, int y) {
		
		
		return true;
	}

	public void printMatrix () {
		for (int i=0;i<ROWS;i++) {
			for (int j=0;j<COLS;j++) {
				System.out.println(String.format("(%d,%d)=%s", i,j, matrixGame[i][j]));
			}
		}
	}

	public static void main (String args []) {
		Piece[] p2Positions=RandomUtil.distributePiecesRandon(2);
		Piece[] p1Positions=RandomUtil.distributePiecesRandon(1);
		Board b = new Board(p1Positions, p2Positions);
		b.printMatrix();
	}
}
