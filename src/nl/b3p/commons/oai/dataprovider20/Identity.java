package nl.b3p.commons.oai.dataprovider20;

/**
 * This interface is used to define the identity of any data provider
 */
public interface Identity {
	/** return oai version */
	public String getOAIversion();

	/** return base url */
	public String getBaseURL();

	/** return data provider name */
	public String getName();

	/** return admin email */
	public String getAdminemail();

	/** return the dp id */
	public String getID();

	/** return the delimiter */
	public String getDelimiter();

	/** return a sample identifier */
	public String getSampleIdentifier();

	/** return schema */
	public String getSchema();

	/** return the earliest date stamp */
	public String getEarliestDatestamp();

	/** return deleted item */
	public String getDeletedItem();

	/** return the granularity */
	public String getGranularity();

}
