/* $Id$ */

package nl.b3p.commons.jpa;

import java.lang.reflect.Method;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;
import org.directwebremoting.annotations.GlobalFilter;

/**
 * Globaal DWR filter dat een transactie start voor de uitvoer van een DWR
 * methode en deze daarna commit (en bij een exception rollback'ed).
 *
 * Omdat niet elke methode een transactie nodig heeft moet wel op een class
 * of een methode een @TransactionalAjax annotatie aanwezig zijn. Met de
 * persistenceUnit property van de annotatie kan eventueel een andere dan de
 * default persistence unit van het JpaUtilServlet gebruikt worden.
 *
 * Dit filter kan worden geconfigureerd in dwr.xml:
 *
    <dwr>
        <allow>
            <!-- globaal -->
            <filter class="nl.b3p.commons.jpa.DwrTransactionFilter"/>
            ...
            <create creator="new" javascript="MyClass">
               <!-- alleen op enkele class -->
               <filter class="nl.b3p.commons.jpa.DwrTransactionFilter"/>
            </create>
        </allow>
    </dwr>
 *
 * Indien DWR met annotations wordt geconfigureerd deze class opnemen in de
 * "classes" init parameter van org.directwebremoting.servlet.DwrServlet, config
 * in dwr.xml is dan niet nodig.
 * 
 * @author Matthijs
 */
@GlobalFilter
public class DwrJpaTransactionFilter implements AjaxFilter  {
    private static final Log log = LogFactory.getLog(DwrJpaTransactionFilter.class);

    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception {

        TransactionalAjax annotation = method.getAnnotation(TransactionalAjax.class);
        if(annotation == null) {
            annotation = method.getDeclaringClass().getAnnotation(TransactionalAjax.class);
        }

        String persistenceUnit = null;
        EntityManager em = null;
        EntityTransaction tx = null;

        if(annotation != null) {
            persistenceUnit = annotation.persistenceUnit();

            em = persistenceUnit.equals(TransactionalAjax.DEFAULT_PERSISTENCE_UNIT)
                    ? JpaUtilServlet.getThreadEntityManager()
                    : JpaUtilServlet.getThreadEntityManager(persistenceUnit);

            log.debug("Starting transaction for persistence unit " + persistenceUnit);
            tx = em.getTransaction();
            tx.begin();
        }

        try {
            Object ret = chain.doFilter(obj, method, params);

            if(tx != null && tx.isActive()) {
                log.debug("Committing active transaction");
                tx.commit();
            }
            return ret;
        } catch (Exception e) {
			log.error("Exception occured during DWR call" + (tx.isActive() ? ", rolling back transaction" : " - no transaction active"), e);

            if(tx.isActive()) {
                try {
                    tx.rollback();
                } catch(Exception e2) {
                    /* log de exception maar swallow deze verder, omdat alleen
                     * wordt gerollback()'d indien er al een eerdere exception
                     * was gethrowed. Die wordt door deze te swallowen verder
                     * gethrowed.
                     */
                    log.error("Exception rolling back transaction", e2);
                }
            }
			throw e;
		} finally {
            if(persistenceUnit != null) {
                log.debug("Closing EntityManager for persistence unit " + persistenceUnit);
                if(persistenceUnit.equals(TransactionalAjax.DEFAULT_PERSISTENCE_UNIT)) {
                    JpaUtilServlet.closeThreadEntityManager();
                } else {
                    JpaUtilServlet.closeThreadEntityManager(persistenceUnit);
                }
            }
        }
    }
}
