package nl.b3p.commons.oai.dataprovider20.error;

public class CannotDisseminateFormat extends OAIError {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6154636510349161472L;

	public CannotDisseminateFormat(String text) {

		super(text);
		code = "cannotDisseminateFormat";
	}
}
