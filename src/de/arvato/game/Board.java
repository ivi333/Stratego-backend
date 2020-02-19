package de.arvato.game;

import de.arvato.game.util.RandomUtil;

public class Board {
	//100x100
	public static final int ROWS = 10, COLS = 10;

	private Piece[] p1PiecesLost;
	private Piece[] p2PiecesLost;
	private Piece [] [] matrixGame;

	public Board (final Piece[] p1Positions, Piece[] p2Positions) {
		matrixGame = new Piece [ROWS] [COLS];
		forbiddenPos ();
		distributeGame (p1Positions);
		distributeGame (p2Positions);
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
	
	public void printMatrix () {
		for (int i=0;i<ROWS;i++) {
			for (int j=0;j<COLS;j++) {
				System.out.println(String.format("(%d,%d)=%s", i,j, matrixGame[i][j]));
			}
		}
	}


	public static void main (String args []) {
		Piece[] p2Positions=RandomUtil.distributePiecesRandon();
		Piece[] p1Positions=RandomUtil.distributePiecesRandon();
		Board b = new Board(p1Positions, p2Positions);
		b.printMatrix();
	}

}
