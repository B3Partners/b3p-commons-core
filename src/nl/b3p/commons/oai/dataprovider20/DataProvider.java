/*
 * B3P Commons Core is a library with commonly used classes for webapps.
 * Included are clieop3, oai, security, struts, taglibs and other
 * general helper classes and extensions.
 *
 * Copyright 2000 - 2008 B3Partners BV
 * 
 * This file is part of B3P Commons Core.
 * 
 * B3P Commons Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Commons Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Commons Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.commons.oai.dataprovider20;

import java.util.*;
import java.text.*;
import nl.b3p.commons.oai.dataprovider20.error.BadArgument;
import nl.b3p.commons.oai.dataprovider20.error.BadResumptionToken;
import nl.b3p.commons.oai.dataprovider20.error.CannotDisseminateFormat;
import nl.b3p.commons.oai.dataprovider20.error.IdDoesNotExist;
import nl.b3p.commons.oai.dataprovider20.error.NoRecordsMatch;
import nl.b3p.commons.oai.dataprovider20.error.OAIError;
import nl.b3p.commons.oai.dataprovider20.token.IdentifierToken;
import nl.b3p.commons.oai.dataprovider20.token.RecordsToken;
import nl.b3p.commons.oai.dataprovider20.token.SetToken;
import nl.b3p.commons.oai.dataprovider20.token.Token;
import nl.b3p.commons.oai.dataprovider20.token.TokenModem;

import org.w3c.dom.*;

/**
 * Data provider class
 */
public class DataProvider implements DataProviderInterface {

    Identity config;
    RecordFactory rf = null;
    int cursor = 0;
    Document doc = null;

    /**
     * Constructor.
     *
     * @param config
     * @param rf
     * @param doc
     */
    public DataProvider(Identity config, RecordFactory rf, Document doc) {
        this.config = config;
        this.rf = rf;
        this.doc = doc;
    }

    /**
     * Constructor
     *
     * @param config
     * @param rf
     */
    public DataProvider(Identity config, RecordFactory rf) {
        this.config = config;
        this.rf = rf;
    }

    /**
     *
     * @param doc
     */
    public void setDocument(Document doc) {
        this.doc = doc;
    }

    /**
     *
     * @return Element
     */
    public Element identify() {

        Element root = doc.createElement("Identify");
        Element curr;
        doc.appendChild(root);

        curr = doc.createElement("repositoryName");
        curr.appendChild(doc.createTextNode(config.getName()));
        root.appendChild(curr);

        curr = doc.createElement("baseURL");
        curr.appendChild(doc.createTextNode(config.getBaseURL()));
        root.appendChild(curr);

        curr = doc.createElement("protocolVersion");
        curr.appendChild(doc.createTextNode(config.getOAIversion()));
        root.appendChild(curr);

        curr = doc.createElement("adminEmail");
        curr.appendChild(doc.createTextNode(config.getAdminemail()));
        root.appendChild(curr);

        curr = doc.createElement("earliestDatestamp");
        curr.appendChild(doc.createTextNode(config.getEarliestDatestamp()));
        root.appendChild(curr);

        curr = doc.createElement("deletedRecord");
        curr.appendChild(doc.createTextNode(config.getDeletedItem()));
        root.appendChild(curr);

        curr = doc.createElement("granularity");
        curr.appendChild(doc.createTextNode(config.getGranularity()));
        root.appendChild(curr);

        curr = doc.createElement("description");
        root.appendChild(curr);
        Element oaiIdentifier = doc.createElement("oai-identifier");
        curr.appendChild(oaiIdentifier);

        oaiIdentifier.setAttribute("xmlns",
                "http://www.openarchives.org/OAI/2.0/oai-identifier");
        oaiIdentifier.setAttribute("xmlns:xsi",
                "http://www.w3.org/2001/XMLSchema-instance");
        oaiIdentifier.setAttribute(
                "xsi:schemaLocation",
                "http://www.openarchives.org/OAI/2.0/oai-identifier  http://www.openarchives.org/OAI/2.0/oai-identifier.xsd");

        curr = doc.createElement("scheme");
        curr.appendChild(doc.createTextNode(config.getSchema()));
        oaiIdentifier.appendChild(curr);

        curr = doc.createElement("repositoryIdentifier");
        curr.appendChild(doc.createTextNode(config.getID()));
        oaiIdentifier.appendChild(curr);

        curr = doc.createElement("delimiter");
        curr.appendChild(doc.createTextNode(config.getDelimiter()));
        oaiIdentifier.appendChild(curr);

        curr = doc.createElement("sampleIdentifier");
        curr.appendChild(doc.createTextNode(config.getSampleIdentifier()));
        oaiIdentifier.appendChild(curr);
        return root;

    }

