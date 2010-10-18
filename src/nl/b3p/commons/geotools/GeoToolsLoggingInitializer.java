/**
 * $Id$
 */

package nl.b3p.commons.geotools;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.geotools.util.logging.Logging;

/**
 * Configureer in web.xml als:
    <listener>
        <listener-class>nl.b3p.commons.geotools.GeoToolsLoggingInitializer</listener-class>
    </listener>
 *
 * Voorbeeld log4j.properties:
log4j.logger.org.geotools=WARN
log4j.logger.org.geotools.data.wfs=ERROR
log4j.logger.org.geotools.factory=ERROR
 */
public class GeoToolsLoggingInitializer implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        /* Van http://docs.codehaus.org/display/GEOTDOC/Logging */

        final Logging logging = Logging.ALL;
        try {
            logging.setLoggerFactory("org.geotools.util.logging.CommonsLoggerFactory");
        } catch (ClassNotFoundException commonsException) {
            try {
                logging.setLoggerFactory("org.geotools.util.logging.Log4JLoggerFactory");
            } catch (ClassNotFoundException log4jException) {
                // Nothing to do, we already tried our best.
            }
        }

        /* De INFO messages van deze logger zijn niet erg boeiend en zorgen
         * voor een boel logregels.
         */
        Logger l = Logger.getRootLogger().getLoggerRepository().getLogger("org.geotools.factory");
        l.setLevel(Level.WARN);
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}