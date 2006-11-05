package nl.b3p.commons.oai.dataprovider20;

import java.util.*;

/**
 * Dublin Core record
 */

public class DCRecord {

	// For oai
	private String fullid;

	private String datestamp;

	private String status;

	// For dc only
	private String title;

	private String description;

	private String publisher;

	private String contributor;

	private String date;

	private String sets;

	private String type;

	private String format;

	private Vector identifier;

	private String source;

	private String language;

	private String relation;

	private String coverage;

	private String rights;

	private Vector creator;

	private Vector subject;

	/** convert to string format */
	public String toString() {
		String result = null;
		result = result + "fullid= " + fullid + "/n";
		result = result + " datestamp= " + datestamp + "/n";
		result = result + " title= " + title + "/n";
		result = result + " description =" + description + "/n";
		return result;
	}

    public String getFullid() {
        return fullid;
    }

    public void setFullid(String fullid) {
        this.fullid = fullid;
    }

    public String getDatestamp() {
        return datestamp;
    }

    public void setDatestamp(String datestamp) {
        this.datestamp = datestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Vector getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Vector identifier) {
        this.identifier = identifier;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public Vector getCreator() {
        return creator;
    }

    public void setCreator(Vector creator) {
        this.creator = creator;
    }

    public Vector getSubject() {
        return subject;
    }

    public void setSubject(Vector subject) {
        this.subject = subject;
    }
}
