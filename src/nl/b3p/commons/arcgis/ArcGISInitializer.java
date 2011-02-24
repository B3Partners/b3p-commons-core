/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.b3p.commons.arcgis;

import com.esri.arcgis.system.AoInitialize;
import com.esri.arcgis.system.EngineInitializer;
import com.esri.arcgis.system.esriLicenseProductCode;
import com.esri.arcgis.system.esriLicenseStatus;
import javax.servlet.ServletContextEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Erik van de Pol
 */
public class ArcGISInitializer {
    private final static Log log = LogFactory.getLog(ArcGISInitializer.class);

    private AoInitialize aoInit = null;

    public void contextInitialized(ServletContextEvent sce) throws Exception {
        //Initialize engine console application
        EngineInitializer.initializeEngine();

        //Initialize ArcGIS license
        aoInit = new AoInitialize();
        initializeArcGISLicenses(aoInit);
    }

    public void contextDestroyed(ServletContextEvent sce) throws Exception {
        //Ensure any ESRI libraries are unloaded in the correct order
        if (aoInit != null)
            aoInit.shutdown();
    }

    private void initializeArcGISLicenses(AoInitialize aoInit) throws Exception {
        log.info("Searching ArcGIS license...");
        if (aoInit.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeArcEditor)
                == esriLicenseStatus.esriLicenseAvailable) {
            log.info("ArcGIS License used: ArcEditor");
            aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeArcEditor);
        } else if (aoInit.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeEngine)
                == esriLicenseStatus.esriLicenseAvailable) {
            log.info("ArcGIS License used: Engine");
            aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeEngine);
        } else if (aoInit.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeArcView)
                == esriLicenseStatus.esriLicenseAvailable) {
            log.info("ArcGIS License used: ArcView");
            aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeArcView);
        } else if (aoInit.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeEngineGeoDB)
                == esriLicenseStatus.esriLicenseAvailable) {
            log.info("ArcGIS License used: EngineGeoDB");
            aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeEngineGeoDB);
        } else if (aoInit.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeArcInfo)
                == esriLicenseStatus.esriLicenseAvailable) {
            log.info("ArcGIS License used: ArcInfo");
            aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeArcInfo);
        } else if (aoInit.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeArcServer)
                == esriLicenseStatus.esriLicenseAvailable) {
            log.info("ArcGIS License used: ArcServer");
            aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeArcServer);
        } else {
            throw new Exception("Could not initialize any ESRI license.");
        }
    }

}
