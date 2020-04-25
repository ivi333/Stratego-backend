package de.arvato.stratego;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.arvato.game.util.Pos;
import de.arvato.stratego.StrategoConstants.GameStatus;
import javafx.util.Pair;

public class Board {
	protected Piece [] pieces;
	protected int humanPlayer;    
	protected int turn;
	protected Map<PieceEnum, Integer> capturedPiecesRed;
	protected Map<PieceEnum, Integer> capturedPiecesBlue;
	protected List<Piece> discoveredPiecesHuman, discoveredPiecesAI;
	protected List<Pair> fights;
	protected StrategoConstants.GameStatus gameStatus;
	
	public Board () {
		pieces = new Piece [100];
		capturedPiecesBlue = new HashMap<PieceEnum, Integer>();
		capturedPiecesRed = new HashMap<PieceEnum, Integer>();
		fights = new ArrayList<Pair>();
	}
	
	public void changeGameStatus (GameStatus status) {
		this.gameStatus = status;
	}

	public void initTurn (final int player) {
		this.turn = player;
	}

	public void humanPlayer (final int player) {
		this.humanPlayer = player;
	}

	public int getHumanPlayer () {
		return this.humanPlayer;
	}

	public int getAIPlayer () {
		return this.humanPlayer == StrategoConstants.RED ? StrategoConstants.BLUE : StrategoConstants.RED;
	}

	public int getTurn () {
		return this.turn;
	}

	public void changeTurn () {
		if (turn == StrategoConstants.RED) {
			turn = StrategoConstants.BLUE;
		} else {
			turn = StrategoConstants.RED;
		}
	}

	public void initBoard (int to, int selectPos) {
		int x1, x2;
		if (humanPlayer == StrategoConstants.RED) {
			x1 = StrategoConstants.RED_PLAYER[0];
			x2 = StrategoConstants.RED_PLAYER[1];
		} else {
			x1 = StrategoConstants.BLUE_PLAYER[0];
			x2 = StrategoConstants.BLUE_PLAYER[1];
		}
		if (selectPos != -1 && to >= x1 && to < x2 ) {
			//interchange pos
			Piece tmp = pieces[to];
			pieces[to] = pieces[selectPos];
			pieces[selectPos] = tmp;
		}
		selectPos = -1;
	}
	
	public void move (int to, int selectPos) {
        if (selectPos != -1 && isPlayablePosition(to)) {
            if (!isValidMovement (selectPos, to)) {
                return;
            }
            Piece pieceFrom = pieces[selectPos];
            Piece pieceTo = pieces[to];

            PieceFight.PieceFlighStatus fightStatus = PieceFight.fight(pieceFrom, pieceTo);
            if (fightStatus == PieceFight.PieceFlighStatus.NO_FIGHT) {
                pieces[to] = pieces[selectPos];
                pieces[selectPos] = null;
            } else if (fightStatus == PieceFight.PieceFlighStatus.TIE) {
                pieces[selectPos] = null;
                pieces[to] = null;
                capturePiece(pieceFrom.getPlayer(), pieceFrom.getPieceEnum());
                capturePiece(pieceTo.getPlayer(), pieceTo.getPieceEnum());
            } else if (fightStatus == PieceFight.PieceFlighStatus.FLAG_CAPTURED) {
                pieces[to] = pieces[selectPos];
                pieces[selectPos] = null;
                capturePiece(pieceTo.getPlayer(), pieceTo.getPieceEnum());
                finishGame ();
            } else if (fightStatus == PieceFight.PieceFlighStatus.PIECE1_WIN) {
                pieces[to] = pieces[selectPos];
                pieces[selectPos] = null;
                capturePiece(pieceTo.getPlayer(), pieceTo.getPieceEnum());
                pieceFrom.setPieceDiscovered(true);
            } else if (fightStatus == PieceFight.PieceFlighStatus.PIECE2_WIN) {
                pieces[selectPos] = null;
                capturePiece(pieceFrom.getPlayer(), pieceFrom.getPieceEnum());
                pieceTo.setPieceDiscovered(true);
            }

            if (fightStatus != PieceFight.PieceFlighStatus.NO_FIGHT) {
                Pair e = new Pair(pieceFrom, pieceTo);
                fights.add(e);
            }

            selectPos = -1;
            changeTurn ();
        }
    }
	
    private void capturePiece (final int player, PieceEnum piece) {
        if (StrategoConstants.RED == player) {
            if (capturedPiecesRed.containsKey(piece)) {
                Integer i = capturedPiecesRed.get(piece);
                capturedPiecesRed.put(piece, ++i);
            } else {
                capturedPiecesRed.put(piece, 1);
            }
        } else {
            if (capturedPiecesBlue.containsKey(piece)) {
                Integer i = capturedPiecesBlue.get(piece);
                capturedPiecesBlue.put(piece, ++i);
            } else {
                capturedPiecesBlue.put(piece, 1);
            }
        }
    }
    
	public void finishGame() {
    	changeGameStatus(GameStatus.FINISH);
    }
	
    
    public Piece getPieceAt(int i) {
        return pieces[i];
    }
    
    public boolean isValidMovement (int selectedPos, int pos) {
        List<Integer> possibilities = getPossibleMovements(selectedPos);
        boolean res=false;
        for (Integer x : possibilities) {
            if (x.equals(pos)) {
                res=true;
                break;
            }
        }
        return res;
    }
    
    public List<Integer> getPossibleMovements (int pos) {
        List<Integer> res = Collections.emptyList();
        switch (gameStatus) {
            case PLAY:
                Piece piece = pieces[pos];
                if (piece == null) {
                    return res;
                }
                if (piece.getPieceEnum().isAllowMovement()) {
                    switch (piece.getPieceEnum()) {
                        case SCOUT:
                            res = calculateMovement (piece, pos, true);
                            break;
                        default:
                            res = calculateMovement (piece, pos, false);
                            break;
                    }
                }
                break;
        }
       return res;
    }
    
