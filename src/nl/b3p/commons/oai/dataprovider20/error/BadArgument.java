package nl.b3p.commons.oai.dataprovider20.error;

public class BadArgument extends OAIError {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1304531325440299776L;

	public BadArgument(String text) {
		super(text);
		code = "badArgument";
	}
}
