/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.game.forex;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Edhendil
 */
public class ForexDAO {

    protected EntityManager em;

    public ForexDAO() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("forex");
        em = emf.createEntityManager();
    }

    public List<ForexHistoricalData> getData(Date since, Date until) {
        return em.createQuery("from ForexHistoricalData where tickTime >= :since and tickTime <= :until", ForexHistoricalData.class).
                setParameter("since", since).setParameter("until", until).getResultList();
    }

    public void save(ForexHistoricalData data) {
        em.persist(data);
    }

    public void save(Collection<ForexHistoricalData> list) {
        em.getTransaction().begin();
        int i = 0;
        for (ForexHistoricalData data : list) {
            em.persist(data);
            i++;
            if (i == 50) {
                i = 0;
                em.clear();
            }
        }
        em.getTransaction().commit();
    }
}
