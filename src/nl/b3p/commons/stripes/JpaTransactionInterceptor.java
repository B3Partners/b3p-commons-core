/*
 * $Id: JpaTransactionInterceptor.java 14349 2010-01-26 15:37:52Z Matthijs $
 */

package nl.b3p.commons.stripes;

import java.beans.Statement;
import java.lang.reflect.Method;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.OnwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import nl.b3p.commons.jpa.JpaUtilServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Interceptor die een transactie start:
 *
 * Indien de ActionBean een Transactional annotatie heeft, na de ActionBeanResolution
 * lifecycle state.
 *
 * De transactie wordt gecommit:
 *
 * Indien de geproduceerde Resolution een attribuut JpaTransactionInterceptor.OPEN_SESSION_IN_VIEW
 * heeft met de waarde Boolean.TRUE, na de RequestComplete state.
 * Zo niet wordt de transactie direct gecommit na de EventHandling state.
 *
 * Let op! Bij open session in view is het niet meer mogelijk om fouten van
 * commit() te tonen omdat de resolution al is uitgevoerd. Het is daarom goed om
 * schrijfacties in ieder geval te flushen of te committen voordat de view wordt
 * uitgevoerd. Na een commit kan dan weer een nieuwe transactie worden gestart
 * voor de view.
 *
 * Indien tijdens het proceed()'en van een state een exception optreedt en er
 * een exception wordt gethrow()'d, wordt een eventuele gestartte transactie
 * gerollback()'d en de exception gerethrowed.
 *
 * Met een parameter van de Transactional annotatie kan worden aangegeven voor
 * welke persistence unit de transactie moet worden gestart. Is deze parameter
 * null dan wordt de default persistence unit (configureerd bij JpaUtilServlet)
 * gebruikt.
 *
 * Let op! Gebruik van de useActionBean tag in view op een bean die een @Transactional
 * annotatie heeft zorgt ervoor dat een eventuele open-session-in-view gesloten
 * wordt na de tag! Bij een closed-session-in-view wordt voor de bean een nieuwe
 * transactie gestart en gecommit.
 */
@Intercepts({LifecycleStage.ActionBeanResolution, LifecycleStage.EventHandling, LifecycleStage.RequestComplete})
public class JpaTransactionInterceptor implements Interceptor {
    private static final Log log = LogFactory.getLog(JpaTransactionInterceptor.class);

    public static final String OPEN_SESSION_IN_VIEW = "openSessionInView";
    public static final String TRANSACTION_PERSISTENCE_UNIT = "transactionPersistenceUnit";

    private String getActiveTransactionPersistenceUnit(ExecutionContext ctx) {
        return (String)ctx.getActionBeanContext().getRequest().getAttribute(TRANSACTION_PERSISTENCE_UNIT);
    }

    public void setActiveTransactionPersistenceUnit(ExecutionContext ctx, String persistenceUnit) {
        ctx.getActionBeanContext().getRequest().setAttribute(TRANSACTION_PERSISTENCE_UNIT, persistenceUnit);
    }

    public Resolution intercept(ExecutionContext ctx) throws Exception {

        Resolution resolution;
        try {
            resolution = ctx.proceed();
        } catch(Throwable e) {
            /* Indien een transactie is gestart door een aanroep van startTransaction()
             * (wordt bepaald door of het request attribute TRANSACTION_PERSISTENCE_UNIT
             * aanwezig is) doe een rollback
             */
            rollbackTransaction(ctx);

            if(e instanceof Exception) {
                throw (Exception)e;
            } else {
                throw new Exception(e);
            }
        }

        if(ctx.getLifecycleStage() == LifecycleStage.ActionBeanResolution) {
            /* Controleer of ActionBean @Transactional annotation heeft.
             * Zo ja, start transactie.
             */

            ActionBean bean = ctx.getActionBean();
            if(bean != null && bean.getClass().isAnnotationPresent(Transactional.class)) {

                if(getActiveTransactionPersistenceUnit(ctx) != null) {
                    log.debug("Transaction already started - using open-session-in-view with useActionBean tag? Not starting another transaction");
                } else {
                    String persistenceUnit = bean.getClass().getAnnotation(Transactional.class).persistenceUnit();

                    log.debug("Starting transaction for ActionBean " + bean.getClass().getName());
                    startTransaction(ctx, persistenceUnit);

                    try {
                        Statement stmt = new Statement(bean, "setEntityManager", new Object[] { getEntityManager(ctx) });
                        stmt.execute();
                        log.debug("Injected entityManager property in ActionBean " + bean.getClass().toString());
                    } catch(Exception e) {
                        log.debug("Error injecting entityManager property in ActionBean " + bean.getClass().toString());
                    }
                }
            }
        } else if(ctx.getLifecycleStage() == LifecycleStage.EventHandling) {

            /* OPEN_SESSION_IN_VIEW kan een request attribute zijn of een
             * parameter van OnwardResolution. De parameter van OnwardResolution
             * override de request attribute indien aanwezig.
             * Bij bijvoorbeeld een StreamingResolution kan de request attribute
             * gebruikt worden omdat die niet extend van OnwardResolution.
             */
            boolean openSessionInView = openSessionInView = Boolean.TRUE.equals(ctx.getActionBeanContext().getRequest().getAttribute(OPEN_SESSION_IN_VIEW));
            if(resolution != null && resolution instanceof OnwardResolution) {
                Object[] param = (Object[])((OnwardResolution)resolution).getParameters().get(OPEN_SESSION_IN_VIEW);
                if(param != null && param.length > 0) {
                    openSessionInView = Boolean.TRUE.equals(param[0]);
                }
            }

            if(!openSessionInView) {
                commitTransaction(ctx);
            } else {
                log.debug("Leaving transaction open for view");
            }

        } else if(ctx.getLifecycleStage() == LifecycleStage.RequestComplete) {

            /* Alleen bij open-session-in-view. Indien transactie al gecommit
             * voor view dan doet deze methode niks
             */
            
            commitTransaction(ctx);
        }

        return resolution;
    }

