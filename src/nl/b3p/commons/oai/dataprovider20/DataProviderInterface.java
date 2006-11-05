package nl.b3p.commons.oai.dataprovider20;


import nl.b3p.commons.oai.dataprovider20.error.OAIError;
import org.w3c.dom.*;

/**
 * Every data provider class has to implement this interface
 */
public interface DataProviderInterface {
	/**
	 * 
	 * @return Element
	 */
	public Element identify();

	/**
	 * 
	 * @return Element
	 */
	public Element listSets() throws OAIError;

	/**
	 * 
	 * @param resumptionToken
	 * @return Element
	 */
	public Element listSets(String resumptionToken) throws OAIError;

	/**
	 * 
	 * @param id
	 * @return Element
	 */
	public Element listMetadataFormats(String identifier) throws OAIError;

	/**
	 * 
	 * @param fileafter
	 * @param filebefore
	 * @param setspec
	 * @param metaformat
	 * @return Element
	 */
	public Element listIdentifiers(String fileafter, String filebefore,
			String setspec, String metaformat) throws OAIError;

	/**
	 * 
	 * @param resumptionToken
	 * @return Element
	 */
	public Element listIdentifiers(String resumptionToken) throws OAIError;

	/**
	 * 
	 * @param fileafter
	 * @param filebefore
	 * @param setspec
	 * @param metaformat
	 * @return Element
	 */
	public Element listRecords(String fileafter, String filebefore,
			String setspec, String metaformat) throws OAIError;

	/**
	 * 
	 * @param resumptionToken
	 * @return Element
	 */
	public Element listRecords(String resumptionToken) throws OAIError;

	/**
	 * 
	 * @param handle
	 * @param metaformat
	 * @return Element
	 */
	public Element getRecord(String handle, String metaformat) throws OAIError;
}
