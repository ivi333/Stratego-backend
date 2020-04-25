package de.arvato.stratego;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StrategoAI implements Runnable {

	private final StrategoAIListener listener;
//	private final StrategoControl control;
//	private final Piece [] pieces;
	private int playerAI;
	private int playerHuman;	
	private StrategoAIStrategy strategy;
	private final Board board;
	
	//Capture pieces
//	private Map<PieceEnum, Integer> capturedHuman, capturedAI;

	//Human/IA available moves
//	private Map<Integer, List<Integer>> availableMovesAI, availableMovesHuman;
	
	//Human/IA discovered pieces
	private List<Piece> discoveredPiecesHuman, discoveredPiecesAI;
	
	public StrategoAI (Board board, StrategoAIListener listener) {
		this.board = board;
		this.listener = listener;
//		this.control = sc;
//		this.pieces = sc.getPieces();
//		this.playerHuman = sc.getMyPlayer();
		// Init captured pieces
//		if (sc.getMyPlayer() == StrategoConstants.RED) {
//			this.capturedHuman = sc.getCapturedPiecesRed();
//			this.capturedAI = sc.getCapturedPiecesBlue();
//			this.playerAI = StrategoConstants.BLUE;
//		} else {			
//			this.capturedHuman = sc.getCapturedPiecesBlue();
//			this.capturedAI = sc.getCapturedPiecesRed();			
//			this.playerAI =  StrategoConstants.RED;			
//		}
		
//		this.availableMovesAI = new HashMap<Integer, List<Integer>>();
//		this.availableMovesHuman = new HashMap<Integer, List<Integer>>();
//		
//		//Init Available movements. Init discovered pieces
//		for (int index=0; index<pieces.length; index++) {
//			Piece piece = pieces[index];
//			List<Integer> positions = sc.getPossibleMovements(index);			
//			if (positions != null && !positions.isEmpty() ) {
//				if (piece != null) {
//					if (piece.getPlayer() == playerAI) {
//						//AI					
//						this.availableMovesAI.put(index, positions);
//						this.discoveredPiecesAI.add(piece);
//					} else {
//						//Human
//						this.availableMovesHuman.put(index, positions);
//						this.discoveredPiecesHuman.add(piece);
//					}
//					
//				}
//			}
//		}	
		
		this.strategy = new StrategoAIStrategy(this);

	}

	
	@Override
	public void run() {		
		findBestMove ();
	}	
	
	private void findBestMove () {
		int from, to;
		
		//1. Evaluate all possible movements from current state
		
		//2. Evaluate my pieces in danger
		
		//2.1 	How to save my pieces
		//2.2
		
		//3. Evaluate known pieces
		
		//4. Evaluate where is the flag
		
		//5. Evaluate bombs
		
		int [] move = this.strategy.searchMove(board);
		
		listener.moveAI(move[0], move[1]);		
	}

	public StrategoAIListener getListener() {
		return listener;
	}

}
