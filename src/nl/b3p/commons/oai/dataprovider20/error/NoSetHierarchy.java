package nl.b3p.commons.oai.dataprovider20.error;

public class NoSetHierarchy extends OAIError {
	/**
	 * 
	 */
	private static final long serialVersionUID = -997195075988902731L;

	public NoSetHierarchy(String text) {
		super(text);
		code = "noSetHierarchy";
	}
}
