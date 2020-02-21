package de.arvato.game.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.arvato.game.Coordinate;
import de.arvato.game.Piece;
import de.arvato.game.PieceEnum;

public class RandomUtil {

	public static final int MAX_PIECES = 40;
	
	public static final int MAR_MAX = 1;
	public static final int GEN_MAX = 1;
	public static final int COR_MAX = 2;
	public static final int COM_MAX = 3;
	public static final int CAP_MAX = 4;
	public static final int TEN_MAX = 4;
	public static final int SAR_MAX = 4;
	public static final int MIN_MAX = 5;
	public static final int EXP_MAX = 8;
	public static final int ESP_MAX = 1;
	public static final int BOM_MAX = 6;
	public static final int BAN_MAX = 1;
	
	private static final Random rng = new Random();
	
	public static Piece [] distributePiecesRandom (final int player) {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		Set<Integer> generated = new LinkedHashSet<Integer>();
		while (generated.size() < MAX_PIECES)
		{
			Integer next;
			if (player == 1) {
				next = rng.nextInt(MAX_PIECES);
			} else {
				next = randomBetween(60, 100);
			}
			generated.add(next);
		}
		Iterator<Integer> it = generated.iterator();
		while (it.hasNext()) {
			int tmp = it.next();
			int x,y;
			if (tmp <10) {
				x = tmp;
				y = 0;
			} else {
				x = Integer.valueOf(String.valueOf(tmp).substring(1)).intValue();
				y = Integer.valueOf(String.valueOf(tmp).substring(0,1)).intValue();
			}
			coordinates.add(new Coordinate(x, y));
			//System.out.println(String.format("(%d,%d)", x, y));			
		}

		HashMap<PieceEnum, Integer> mapBoard = new HashMap<PieceEnum, Integer>();
		mapBoard.put  (PieceEnum.MARISCAL, MAR_MAX);
		mapBoard.put  (PieceEnum.GENERAL, GEN_MAX);
		mapBoard.put  (PieceEnum.CORONEL, COR_MAX);
		mapBoard.put  (PieceEnum.COMANDANTE, COM_MAX);
		mapBoard.put  (PieceEnum.CAPITAN, CAP_MAX);
		mapBoard.put  (PieceEnum.TENIENTE, TEN_MAX);
		mapBoard.put  (PieceEnum.SARGENTO, SAR_MAX);
		mapBoard.put  (PieceEnum.MINERA, MIN_MAX);
		mapBoard.put  (PieceEnum.EXPLORADOR, EXP_MAX);
		mapBoard.put  (PieceEnum.ESPIA, ESP_MAX);
		mapBoard.put  (PieceEnum.BOMBA, BOM_MAX);
		mapBoard.put  (PieceEnum.BANDERA, BAN_MAX);

		int pos = 0;
		Piece [] res = new Piece[MAX_PIECES];
		for (Map.Entry<PieceEnum, Integer> entry : mapBoard.entrySet()) {
			for (int j=0;j<entry.getValue();j++) {
				Piece mock = new Piece(player, entry.getKey());
				mock.jumpTo(coordinates.get(pos).x, coordinates.get(pos).y);
				res[pos++] = mock;
			}
		}
		return res;
	}
	
	public static int randomBetween (int low, int high) {
		return rng.nextInt(high-low) + low;		
	}
}