    /**
     *
     * @return Element
     */
    public Element listSets() throws OAIError {

        Element root = doc.createElement("ListSets");
        Element curr, parent;

        Vector v;
        try {
            v = rf.getSets(cursor, Token.TOKENSIZE);
        } catch (Exception ex) {
            throw new NoRecordsMatch(ex.toString());
        }
        doc.appendChild(root);

        for (Enumeration enu = v.elements(); enu.hasMoreElements();) {
            curr = doc.createElement("set");
            root.appendChild(curr);
            parent = curr;

            String name = (String) enu.nextElement();
            curr = doc.createElement("setSpec");
            curr.appendChild(doc.createTextNode(name));
            parent.appendChild(curr);

            curr = doc.createElement("setName");
            curr.appendChild(doc.createTextNode(name));
            parent.appendChild(curr);
        }

        if (v.size() == Token.TOKENSIZE) {
            SetToken token = new SetToken();
            token.setCursor(cursor + Token.TOKENSIZE);
            curr = doc.createElement("resumptionToken");
            curr.appendChild(doc.createTextNode(TokenModem.encode(token)));
            root.appendChild(curr);
        }
        // reset cursor;
        cursor = 0;
        return root;

    }

    /**
     *
     * @param resumptionToken
     * @return Element
     */
    public Element listSets(String resumptionToken) throws OAIError {
        Token token = TokenModem.decode(resumptionToken);
        if (token.getType() == Token.SET_TOKEN) {
            cursor = token.getCursor();
            return listSets();
        } else {
            throw new BadResumptionToken("malformat");
        }
    }

    /**
     *
     * @param id
     * @return Element
     */
    public Element listMetadataFormats(String id) throws OAIError {

        DCRecord record = null;
        if (id != null) {
            try {
                record = rf.getRecord(id);
            } catch (Exception ex) {
                throw new NoRecordsMatch(ex.toString());
            }

            if (record == null) {
                throw new IdDoesNotExist("id not exist");
            }
        }

        Element root = doc.createElement("ListMetadataFormats");

        Element curr, parent;
        doc.appendChild(root);

        if ((record != null) || (id == null)) {
            curr = doc.createElement("metadataFormat");
            root.appendChild(curr);
            parent = curr;

            curr = doc.createElement("metadataPrefix");
            curr.appendChild(doc.createTextNode("oai_dc"));
            parent.appendChild(curr);

            curr = doc.createElement("schema");
            curr.appendChild(doc.createTextNode("http://www.openarchives.org/OAI/2.0/oai_dc.xsd"));
            parent.appendChild(curr);

            curr = doc.createElement("metadataNamespace");
            curr.appendChild(doc.createTextNode("http://www.openarchives.org/OAI/2.0/oai_dc"));
            parent.appendChild(curr);

        } else {
            throw new IdDoesNotExist("id not exist");
        }
        return root;
    }

    /**
     *
     * @param fileafter
     * @param filebefore
     * @param setspec
     * @param metaformat
     * @return Element
     */
    public Element listIdentifiers(String fileafter, String filebefore,
            String setspec, String metaformat) throws OAIError {

        if ((fileafter != null) && (!fileafter.trim().equals(""))) {
            fileafter = checkValidaty(fileafter);
        } else {
            fileafter = null;
        }
        if ((filebefore != null) && (!filebefore.trim().equals(""))) {
            filebefore = checkValidaty(filebefore);
        } else {
            filebefore = null;
        }
        if (metaformat == null) {
            throw new BadArgument("metadataPrefix error");
        }
        if ((setspec == null) || (setspec.trim().equals(""))) {
            setspec = null;
        }
        if ((metaformat != null) && (!metaformat.trim().equals("")) && (!metaformat.equals("oai_dc"))) {
            throw new CannotDisseminateFormat("can not disseminate " + metaformat);
        }
        Vector v;
        try {
            v = rf.getRecords(fileafter, filebefore, setspec, cursor, Token.TOKENSIZE);
        } catch (Exception ex) {
            throw new NoRecordsMatch(ex.toString());
        }
        if ((v == null) || (v.isEmpty())) {
            throw new NoRecordsMatch("not items match");
        }
        Element root = doc.createElement("ListIdentifiers");
        Element curr;
        doc.appendChild(root);

        for (Enumeration e = v.elements(); e.hasMoreElements();) {
            DCRecord record = (DCRecord) (e.nextElement());
            curr = doc.createElement("header");
            Element header = curr;
            root.appendChild(header);

            if (record.getFullid() != null) {
                curr = doc.createElement("identifier");
                curr.appendChild(doc.createTextNode(record.getFullid()));
                header.appendChild(curr);
            }

            if (record.getDatestamp() != null) {
                curr = doc.createElement("datestamp");
                curr.appendChild(doc.createTextNode(record.getDatestamp()));
                header.appendChild(curr);
            }

            if (record.getSets() != null) {
                curr = doc.createElement("setSpec");
                curr.appendChild(doc.createTextNode(record.getSets()));
                header.appendChild(curr);
            }
        }

        if (Token.TOKENSIZE == v.size()) {
            IdentifierToken token = new IdentifierToken();
            token.setCursor(cursor + Token.TOKENSIZE);
            token.from = fileafter;
            token.until = filebefore;
            token.metadataPrefix = metaformat;
            token.set = setspec;
            curr = doc.createElement("resumptionToken");
            curr.appendChild(doc.createTextNode(TokenModem.encode(token)));
            root.appendChild(curr);
        }

        // reset cursor
        cursor = 0;
        return root;

    }

