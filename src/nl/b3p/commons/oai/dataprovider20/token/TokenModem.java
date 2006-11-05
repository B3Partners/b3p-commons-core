package nl.b3p.commons.oai.dataprovider20.token;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;
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
			if (token.getType() == Token.SET_TOKEN)
				return URLEncoder.encode("v=s&c=" + token.getCursor(), "UTF-8");
			else if (token.getType() == Token.IDENTIFIER_TOKEN) {
				IdentifierToken t = (IdentifierToken) token;
				String result = URLEncoder.encode("v=i&c=" + t.getCursor()
						+ clear("f", t.from) + clear("u", t.until)
						+ clear("s", t.set) + clear("m", t.metadataPrefix),
						"UTF-8");
				System.out.println(result);
				return result;
			} else if (token.getType() == Token.RECORDS_TOKEN) {
				RecordsToken t = (RecordsToken) token;
				return URLEncoder.encode("v=r&c=" + t.getCursor()
						+ clear("f", t.from) + clear("u", t.until)
						+ clear("s", t.set) + clear("m", t.metadataPrefix),
						"UTF-8");
			} else
				return null;
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
			if (type.equals("s"))
				t = new SetToken();
			else {
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
		if (value == null)
			return "";
		else
			return "&" + label + "=" + value;
	}
}
