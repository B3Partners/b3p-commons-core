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

/*
 * $Id: ASelectUtils.java 699 2005-06-22 11:29:02Z Matthijs $
 */
package nl.b3p.commons.security.aselect;

import java.util.Map;
import java.util.Hashtable;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Utility class met functies voor A-Select API.
 */
public class ASelectUtils {

    private ASelectUtils() {
    }

    /* Ondersteunt geen parameters die meerdere keren voorkomen */
    public static Map parseQueryString(String q, String enc)
            throws UnsupportedEncodingException {
        String[] params = q.split("&");

        Map results = new Hashtable(params.length);

        for (int i = 0; i < params.length; i++) {
            String[] kv = params[i].split("=", 2);
            results.put(URLDecoder.decode(kv[0], enc), kv.length > 1 ? URLDecoder.decode(kv[1], enc) : "");
        }

        return results;
    }

    public static String appendQueryParameters(String URL, Map params, String enc)
            throws UnsupportedEncodingException {
        if (params == null || params.size() == 0) {
            return URL;
        }

        StringBuffer str = new StringBuffer(URL);

        if (URL.indexOf((int) '?') == -1) {
            str.append('?');
        } else {
            if (URL.length() > 0) {
                char c = URL.charAt(URL.length() - 1);
                if (c != '&' && c != '?') {
                    str.append('&');
                }
            }
        }

        Iterator i = params.entrySet().iterator();
        boolean first = true;
        while (i.hasNext()) {
            if (!first) {
                str.append('&');
            } else {
                first = false;
            }

            Map.Entry kv = (Map.Entry) i.next();
            str.append(URLEncoder.encode(kv.getKey().toString(), enc));
            str.append('=');
            str.append(URLEncoder.encode(kv.getValue().toString(), enc));
        }

        return str.toString();
    }
}
