package de.arvato.game;

/**
 * 40 en total
 */
public enum PieceEnum {
	MARISCAL ("MAR", 10), 	/* 1 */
	GENERAL ("GEN", 9),		/* 1 */
	CORONEL ("COR", 8),	  	/* 2 */
	COMANDANTE ("COM", 7),  /* 3 */
	CAPITAN ("CAP", 6),  	/* 4 */ 
	TENIENTE ("TEN", 5),	/* 4 */
	SARGENTO ("SAR", 4),	/* 4 */
	MINERA ("MIN", 3),		/* 5 */
	EXPLORADOR ("EXP", 2),	/* 8 */
	ESPIA ("ESP", 1),		/* 1 */
	BOMBA ("BOM", -1),		/* 6 */
	BANDERA ("BAN", -1),	/* 1 */
	FORBIDDEN ("FORB", -1); /* 8 */ 
	private final String name;
	private final int point;
	PieceEnum (final String n, final int p) {
		this.name = n;
		this.point = p;
	}
	public String getName() {
		return name;
	}
	public int getPoint() {
		return point;
	}
}
