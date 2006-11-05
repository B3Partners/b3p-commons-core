package nl.b3p.commons.oai.dataprovider20.error;

public class NoRecordsMatch extends OAIError {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoRecordsMatch(String text) {

		super(text);
		code = "noRecordsMatch";
	}
}
