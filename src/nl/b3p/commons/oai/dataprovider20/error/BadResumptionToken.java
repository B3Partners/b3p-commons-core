package nl.b3p.commons.oai.dataprovider20.error;

public class BadResumptionToken extends OAIError {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4271114110745517858L;

	public BadResumptionToken(String text) {

		super(text);
		code = "badResumptionToken";
	}
}
