/**
 * $Id$
 */

package nl.b3p.commons.stripes;

import javax.persistence.EntityManager;
import net.sourceforge.stripes.action.ActionBean;

public abstract class JpaTransactionActionBean implements ActionBean {
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
