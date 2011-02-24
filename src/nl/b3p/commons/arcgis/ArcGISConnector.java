/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.b3p.commons.arcgis;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Voor gebruik: zie de wiki. Howto -> Gebruik van ArcObjects in Java
 *
 * @author Erik
 */
public class ArcGISConnector implements ServletContextListener {
    private final static Log log = LogFactory.getLog(ArcGISConnector.class);

    private ArcGISInitializer arcGISInitializer;

    public void contextInitialized(ServletContextEvent sce) {
        try {
            log.info("ArcObjects Jar bootstrap started.");
            bootstrapArcobjectsJar();
            log.info("ArcObjects Jar bootstrap successful.");

            arcGISInitializer = new ArcGISInitializer();
            arcGISInitializer.contextInitialized(sce);

        } catch (Exception e) {
            log.error("Error connecting to ArcObjects.", e);
        }
    }

    private void bootstrapArcobjectsJar() throws Exception {
        //Get the ArcGIS Engine runtime, if it is available
        String arcObjectsHome = System.getenv("AGSENGINEJAVA");

        //If the ArcGIS Engine runtime is not available, then we can try ArcGIS Desktop runtime
        if (arcObjectsHome == null) {
            arcObjectsHome = System.getenv("AGSDESKTOPJAVA");
        }

        //If no runtime is available, exit application gracefully
        if (arcObjectsHome == null) {
            if (System.getProperty("os.name").toLowerCase().indexOf("win") > -1) {
                throw new Exception("You must have ArcGIS Engine Runtime or ArcGIS Desktop " +
                        "installed in order to execute this sample.\n" +
                        "Install one of the products above, then re-run this sample.");
            } else {
                throw new Exception("You must have ArcGIS Engine Runtime " +
                        "installed in order to execute this sample.\n" +
                        "Install one of the products above, then re-run this sample.");
            }
        }

        //Obtain the relative path to the arcobjects.jar file
        String jarPath = arcObjectsHome + "java" + File.separator + "lib"
                + File.separator + "arcobjects.jar";

        //Create a new file
        File jarFile = new File(jarPath);

        //Test for file existence
        if (!jarFile.exists()) {
            throw new Exception("The arcobjects.jar was not found in the following location: " + jarFile.getParent() + "\n" +
                    "Verify that arcobjects.jar can be located in the specified folder." +
                    "If not present, try uninstalling your ArcGIS software and reinstalling it.");
        }

        //Helps load classes and resources from a search path of URLs
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> sysclass = URLClassLoader.class;

        try {
            Method method = sysclass.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(sysloader, new Object[]{jarFile.toURI().toURL()});
        } catch (Throwable throwable) {
            throw new Exception("Could not add arcobjects.jar to system classloader", throwable);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (arcGISInitializer != null)
                arcGISInitializer.contextDestroyed(sce);
        } catch (Exception e) {
            log.error("Error shutting down ArcObjects connection", e);
        }
    }
}
