package nl.b3p.commons.oai.dataprovider20.error;

public class OAIError extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2154794218770572543L;

	public String code = null;

	OAIError(String text) {
		super(text);
	}

	public String getCode() {
		return code;
	}

}
