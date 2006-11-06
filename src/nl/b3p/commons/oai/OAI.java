package nl.b3p.commons.oai;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.text.*;
import nl.b3p.commons.oai.dataprovider20.DataProvider;
import nl.b3p.commons.oai.dataprovider20.Identity;
import nl.b3p.commons.oai.dataprovider20.RecordFactory;
import nl.b3p.commons.oai.dataprovider20.error.BadArgument;
import nl.b3p.commons.oai.dataprovider20.error.BadVerb;
import nl.b3p.commons.oai.dataprovider20.error.OAIError;
import nl.b3p.commons.oai.util.xml.XMLTool;

import org.w3c.dom.*;


public class OAI extends HttpServlet
        
{
    private static final long serialVersionUID = -6065554885961025291L;
    
    // these two parameters should be formatted by implementing class
    static protected Identity config = null;
    static protected RecordFactory rf = null;
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        work(request, response);
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        work(request, response);
    }
    
    // ////////////////////////////////////////////////////
    public void work(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/xml;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String formname = request.getParameter("verb");
        String token = request.getParameter("resumptionToken");
        Element elem = null;
        Document doc = XMLTool.createDocumentRoot();
        DataProvider dp = new DataProvider(getConfig(), getRf(), doc);
        OAIError oaierror = null;
        if ((token == null) || token.trim().equals(""))
            token = null;
        
        try {
            
            if ((formname == null) || formname.trim().equals(""))
                throw new BadVerb("no verb specified!");
            
            // check the number of parameters
            int numofparameters = 0;
            Hashtable params = new Hashtable();
            
            String queryString = request.getQueryString();
            if (queryString != null) {
                StringTokenizer st = new StringTokenizer(queryString, "&");
                while (st.hasMoreTokens()) {
                    Object o = params.put(st.nextToken(), "anything");
                    if (o != null)
                        throw new BadArgument("Duplicate Argument");
                    numofparameters++;
                }
            }
            if ((token != null) && (numofparameters > 2))
                throw new BadArgument("The wrong argument with resumptionToken");
            
            if (formname.equals("Identify")) {
                if (numofparameters != 1)
                    throw new BadArgument("bad argument for Identify");
                elem = dp.identify();
            }
            
            else if (formname.equals("ListIdentifiers")) {
                if (token == null) {
                    elem = dp.listIdentifiers(clear(request
                            .getParameter("from")), clear(request
                            .getParameter("until")), clear(request
                            .getParameter("set")), clear(request
                            .getParameter("metadataPrefix")));
                } else {
                    elem = dp.listIdentifiers(token);
                }
            }
            
            else if (formname.equals("GetRecord")) {
                elem = dp.getRecord(clear(request.getParameter("identifier")),
                        clear(request.getParameter("metadataPrefix")));
            } else if (formname.equals("ListRecords")) {
                if (token == null)
                    elem = dp.listRecords(clear(request.getParameter("from")),
                            clear(request.getParameter("until")), clear(request
                            .getParameter("set")), clear(request
                            .getParameter("metadataPrefix")));
                else
                    elem = dp.listRecords(token);
            } else if (formname.equals("ListSets")) {
                if (token == null)
                    elem = dp.listSets();
                else
                    elem = dp.listSets(token);
            } else if (formname.equals("ListMetadataFormats")) {
                elem = dp.listMetadataFormats(clear(request
                        .getParameter("identifier")));
            } else
                throw (new BadVerb("badVerb"));
        } catch (OAIError err) {
            oaierror = err;
            elem = doc.createElement("error");
            elem.setAttribute("code", err.getCode());
            elem.appendChild(doc.createTextNode(err.toString()));
            
        }
        
        Element root = doc.createElement("OAI-PMH");
        root.setAttribute("xmlns", "http://www.openarchives.org/OAI/2.0/");
        root.setAttribute("xmlns:xsi",
                "http://www.w3.org/2001/XMLSchema-instance");
        root
                .setAttribute(
                "xsi:schemaLocation",
                "http://www.openarchives.org/OAI/2.0/"
                + "     http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd");
        
        Element date = doc.createElement("responseDate");
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        simple.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        String datestring = simple.format(new java.util.Date()) + "Z";
        date.appendChild(doc.createTextNode(datestring));
        root.appendChild(date);
        
        Element url = doc.createElement("request");
        if ((oaierror != null)
        && (oaierror.getCode().equals("badVerb") || oaierror.getCode()
        .equals("badArgument"))) {
            url.appendChild(doc.createTextNode(request.getRequestURL()
            .toString()));
        } else {
            for (Enumeration enume = request.getParameterNames(); enume
                    .hasMoreElements();) {
                String key = (String) enume.nextElement();
                String value = (String) request.getParameter(key);
                url.setAttribute(key, value);
            }
            
            url.appendChild(doc.createTextNode(request.getRequestURL()
            .toString()));
            
        }
        root.appendChild(url);
        root.appendChild(elem);
        doc.appendChild(root);
        XMLTool.Dom2Stream(doc, out);
        
        out.close();
        return;
    }
    
    private String clear(String in) {
        if (in == null)
            return null;
        else if (in.trim().equals(""))
            return null;
        else
            return in;
    }
    
    /**
     * Special requirement in OAI for encoding & only in request URL
     */
    
    public static String encodeAMP(String input) {
        String result = new String();
        StringTokenizer st = new StringTokenizer(input, "&");
        for (; st.hasMoreTokens();)
            result = result + st.nextToken() + "&amp;";
        result = result.substring(0, result.length() - 5);
        return result;
    }
    
    public static Identity getConfig() {
        return config;
    }
    
    public static void setConfig(Identity aConfig) {
        config = aConfig;
    }
    
    public static RecordFactory getRf() {
        return rf;
    }
    
    public static void setRf(RecordFactory aRf) {
        rf = aRf;
    }
    
}
