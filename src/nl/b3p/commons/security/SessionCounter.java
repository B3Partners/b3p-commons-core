/*
 * SessionCounter.java
 *
 * Created on 15 februari 2005, 20:01
 */

package nl.b3p.commons.security;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author Chris
 */
public class SessionCounter implements HttpSessionListener {

	private static int activeSessions = 0;

	public void sessionCreated(HttpSessionEvent se) {
		activeSessions++;
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		if(activeSessions > 0)
			activeSessions--;
	}

	public static int getActiveSessions() {
		return activeSessions;
	}
}
