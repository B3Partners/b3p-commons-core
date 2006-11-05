package nl.b3p.commons.oai.dataprovider20.error;

public class BadGranularity extends OAIError {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3548668521832176623L;

	public BadGranularity(String text) {

		super(text);
		code = "badGranularity";
	}
}
