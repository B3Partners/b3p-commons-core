package nl.b3p.commons.oai.dataprovider20.token;

public class RecordsToken extends Token {
	public String from;

	public String until;

	public String set;

	public String metadataPrefix;

	/**
	 * @return token type
	 */
	public int getType() {
		return Token.RECORDS_TOKEN;
	}

}
