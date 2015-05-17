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
package nl.b3p.commons.oai.dataprovider20.token;

import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.StringTokenizer;
import nl.b3p.commons.oai.dataprovider20.error.BadResumptionToken;

/**
 * @ author liu_x@cs.odu.edu @ date March-13-2001
 */
public class TokenModem {
    // encoding the token
    /**
     * rules for encoding v=verb c=cursor f=from u=until s=set m=metadataprefix
     */
    public static String encode(Token token) {
        try {
            if (token.getType() == Token.SET_TOKEN) {
                return URLEncoder.encode("v=s&c=" + token.getCursor(), "UTF-8");
            } else if (token.getType() == Token.IDENTIFIER_TOKEN) {
                IdentifierToken t = (IdentifierToken) token;
                String result = URLEncoder.encode("v=i&c=" + t.getCursor() + clear("f", t.from) + clear("u", t.until) + clear("s", t.set) + clear("m", t.metadataPrefix),
                        "UTF-8");
                System.out.println(result);
                return result;
            } else if (token.getType() == Token.RECORDS_TOKEN) {
                RecordsToken t = (RecordsToken) token;
                return URLEncoder.encode("v=r&c=" + t.getCursor() + clear("f", t.from) + clear("u", t.until) + clear("s", t.set) + clear("m", t.metadataPrefix),
                        "UTF-8");
            } else {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // This code should not be never reached
            return null;
        }
    }

    /**
     * decode the token
     */
    public static Token decode(String token) throws BadResumptionToken {

        Token t = null;
        try {
            String value = URLDecoder.decode(token, "UTF-8");
            Hashtable saver = new Hashtable();

            StringTokenizer st = new StringTokenizer(value, "&");
            for (; st.hasMoreTokens();) {
                String piece = st.nextToken();
                saver.put(piece.substring(0, 1), piece.substring(2));
            }

            String type = (String) (saver.get("v"));
            if (type.equals("s")) {
                t = new SetToken();
            } else {
                if (type.equals("r")) {
                    RecordsToken record = new RecordsToken();
                    record.metadataPrefix = (String) saver.get("m");
                    record.from = (String) saver.get("f");
                    record.until = (String) saver.get("u");
                    record.set = (String) saver.get("s");
                    t = record;
                } else if (type.equals("i")) {
                    IdentifierToken record = new IdentifierToken();
                    record.from = (String) saver.get("f");
                    record.until = (String) saver.get("u");
                    record.set = (String) saver.get("s");
                    record.metadataPrefix = (String) saver.get("m");
                    t = record;
                }

            }// end if

            t.setCursor(Integer.parseInt((String) saver.get("c")));
        } // end try
        catch (Exception e) {
            System.out.println(e);
            throw new BadResumptionToken("token format error");
        }
        return t;
    }

    private static String clear(String label, String value) {
        if (value == null) {
            return "";
        } else {
            return "&" + label + "=" + value;
        }
    }
}
