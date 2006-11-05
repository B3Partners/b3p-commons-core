package nl.b3p.commons.oai.dataprovider20.error;

public class IdDoesNotExist extends OAIError {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IdDoesNotExist(String text) {

		super(text);
		code = "idDoesNotExist";
	}
}
