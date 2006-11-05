package nl.b3p.commons.oai.dataprovider20.error;

public class BadVerb extends OAIError {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5948424277621011258L;

	public BadVerb(String text) {

		super(text);
		code = "badVerb";
	}
}