    private List<Integer> calculateMovement(final Piece piece, final int pos, final boolean isMultiple) {
        int row = Pos.row(pos);
        int col = Pos.col(pos);
        List<Integer> freePos = new ArrayList<Integer>();
        int nextPos;
        if (isMultiple) {
            int tmpRow = row;
            boolean available=true;
            while ( ((tmpRow -1) >=0) && available) {
                nextPos = Pos.fromColAndRow(col, (tmpRow-1));
                if (nextPos >= 0 && nextPos < 100) {
                    available = nextPositionAvailable(piece, nextPos);
                    if (available) {
                        freePos.add(nextPos);
                    }
                    if (pieces[nextPos] != null) break;
                }
                tmpRow--;
            }

            tmpRow = row;
            available = true;
            while ( ((tmpRow + 1) < 10) && available) {
                nextPos = Pos.fromColAndRow(col, (tmpRow +1));
                if (nextPos >= 0 && nextPos < 100) {
                    available = nextPositionAvailable(piece, nextPos);
                    if (available) {
                        freePos.add(nextPos);
                    }
                    if (pieces[nextPos] != null) break;
                }
                tmpRow++;
            }

            available = true;
            int tmpPos = pos;
            int tmpCol = col;
            while ( (Pos.row(tmpPos+1) == row) && available) {
                nextPos = Pos.fromColAndRow((tmpCol+1), row);
                if (nextPos >= 0 && nextPos < 100) {
                    available = nextPositionAvailable(piece, nextPos);
                    if (available) {
                        freePos.add(nextPos);
                    }
                    if (pieces[nextPos] != null) break;
                }
                tmpPos++;
                tmpCol++;
            }

            tmpPos=pos;
            tmpCol=col;
            available=true;
            while ( (Pos.row(tmpPos-1) == row) && available) {
                nextPos = Pos.fromColAndRow((tmpCol-1), row);
                if (nextPos >= 0 && nextPos < 100) {
                    available = nextPositionAvailable (piece, nextPos);
                    if (available) {
                        freePos.add(nextPos);
                    }
                    if (pieces[nextPos] != null) break;
                }
                tmpPos--;
                tmpCol--;
            }
        } else {
            if ( (row -1) >= 0) {
                nextPos = Pos.fromColAndRow(col, (row -1));
                if (nextPos >= 0 && nextPos < 100) {
                    boolean available = nextPositionAvailable(piece, nextPos);
                    if (available) {
                        freePos.add(nextPos);
                    }
                }
            }

            if ( (row + 1) < 10) {
                nextPos = Pos.fromColAndRow(col, (row +1));
                if (nextPos >= 0 && nextPos < 100) {
                    boolean available = nextPositionAvailable(piece, nextPos);
                    if (available) {
                        freePos.add(nextPos);
                    }
                }
            }

            if ( Pos.row(pos+1) == row ) {
                nextPos = Pos.fromColAndRow((col+1), row);
                if (nextPos >= 0 && nextPos < 100) {
                    boolean available = nextPositionAvailable(piece, nextPos);
                    if (available) {
                        freePos.add(nextPos);
                    }
                }
            }

            if ( Pos.row(pos-1) == row) {
                nextPos = Pos.fromColAndRow((col-1), row);
                if (nextPos >= 0 && nextPos < 100) {
                    boolean available = nextPositionAvailable (piece, nextPos);
                    if (available) {
                        freePos.add(nextPos);
                    }
                }
            }
        }
        return freePos;
    }

    private boolean nextPositionAvailable(Piece piece, int nextPos) {
        // not playable position
        if (!isPlayablePosition(nextPos)) return false;
        // Board position is free
        Piece nextPiece = pieces[nextPos];
        if (nextPiece == null) return true;
        //Opposite piece
        if (nextPiece.getPlayer() != piece.getPlayer()) return true;
        // by default is not playable
        return false;
    }
	
    public static boolean isPlayablePosition (final int pos) {
        return pos != StrategoConstants.c6 && pos != StrategoConstants.d6
                && pos != StrategoConstants.c5 && pos != StrategoConstants.d5
                && pos != StrategoConstants.g6 && pos != StrategoConstants.h6
                && pos != StrategoConstants.g5 && pos != StrategoConstants.h5;
    }

	public void printPieces () {
		for (int z=0;z<100;z++) {
			StringBuffer sb = new StringBuffer();
			sb.append("Pos ");
			sb.append(z);
			sb.append(" : ");
			if (pieces[z] != null) {
				sb.append(pieces[z].getPieceEnum().name());
				sb.append(" player:");
				sb.append(pieces[z].getPlayer());
			} else {
				sb.append("EMPTY");
			}

			System.out.println(sb.toString());
		}
	}

	public StrategoConstants.GameStatus getGameStatus() {
		return gameStatus;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Board [pieces=");
		builder.append(Arrays.toString(pieces));
		builder.append(", humanPlayer=");
		builder.append(humanPlayer);
		builder.append(", turn=");
		builder.append(turn);
		builder.append(", capturedPiecesRed=");
		builder.append(capturedPiecesRed);
		builder.append(", capturedPiecesBlue=");
		builder.append(capturedPiecesBlue);
		builder.append(", discoveredPiecesHuman=");
		builder.append(discoveredPiecesHuman);
		builder.append(", discoveredPiecesAI=");
		builder.append(discoveredPiecesAI);
		builder.append(", fights=");
		builder.append(fights);
		builder.append(", gameStatus=");
		builder.append(gameStatus);
		builder.append("]");
		return builder.toString();
	}
}