    private EntityManager getEntityManager(ExecutionContext ctx) {
        String persistenceUnit = getActiveTransactionPersistenceUnit(ctx);

        if(persistenceUnit == null) {
            /* startTransaction() was niet aangeroepen */
            return null;
        }

        return persistenceUnit.equals(Transactional.DEFAULT_PERSISTENCE_UNIT)
                    ? JpaUtilServlet.getThreadEntityManager()
                    : JpaUtilServlet.getThreadEntityManager(persistenceUnit);
    }

    private void closeEntityManager(String persistenceUnit) {
        log.debug("Closing EntityManager for persistence unit " + persistenceUnit);
        if(persistenceUnit.equals(Transactional.DEFAULT_PERSISTENCE_UNIT)) {
            JpaUtilServlet.closeThreadEntityManager();
        } else {
            JpaUtilServlet.closeThreadEntityManager(persistenceUnit);
        }
    }

    private void startTransaction(ExecutionContext ctx, String persistenceUnit) {
        setActiveTransactionPersistenceUnit(ctx, persistenceUnit);

        EntityManager em = getEntityManager(ctx);
        EntityTransaction tx = em.getTransaction();
        log.debug("Starting transaction for persistence unit " + persistenceUnit);
        tx.begin();
    }

    private void commitTransaction(ExecutionContext ctx) {
        EntityManager em = getEntityManager(ctx);

        /* Indien em null is dan was startTransaction() nooit aangeroepen - dus
         * er is nog geen Transactional annotatie tegengekomen. In dat geval
         * dus ook geen transactie om te committen
         */
        
        if(em != null) {
            EntityTransaction tx = em.getTransaction();

            if(tx.isActive()) {
                log.debug("Committing active transaction");
                tx.commit();
                closeEntityManager(getActiveTransactionPersistenceUnit(ctx));
                /* Verwijder attribuut zodat bij closed-session-in-view niet
                 * wordt geprobeerd ook bij RequestComplete state de transactie
                 * te committen (alhoewel deze dan niet active zou zijn, maar
                 * hiermee wordt ook netjes de debug log message vermeden)
                 */
                setActiveTransactionPersistenceUnit(ctx, null);
            } else {
                log.debug("Transaction is not active - not committing");
            }
        }
    }

    private void rollbackTransaction(ExecutionContext ctx) {
        EntityManager em = getEntityManager(ctx);

        if(em != null) {
            EntityTransaction tx = em.getTransaction();

            if(!tx.isActive()) {
                log.debug("Exception occurred but the transaction is not active - not rolling back");
            } else {
                log.error("Exception occurred - rolling back active transaction");
                try {
                    tx.rollback();

                    closeEntityManager(getActiveTransactionPersistenceUnit(ctx));
                    setActiveTransactionPersistenceUnit(ctx, null);
                    
                } catch(Exception e) {
                    /* log de exception maar swallow deze verder, omdat alleen
                     * wordt gerollback()'d indien er al een eerdere exception
                     * was gethrowed. Die wordt door deze te swallowen verder
                     * gethrowed.
                     */
                    log.error("Exception rolling back transaction", e);
                }
            }
        }
    }
}