    /**
     *
     * @param resumptionToken
     * @return Element
     */
    public Element listIdentifiers(String resumptionToken) throws OAIError {
        IdentifierToken token = (IdentifierToken) (TokenModem.decode(resumptionToken));
        if (token.getType() == Token.IDENTIFIER_TOKEN) {
            cursor = token.getCursor();
            return listIdentifiers(token.from, token.until, token.set,
                    token.metadataPrefix);
        } else {
            throw new BadResumptionToken("malformat");
        }
    }

    /**
     *
     * @param fileafter
     * @param filebefore
     * @param setspec
     * @param metaformat
     * @return Element
     */
    public Element listRecords(String fileafter, String filebefore,
            String setspec, String metaformat) throws OAIError {

        if ((fileafter != null) && (!fileafter.trim().equals(""))) {
            fileafter = checkValidaty(fileafter);
        } else {
            fileafter = null;
        }
        if ((filebefore != null) && (!filebefore.trim().equals(""))) {
            filebefore = checkValidaty(filebefore);
        } else {
            filebefore = null;
        }
        if (metaformat == null) {
            throw new BadArgument("metadataPrefix error");
        }
        if (!metaformat.equals("oai_dc")) {
            throw new CannotDisseminateFormat("can not disseminate " + metaformat);
        }
        Vector v;
        try {
            v = rf.getRecords(fileafter, filebefore, setspec, cursor, Token.TOKENSIZE);
        } catch (Exception ex) {
            throw new NoRecordsMatch(ex.toString());
        }
        if ((v == null) || (v.isEmpty())) {
            throw new NoRecordsMatch("no items match");
        }
        Element root = doc.createElement("ListRecords");

        Element curr;
        doc.appendChild(root);
        for (Enumeration e = v.elements(); e.hasMoreElements();) {
            createRecordElement(doc, root, (DCRecord) (e.nextElement()),
                    metaformat);
        }

        if (Token.TOKENSIZE == v.size()) {
            RecordsToken token = new RecordsToken();
            token.setCursor(cursor + Token.TOKENSIZE);
            token.from = fileafter;
            token.until = filebefore;
            token.set = setspec;
            token.metadataPrefix = metaformat;
            curr = doc.createElement("resumptionToken");
            curr.appendChild(doc.createTextNode(TokenModem.encode(token)));
            root.appendChild(curr);
        }

        // reset cursor
        cursor = 0;

        return root;
    }

    /**
     *
     * @param resumptionToken
     * @return Element
     */
    public Element listRecords(String resumptionToken) throws OAIError {
        RecordsToken token = (RecordsToken) (TokenModem.decode(resumptionToken));

        if (token.getType() == Token.RECORDS_TOKEN) {
            cursor = token.getCursor();
            return listRecords(token.from, token.until, token.set,
                    token.metadataPrefix);
        } else {
            throw new BadResumptionToken("malformat");
        }
    }

    /**
     *
     * @param handle
     * @param metaformat
     * @return Element
     */
    public Element getRecord(String handle, String metaformat) throws OAIError {
        if ((metaformat == null) || (handle == null)) {
            throw new BadArgument("Missing Mandatory Field");        // handle=handle.substring(handle.lastIndexOf(":")+1);
        }
        if (!metaformat.equals("oai_dc")) {
            throw new CannotDisseminateFormat("can not disseminate " + metaformat);
        }
        DCRecord record;
        try {
            record = rf.getRecord(handle);
        } catch (Exception ex) {
            throw new IdDoesNotExist(ex.toString());
        }
        if (record == null) {
            throw new IdDoesNotExist("id not exist");
        }
        Element root = doc.createElement("GetRecord");

        doc.appendChild(root);

        createRecordElement(doc, root, record, metaformat);

        return root;
    }

