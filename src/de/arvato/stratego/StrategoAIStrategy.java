package de.arvato.stratego;

import java.util.List;
import java.util.Map;

public class StrategoAIStrategy {

	public static int MAX_SCORE = 1000;
	public static int MIN_SCORE = -1000;
	public static int MAX_DEPTH = 5;
	
	private StrategoAI strategoAI;
	private StrategoAIHeuristic heuristic;
	
	public StrategoAIStrategy(StrategoAI strategoAI) {
		this.strategoAI = strategoAI;
		this.heuristic = new StrategoAIHeuristic(strategoAI);
	}

	public int [] searchMove (final Board board) {
		int [] move = new int[2];
		//from 
		move[0] = 0;
		//to
		move[1] = 0;
		
		Piece [] board = this.strategoAI.getPieces();
		int player = this.strategoAI.getPlayerAI();
		
		long now = System.currentTimeMillis();
		StrategoAIBestMove bestMove = minimax(board, player, MAX_DEPTH, 0);		
		System.out.println("AI Time consumed:" + ( (System.currentTimeMillis() - now) ) + " miliseconds.");
		System.out.println("AI Time consumed:" + ( (System.currentTimeMillis() - now)/1000 ) + " seconds.");
		
		return move;
	}

	public void randomMove () {
		
	}
	
	// player is the original player never changes
	public StrategoAIBestMove minimax (Piece [] board, int player, int maxDepth, int curDepth) {
		
		int oppositePlayer = StrategoConstants.RED;
		if (player == StrategoConstants.RED) {
			oppositePlayer = StrategoConstants.BLUE;
		}
		
		//Evaluate Function
		if (curDepth== maxDepth || isGameOver (board, player)) {
			return heuristic.evaluate (board, player);		
		}

		int newPlayerIndex;
		if (curDepth % 2 == 0) {
			newPlayerIndex = oppositePlayer;
		} else {
			newPlayerIndex = player;
		}
		
		int bestScore;
		if (newPlayerIndex == player) {
			bestScore = MIN_SCORE;
		} else {
			bestScore = MAX_SCORE;
		}
		
		//Identify 
		
		
		//Update the best score
		
		
		
		return null;
	}
	
	private boolean isGameOver(Piece[] board, int player) {
		
		return false;
	}

	public int evaluateFunction (Piece [] board) {
		return -1;
	}
	
	public Piece [] cloneBoard (Piece [] board) {
		Piece newBoard [] = new Piece[board.length];
		for (int z=0;z<board.length;z++) {
			if (board[z] != null) {
				newBoard[z] = board[z].copy();
			}
		}
		return newBoard;
	}
	
	public boolean piecesInDanger () {
		return false;
	}
	
	public boolean piecesDiscovered () {
		return false;
	}
	
	public boolean bombsDiscovered () {
		return false;
	}
	
	public void proposeFlagPositions () {
		
	}
}
