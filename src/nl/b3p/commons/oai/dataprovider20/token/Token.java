package nl.b3p.commons.oai.dataprovider20.token;

public abstract class Token {
	public static int TOKENSIZE = 100;

	public static final int SET_TOKEN = 1;

	public static final int IDENTIFIER_TOKEN = 2;

	public static final int RECORDS_TOKEN = 3;

	private int cursor;

	public void setCursor(int cursor) {
		this.cursor = cursor;
	}

	public int getCursor() {
		return cursor;
	}

	public abstract int getType();

}