    private void createRecordElement(Document doc, Element root,
            DCRecord record, String metaformat) {
        Element recordElmt, curr, header, metadata, dc;
        curr = doc.createElement("record");
        root.appendChild(curr);
        recordElmt = curr;

        curr = doc.createElement("header");
        header = curr;
        recordElmt.appendChild(header);

        if (record.getFullid() != null) {
            curr = doc.createElement("identifier");
            curr.appendChild(doc.createTextNode(record.getFullid()));
            header.appendChild(curr);
        }

        if (record.getDatestamp() != null) {
            curr = doc.createElement("datestamp");
            curr.appendChild(doc.createTextNode(record.getDatestamp()));
            header.appendChild(curr);
        }

        if (record.getSets() != null) {
            curr = doc.createElement("setSpec");
            curr.appendChild(doc.createTextNode(record.getSets()));
            header.appendChild(curr);
        }

        if (!metaformat.equals("oai_dc")) {
            return;
        }
        metadata = doc.createElement("metadata");
        recordElmt.appendChild(metadata);
        curr = doc.createElement("oai_dc:dc");
        addDCNameSpace(curr);
        dc = curr;
        metadata.appendChild(dc);

        if (record.getTitle() != null) {
            curr = doc.createElement("dc:title");
            curr.appendChild(doc.createTextNode(record.getTitle()));
            dc.appendChild(curr);
        }

        if (record.getFormat() != null) {
            curr = doc.createElement("dc:format");
            curr.appendChild(doc.createTextNode(record.getFormat()));
            dc.appendChild(curr);
        }

        if (record.getCreator() != null) {
            for (Enumeration e = record.getCreator().elements(); e.hasMoreElements();) {
                curr = doc.createElement("dc:creator");
                curr.appendChild(doc.createTextNode((String) (e.nextElement())));
                dc.appendChild(curr);
            }
        }

        if (record.getSubject() != null) {
            for (Enumeration e = record.getSubject().elements(); e.hasMoreElements();) {
                curr = doc.createElement("dc:subject");
                curr.appendChild(doc.createTextNode((String) (e.nextElement())));
                dc.appendChild(curr);
            }
        }

        if (record.getDescription() != null) {
            curr = doc.createElement("dc:description");
            curr.appendChild(doc.createTextNode(record.getDescription()));
            dc.appendChild(curr);
        }

        if (record.getRights() != null) {
            curr = doc.createElement("dc:rights");
            curr.appendChild(doc.createTextNode(record.getRights()));
            dc.appendChild(curr);
        }

        if (record.getPublisher() != null) {
            curr = doc.createElement("dc:publisher");
            curr.appendChild(doc.createTextNode(record.getPublisher()));
            dc.appendChild(curr);
        }

        if (record.getContributor() != null) {
            curr = doc.createElement("dc:contributor");
            curr.appendChild(doc.createTextNode(record.getContributor()));
            dc.appendChild(curr);
        }

        if (record.getDate() != null) {
            curr = doc.createElement("dc:date");
            curr.appendChild(doc.createTextNode(record.getDate()));
            dc.appendChild(curr);
        }

        if (record.getType() != null) {
            curr = doc.createElement("dc:type");
            curr.appendChild(doc.createTextNode(record.getType()));
            dc.appendChild(curr);
        }

        if (record.getIdentifier() != null) {
            for (Enumeration e = record.getIdentifier().elements(); e.hasMoreElements();) {
                curr = doc.createElement("dc:identifier");
                curr.appendChild(doc.createTextNode((String) (e.nextElement())));
                dc.appendChild(curr);
            }
        }

        if (record.getSource() != null) {
            curr = doc.createElement("dc:source");
            curr.appendChild(doc.createTextNode(record.getSource()));
            dc.appendChild(curr);
        }

        if (record.getLanguage() != null) {
            curr = doc.createElement("dc:language");
            curr.appendChild(doc.createTextNode(record.getLanguage()));
            dc.appendChild(curr);
        }

        if (record.getRelation() != null) {
            curr = doc.createElement("dc:relation");
            curr.appendChild(doc.createTextNode(record.getRelation()));
            dc.appendChild(curr);
        }

    }

    private String checkValidaty(String day) throws OAIError {

        SimpleDateFormat formatter;

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date;

        formatter.setLenient(true);
        try {
            date = formatter.parse(day);
        } catch (Exception e) {
            throw new BadArgument("date format error");
        }

        if (day.length() > 10) {
            throw new BadArgument("bad Granularity for " + day);
        }
        return formatter.format(date);

    }

    private void addDCNameSpace(Element e) {

        e.setAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
        e.setAttribute("xmlns:oai_dc",
                "http://www.openarchives.org/OAI/2.0/oai_dc/");
        e.setAttribute("xmlns:xsi",
                "http://www.w3.org/2001/XMLSchema-instance");
        e.setAttribute(
                "xsi:schemaLocation",
                "http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd");
    }
}
