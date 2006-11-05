package nl.b3p.commons.oai.dataprovider20.error;

public class NoItemsMatch extends OAIError {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoItemsMatch(String text) {

		super(text);
		code = "noItemsMatch";
	}
}
